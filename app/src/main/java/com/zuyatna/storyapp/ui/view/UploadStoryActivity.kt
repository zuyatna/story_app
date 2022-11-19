package com.zuyatna.storyapp.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.coroutineScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.zuyatna.storyapp.R
import com.zuyatna.storyapp.databinding.ActivityUploadStoryBinding
import com.zuyatna.storyapp.manager.PreferenceManager
import com.zuyatna.storyapp.ui.viewmodel.UploadStoryViewModel
import com.zuyatna.storyapp.utils.NetworkResult
import com.zuyatna.storyapp.utils.reduceFileImage
import com.zuyatna.storyapp.utils.rotateBitmap
import com.zuyatna.storyapp.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class UploadStoryActivity : AppCompatActivity() {
    private val binding: ActivityUploadStoryBinding by lazy {
        ActivityUploadStoryBinding.inflate(layoutInflater)
    }

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val uploadStoryViewModel: UploadStoryViewModel by viewModels()

    private var getFile: File? = null
    private var uploadJob: Job = Job()
    private var isDescFilled = false
    private var isImageFilled = false
    private var location: Location? = null

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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        permissionGranted()

        setUploadButtonEnable()

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

            val bos = ByteArrayOutputStream()
            result.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapData = bos.toByteArray()

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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        Timber.d("$permissions")
        when {
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getLastLocation()
            }
            else -> {
                binding.checkbox.isChecked = false
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if(it != null) {
                    location = it

                    Timber.tag("getLastLocation").d("${it.latitude}, ${it.longitude}")
                } else {
                    binding.checkbox.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun uploadStory(auth: String) {
        val file = reduceFileImage(getFile as File)
        val requestImageFile = file.asRequestBody("image/jpg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )

        var lat : String? = null
        var lon : String? = null

        if(location != null) {
            lat = location?.latitude.toString()
            lon = location?.longitude.toString()
        }

        val description = binding.etUploadStoryDescription.text.toString().trim()

        setProgressBar(true)

        lifecycle.coroutineScope.launchWhenResumed {
            if (uploadJob.isActive) uploadJob.cancel()
            uploadJob = launch {
                uploadStoryViewModel.uploadStory(auth, description, lat, lon, imageMultipart).collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            Toast.makeText(this@UploadStoryActivity, getString(R.string.upload_file_successful), Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@UploadStoryActivity, MainActivity::class.java))
                            finishAffinity()
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