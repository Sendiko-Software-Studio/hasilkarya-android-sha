package com.system.hasilkarya.qr.presentation

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.system.hasilkarya.R
import com.system.hasilkarya.core.ui.theme.poppinsFont
import com.system.hasilkarya.core.utils.ding
import com.system.hasilkarya.qr.domain.BarcodeAnalyzer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun QrScanComponent(
    onResult: (String) -> Unit,
    navigateBack: () -> Unit,
    title: String,
    isValid: Boolean,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    var result by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopStart,
            content = {
                Box(
                    contentAlignment = Alignment.Center,
                    content = {
                        AndroidView(
                            modifier = Modifier,
                            factory = { androidViewContext ->
                                PreviewView(androidViewContext).apply {
                                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                    )
                                    implementationMode =
                                        PreviewView.ImplementationMode.COMPATIBLE
                                }
                            },
                            update = { previewView ->
                                val cameraSelector: CameraSelector = CameraSelector.Builder()
                                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                    .build()
                                val cameraExecutor: ExecutorService =
                                    Executors.newSingleThreadExecutor()
                                val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                                    ProcessCameraProvider.getInstance(context)

                                cameraProviderFuture.addListener(
                                    {
                                        preview = Preview.Builder().build().also {
                                            it.setSurfaceProvider(previewView.surfaceProvider)
                                        }
                                        val cameraProvider: ProcessCameraProvider =
                                            cameraProviderFuture.get()
                                        val barcodeAnalyser = BarcodeAnalyzer { barcodes ->
                                            barcodes.forEach { barcode ->
                                                barcode.rawValue?.let { barcodeValue ->
                                                    onResult(barcodeValue)
                                                    result = barcodeValue
                                                    ding(context)
                                                }
                                            }
                                        }
                                        val imageAnalysis: ImageAnalysis =
                                            ImageAnalysis.Builder()
                                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                                .build()
                                                .also {
                                                    it.setAnalyzer(
                                                        cameraExecutor,
                                                        barcodeAnalyser
                                                    )
                                                }

                                        try {
                                            cameraProvider.unbindAll()
                                            cameraProvider.bindToLifecycle(
                                                lifecycleOwner,
                                                cameraSelector,
                                                preview,
                                                imageAnalysis
                                            )
                                        } catch (e: Exception) {
                                            Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                                        }
                                    }, ContextCompat.getMainExecutor(context)
                                )
                            }
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.scan_rectangle),
                            contentDescription = "scan",
                            modifier = Modifier.size(256.dp),
                            tint = Color.White
                        )
                    },
                )
                IconButton(
                    onClick = { navigateBack() },
                    content = {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "kembali",
                        )
                    },
                    modifier = Modifier.padding(top = 24.dp)
                )
            }
        )

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                Text(
                    modifier = Modifier
                        .padding(
                            vertical = 24.dp,
                            horizontal = 24.dp
                        )
                        .fillMaxWidth(),
                    text = if (isValid) "Scan QR berhasil!" else "Scan QR $title",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = poppinsFont
                )
                Spacer(modifier = Modifier.size(16.dp))
                AnimatedVisibility(
                    visible = isValid,
                    modifier = Modifier.padding(bottom = 32.dp),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically(),
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = "ceklist",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            }
        )
    }
}