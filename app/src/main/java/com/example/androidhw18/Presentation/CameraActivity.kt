package com.example.androidhw18.Presentation

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.androidhw18.Data.App
import com.example.androidhw18.Data.SightDao
import com.example.androidhw18.databinding.ActivityMain2Binding
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor

private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss"

class CameraActivity() : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels{
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val sightDao: SightDao = (application as App).db.sightDao()
                return MainViewModel(sightDao) as T
            }
        }
    }
    private var imageCapture: ImageCapture? = null

    private lateinit var executer: Executor
    private lateinit var binding: ActivityMain2Binding
    private val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
        .format(System.currentTimeMillis())

    private val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ permissions ->
        if (permissions){
            startCamera()
        }else{
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                Toast.makeText(this, "Need permission to use camera.", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Please grant permission to use camera", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        executer = ContextCompat.getMainExecutor(this)
        binding.takePhotoButton.setOnClickListener { takePhoto() }
        binding.butExit.setOnClickListener { finish() }
        checkPermissions()
    }

    private fun takePhoto(){
        val imageCapture = imageCapture ?: return
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
        imageCapture.takePicture(
            outputOptions,
            executer,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Photo saved on: ${outputFileResults.savedUri}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Glide.with(this@CameraActivity)
                        .load(outputFileResults.savedUri)
                        .circleCrop()
                        .into(binding.imagePreview)
                   viewModel.add(name, outputFileResults.savedUri.toString())
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Photo failed: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    exception.printStackTrace()
                }
            }
        )
    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            imageCapture = ImageCapture.Builder().build()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture
            )
            }, executer)
    }

    private fun checkPermissions(){
        val isAllGranted = REQUEST_PERMISSIONS.all {
                permission -> ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllGranted){
            startCamera()
            Toast.makeText(this, "permission is Granted", Toast.LENGTH_SHORT).show()
        }else{
            REQUEST_PERMISSIONS.map {
                launcher.launch(it)
            }
        }
    }

    companion object{
        private val REQUEST_PERMISSIONS: Array<String> = buildList {
            add(android.Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}