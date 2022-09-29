package com.zuyatna.storyapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.zuyatna.storyapp.R
import com.zuyatna.storyapp.api.ApiConfig
import com.zuyatna.storyapp.databinding.ActivityUploadStoryBinding
import com.zuyatna.storyapp.manager.PreferenceManager
import com.zuyatna.storyapp.model.upload.UploadStoryModel
import com.zuyatna.storyapp.utils.NetworkResult
import com.zuyatna.storyapp.viewmodel.UploadStoryViewModel
import com.zuyatna.storyapp.viewmodel.UploadStoryViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class UploadStoryActivity : AppCompatActivity() {
    private val binding: ActivityUploadStoryBinding by lazy {
        ActivityUploadStoryBinding.inflate(layoutInflater)
    }

    private val format = "dd-MMM-yyyy"

    private val timeStamp: String = SimpleDateFormat(
        format,
        Locale.US
    ).format(System.currentTimeMillis())

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var uploadStoryViewModel: UploadStoryViewModel
    private lateinit var currentPhotoPath: String

    private var getFile: File? = null
    private var uploadJob: Job = Job()
    private var isDescFilled = false
    private var isImageFilled = false

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.upload_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferenceManager = PreferenceManager(this)

        val uploadStoryModel = UploadStoryModel(ApiConfig.getInstance())
        uploadStoryViewModel = ViewModelProvider(this, UploadStoryViewModelFactory(uploadStoryModel))[UploadStoryViewModel::class.java]

        permissionGranted()

        setUploadButtonEnable()

        binding.btUploadStoryCamera.setOnClickListener {
            startTakePhoto()
        }

        binding.btUploadStoryGallery.setOnClickListener {
            startGallery()
        }

        binding.etUploadStoryDescription.addTextChangedListener(onTextChanged = { s, _, _, _ ->
            isDescFilled = !s.isNullOrEmpty()
            setUploadButtonEnable()
        })

        binding.btUploadStoryUpload.setOnClickListener {
            uploadStory(preferenceManager.userToken)
        }
    }

    private fun setUploadButtonEnable() {
        val etDesc = binding.etUploadStoryDescription
        val btLogin = binding.btUploadStoryUpload

        if (etDesc.text.toString().isEmpty()) {
            btLogin.isEnabled = false
        } else {
            btLogin.isEnabled = isDescFilled && isImageFilled
        }
    }

    @Suppress("BooleanMethodIsAlwaysInverted")
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun permissionGranted() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.zuyatna.storyapp.view",
                it
            )

            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile

            val result = BitmapFactory.decodeFile(myFile.path)
            binding.ivUploadStoryPreview.setImageBitmap(result)

            isImageFilled = true
            setUploadButtonEnable()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"

        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile

            binding.ivUploadStoryPreview.setImageURI(selectedImg)

            isImageFilled = true
            setUploadButtonEnable()
        }
    }

    private fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    private fun uploadStory(auth: String) {
        val file = reduceFileImage(getFile as File)
        val description = binding.etUploadStoryDescription.text.toString().trim()

        setProgressBar(true)

        lifecycle.coroutineScope.launchWhenResumed {
            if (uploadJob.isActive) uploadJob.cancel()
            uploadJob = launch {
                uploadStoryViewModel.uploadStory(auth, description, file).collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            Toast.makeText(this@UploadStoryActivity, getString(R.string.upload_file_successful), Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@UploadStoryActivity, MainActivity::class.java))
                            finish()
                        }
                        is NetworkResult.Error -> {
                            Toast.makeText(this@UploadStoryActivity, getString(R.string.upload_file_error), Toast.LENGTH_SHORT).show()
                            setProgressBar(false)
                        }
                    }
                }
            }
        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)

        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))

        return file
    }

    private fun setProgressBar(loading: Boolean) {
        when(loading) {
            true -> {
                binding.btUploadStoryUpload.visibility = View.GONE
                binding.pbUploadStory.visibility = View.VISIBLE
            }
            false -> {
                binding.btUploadStoryUpload.visibility = View.VISIBLE
                binding.pbUploadStory.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}