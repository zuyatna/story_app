package com.zuyatna.storyapp.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.zuyatna.storyapp.R
import com.zuyatna.storyapp.databinding.ActivityUploadStoryBinding
import com.zuyatna.storyapp.manager.PreferenceManager
import com.zuyatna.storyapp.ui.viewmodel.UploadStoryViewModel
import com.zuyatna.storyapp.utils.rotateBitmap
import com.zuyatna.storyapp.utils.uriToFile
import kotlinx.coroutines.Job
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class UploadStoryActivity : AppCompatActivity() {
    private val binding: ActivityUploadStoryBinding by lazy {
        ActivityUploadStoryBinding.inflate(layoutInflater)
    }

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var uploadStoryViewModel: UploadStoryViewModel

    private var getFile: File? = null
    private var uploadJob: Job = Job()
    private var isDescFilled = false
    private var isImageFilled = false

    companion object {
        const val CAMERA_X_RESULT = 200

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

        preferenceManager = PreferenceManager(this)

//        val uploadStoryRepository = UploadStoryRepository(ApiConfig.getInstance())
//        uploadStoryViewModel = ViewModelProvider(this, UploadStoryViewModelFactory(uploadStoryRepository))[UploadStoryViewModel::class.java]

        permissionGranted()

//        setUploadButtonEnable()

        binding.apply {
            ivBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        binding.btUploadStoryCamera.setOnClickListener {
            startCameraX()
        }

        binding.btUploadStoryGallery.setOnClickListener {
            startGallery()
        }

        binding.etUploadStoryDescription.addTextChangedListener(onTextChanged = { s, _, _, _ ->
            isDescFilled = !s.isNullOrEmpty()
            setUploadButtonEnable()
        })

        binding.btUploadStoryUpload.setOnClickListener {
//            uploadStory(preferenceManager.userToken)
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

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    @Suppress("DEPRECATION", "BlockingMethodInNonBlockingContext")
    private val launcherIntentCameraX = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.ivUploadStoryPreview.setImageBitmap(result)

            getFile = File(Environment.getExternalStorageDirectory().toString() + File.separator + "image")
            getFile = myFile

            // Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            result.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapData = bos.toByteArray()

            // Write the bytes in file
            val fos = FileOutputStream(myFile)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
            getFile

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

//    private fun uploadStory(auth: String) {
//        val file = reduceFileImage(getFile as File)
//        val description = binding.etUploadStoryDescription.text.toString().trim()
//
//        setProgressBar(true)
//
//        lifecycle.coroutineScope.launchWhenResumed {
//            if (uploadJob.isActive) uploadJob.cancel()
//            uploadJob = launch {
//                uploadStoryViewModel.uploadStory(auth, description, file).collect { result ->
//                    when (result) {
//                        is NetworkResult.Success -> {
//                            Toast.makeText(this@UploadStoryActivity, getString(R.string.upload_file_successful), Toast.LENGTH_SHORT).show()
//                            startActivity(Intent(this@UploadStoryActivity, MainActivity::class.java))
//                            finish()
//                        }
//                        is NetworkResult.Error -> {
//                            Toast.makeText(this@UploadStoryActivity, getString(R.string.upload_file_error), Toast.LENGTH_SHORT).show()
//                            setProgressBar(false)
//                        }
//                    }
//                }
//            }
//        }
//    }

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
}