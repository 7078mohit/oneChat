package com.example.chattingappscreens.presentation.chatting

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Cameraswitch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File

@Composable
fun ClickImage(onClick:(Uri) -> Unit , onCancel : (Boolean) -> Unit){

    val permission = Manifest.permission.CAMERA
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember{ ImageCapture.Builder().build()}
    val context = LocalContext.current
    val tempFile = File(context.cacheDir ,  "image_${System.currentTimeMillis()}_.jpg")
    val photoUri = FileProvider.getUriForFile(context  ,"${context.packageName}.provider", tempFile)
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA)}


    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            granted ->
        if (granted){
            Toast.makeText(context , "Permission Granted" , Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context , "Permission Denied" , Toast.LENGTH_LONG).show()
        }
    }


    val previewView = remember { mutableStateOf(PreviewView(context)) }


    fun bind(){

        val preview = Preview.Builder().build()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener(
            {
                val cameraProvider = cameraProviderFuture.get()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
                preview.setSurfaceProvider(previewView.value.surfaceProvider)
            },
            ContextCompat.getMainExecutor(context)
        )
    }


    LaunchedEffect(cameraSelector) {
        bind()
    }


    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { context ->
                previewView.value
            },
            modifier = Modifier.fillMaxSize()
        )


        Row(modifier = Modifier.fillMaxWidth().padding(70.dp).align(Alignment.BottomCenter) , horizontalArrangement = Arrangement.SpaceBetween  ) {


            IconButton( onClick = {onCancel(false)}) {
                Icon(imageVector = Icons.Default.Cancel , contentDescription = "cancel" , modifier =Modifier.size(35.dp) , tint = MaterialTheme.colorScheme.surfaceDim)
            }

            IconButton(
                onClick = {
                    if(ContextCompat.checkSelfPermission(context , permission) == PackageManager.PERMISSION_GRANTED){

                        val outputOptions = ImageCapture.OutputFileOptions.Builder(tempFile).build()
                        imageCapture.takePicture(
                            outputOptions,
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback{
                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                   onClick(photoUri)
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    exception.printStackTrace()
                                }

                            }
                        )
                    }
                    else{
                        permissionLauncher.launch(permission)
                    }
                }
            ) {
                Icon(imageVector = Icons.Outlined.Camera, contentDescription = "camera" , modifier = Modifier.size(35.dp) , tint = MaterialTheme.colorScheme.surface)
            }

            IconButton(onClick = {
                cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }
            }) {
                Icon(imageVector = Icons.Outlined.Cameraswitch, contentDescription = "changeCamera" , modifier =Modifier.size(35.dp) , tint = MaterialTheme.colorScheme.surface)
            }

        }
    }
}