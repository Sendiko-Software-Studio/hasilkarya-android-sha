@file:OptIn(ExperimentalMaterial3Api::class)

package com.system.hasilkarya.qr.presentation

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.system.hasilkarya.R
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.ui.components.ContentBoxWithNotification
import com.system.hasilkarya.core.ui.theme.poppinsFont
import com.system.hasilkarya.dashboard.presentation.ScanOptions
import com.system.hasilkarya.dashboard.presentation.ScanOptions.Driver
import com.system.hasilkarya.dashboard.presentation.ScanOptions.None
import com.system.hasilkarya.dashboard.presentation.ScanOptions.Pos
import com.system.hasilkarya.dashboard.presentation.ScanOptions.Truck
import com.system.hasilkarya.qr.data.ratioData
import com.system.hasilkarya.qr.domain.BarcodeAnalyzer
import kotlinx.coroutines.delay
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrScreen(
    state: QrScreenState,
    onEvent: (QrScreenEvent) -> Unit,
    onNavigateBack: (String) -> Unit,
) {
    LaunchedEffect(
        key1 = state,
        block = {
            if (state.isPostSuccessful)
                delay(1000)
                onNavigateBack(Destination.DashboardScreen.name)

            if (state.notificationMessage.isNotBlank())
                onEvent(QrScreenEvent.OnClearNotification)
        }
    )
    ContentBoxWithNotification(
        message = state.notificationMessage,
        isLoading = state.isLoading,
        isErrorNotification = state.isRequestFailed.isFailed
    ) {
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        start = 8.dp,
                        end = 8.dp
                    )
            ) {
                AnimatedVisibility(
                    visible = state.currentlyScanning == Driver,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally(),
                    content = {
                        ScanDriverForm(
                            onResult = { onEvent(QrScreenEvent.OnDriverIdRegistered(it)) },
                            navigateBack = {
                                onNavigateBack(it)
                            }
                        )
                    }
                )
                AnimatedVisibility(
                    visible = state.currentlyScanning == Truck,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally(),
                    content = {
                        ScanTruckForm(
                            onResult = { onEvent(QrScreenEvent.OnTruckIdRegistered(it)) },
                            prevForm = {
                                onEvent(QrScreenEvent.OnNavigateForm(Driver))
                            }
                        )
                    }
                )
                AnimatedVisibility(
                    visible = state.currentlyScanning == Pos,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally(),
                    content = {
                        ScanPostForm(
                            onResult = { onEvent(QrScreenEvent.OnPosIdRegistered(it)) },
                            prevForm = {
                                onEvent(QrScreenEvent.OnNavigateForm(Truck))
                            }
                        )
                    }
                )
                AnimatedVisibility(visible = state.currentlyScanning == None) {
                    val listRatio = listOf(
                        ratioData(ratio = 0.3, ratioText = "30%"),
                        ratioData(ratio = 0.4, ratioText = "40%"),
                        ratioData(ratio = 0.5, ratioText = "50%"),
                        ratioData(ratio = 0.6, ratioText = "60%"),
                        ratioData(ratio = 0.7, ratioText = "70%"),
                        ratioData(ratio = 0.8, ratioText = "80%"),
                        ratioData(ratio = 0.9, ratioText = "90%"),
                        ratioData(ratio = 1.0, ratioText = "100%"),
                    )
                    var ratio by remember {
                        mutableDoubleStateOf(0.0)
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    onEvent(QrScreenEvent.OnNavigateForm(Pos))
                                },
                                content = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "kembali"
                                    )
                                }
                            )
                        }

                        Text(
                            text = "Pilih observasi rasio presentasi",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            fontFamily = poppinsFont
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            content = {
                                items(listRatio) {
                                    InputChip(
                                        selected = ratio == it.ratio,
                                        onClick = { ratio = it.ratio },
                                        label = { Text(text = it.ratioText, fontFamily = poppinsFont, fontSize = 16.sp) },
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            },
                        )

                        Spacer(modifier = Modifier.size(16.dp))
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(128.dp),
                            value = state.remarks,
                            onValueChange = {
                                onEvent(QrScreenEvent.OnNewRemarks(it))
                            },
                            placeholder = { Text(text = "keterangan")},
                            leadingIcon = { Icon(imageVector = Icons.Default.TextSnippet, contentDescription = "keterangan")},
                            shape = RoundedCornerShape(16.dp)
                        )
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                onEvent(QrScreenEvent.OnSelectedRatio(ratio))
                                onEvent(QrScreenEvent.SaveMaterial)
                            }
                        ) {
                            Text(text = "Simpan data")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScanDriverForm(
    onResult: (String) -> Unit,
    navigateBack: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(3f),
        ) {
            AndroidView(
                modifier = Modifier,
                factory = { androidViewContext ->
                    PreviewView(androidViewContext).apply {
                        this.scaleType = PreviewView.ScaleType.FILL_CENTER
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
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
                                        result = barcodeValue
                                    }
                                }
                            }
                            val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()
                                .also {
                                    it.setAnalyzer(cameraExecutor, barcodeAnalyser)
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
                painter = painterResource(id = R.drawable.scanning),
                contentDescription = "scan",
                modifier = Modifier.size(200.dp),
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .weight(1f),
        ) {
            QrFormHeader(navigateBack = { navigateBack(Destination.DashboardScreen.name) }, result = result, currentlyScanning = Driver)
            Spacer(modifier = Modifier.size(16.dp))
            AnimatedVisibility(
                visible = result.isNotBlank(),
                enter = fadeIn(),
                exit = fadeOut(),
                content = {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onResult(result)
                        },
                        content = {
                            Text(text = "Lanjut scan Truk", fontFamily = poppinsFont)
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun ScanTruckForm(
    onResult: (String) -> Unit,
    prevForm: (ScanOptions) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(3f),
        ) {
            AndroidView(
                modifier = Modifier,
                factory = { androidViewContext ->
                    PreviewView(androidViewContext).apply {
                        this.scaleType = PreviewView.ScaleType.FILL_CENTER
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
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

                    cameraProviderFuture.addListener({
                        preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val cameraProvider: ProcessCameraProvider =
                            cameraProviderFuture.get()
                        val barcodeAnalyser = BarcodeAnalyzer { barcodes ->
                            barcodes.forEach { barcode ->
                                barcode.rawValue?.let { barcodeValue ->
                                    result = barcodeValue
                                }
                            }
                        }
                        val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor, barcodeAnalyser)
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
                    }, ContextCompat.getMainExecutor(context))
                }
            )
            Icon(
                painter = painterResource(id = R.drawable.scanning),
                contentDescription = "scan",
                modifier = Modifier.size(200.dp),
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .weight(1f),
        ) {
            QrFormHeader(
                result = result,
                currentlyScanning = Truck,
                navigateBack = {
                    prevForm(Driver)
                },
            )

            Spacer(modifier = Modifier.size(16.dp))
            AnimatedVisibility(
                visible = result.isNotBlank(),
                enter = fadeIn(),
                exit = fadeOut(),
                content = {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onResult(result)
                        },
                        content = {
                            Text(text = "Lanjut scan Pos", fontFamily = poppinsFont)
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun ScanPostForm(
    onResult: (String) -> Unit,
    prevForm: (ScanOptions) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.weight(3f),
        ) {
            AndroidView(
                modifier = Modifier,
                factory = { androidViewContext ->
                    PreviewView(androidViewContext).apply {
                        this.scaleType = PreviewView.ScaleType.FILL_CENTER
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
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

                    cameraProviderFuture.addListener({
                        preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val cameraProvider: ProcessCameraProvider =
                            cameraProviderFuture.get()
                        val barcodeAnalyser = BarcodeAnalyzer { barcodes ->
                            barcodes.forEach { barcode ->
                                barcode.rawValue?.let { barcodeValue ->
                                    result = barcodeValue
                                }
                            }
                        }
                        val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor, barcodeAnalyser)
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
                    }, ContextCompat.getMainExecutor(context))
                }
            )
            Icon(
                painter = painterResource(id = R.drawable.scanning),
                contentDescription = "scan",
                modifier = Modifier.size(200.dp),
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .weight(1f),
        ) {
            QrFormHeader(navigateBack = { prevForm(Truck) }, result = result, currentlyScanning = Pos)

            Spacer(modifier = Modifier.size(16.dp))
            AnimatedVisibility(
                visible = result.isNotBlank(),
                enter = fadeIn(),
                exit = fadeOut(),
                content = {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onResult(result)
                        },
                        content = {
                            Text(text = "Lanjut isi data", fontFamily = poppinsFont)
                        }
                    )
                }
            )
        }
    }
}