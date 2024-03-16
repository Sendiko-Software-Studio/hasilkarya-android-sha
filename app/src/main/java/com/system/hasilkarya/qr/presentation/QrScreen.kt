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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.ui.components.ContentBoxWithNotification
import com.system.hasilkarya.core.ui.components.NormalTextField
import com.system.hasilkarya.dashboard.presentation.ScanOptions
import com.system.hasilkarya.dashboard.presentation.ScanOptions.Driver
import com.system.hasilkarya.dashboard.presentation.ScanOptions.None
import com.system.hasilkarya.dashboard.presentation.ScanOptions.Pos
import com.system.hasilkarya.dashboard.presentation.ScanOptions.Truck
import com.system.hasilkarya.qr.domain.BarcodeAnalyzer
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
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                AnimatedVisibility(
                    visible = state.currentlyScanning == Driver,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally(),
                    content = {
                        ScanDriverForm(onResult = { onEvent(QrScreenEvent.OnDriverIdRegistered(it)) })
                    }
                )
                AnimatedVisibility(
                    visible = state.currentlyScanning == Truck,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally(),
                    content = {
                        ScanTruckForm(onResult = { onEvent(QrScreenEvent.OnTruckIdRegistered(it)) })
                    }
                )
                AnimatedVisibility(
                    visible = state.currentlyScanning == Pos,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally(),
                    content = {
                        ScanPostForm(onResult = { onEvent(QrScreenEvent.OnPosIdRegistered(it)) })
                    }
                )
                AnimatedVisibility(visible = state.currentlyScanning == None) {
                    val listRatio = listOf(0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0)
                    var isExpanded by remember {
                        mutableStateOf(false)
                    }
                    var ratio by remember {
                        mutableDoubleStateOf(0.0)
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        LargeTopAppBar(title = { Text(text = "Isi data dibawah") })
                        OutlinedButton(
                            onClick = { isExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            content = {
                                Text(
                                    text = if (ratio == 0.0) "Masukkan persentase rasio pengamatan"
                                    else "Presentase rasio pengamatan: $ratio",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        )

                        AnimatedVisibility(visible = isExpanded) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                listRatio.forEach {
                                    Text(
                                        text = it.toString(),
                                        modifier = Modifier
                                            .padding(vertical = 8.dp, horizontal = 8.dp)
                                            .clickable {
                                                ratio = it
                                            }
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Start,
                                        fontSize = 18.sp,
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.size(16.dp))
                        NormalTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.remarks,
                            onNewValue = {
                                onEvent(QrScreenEvent.OnNewRemarks(it))
                            },
                            hint = "keterangan",
                            leadingIcon = Icons.Default.TextSnippet,
                            onClearText = { onEvent(QrScreenEvent.OnClearRemarks()) }
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

@OptIn(ExperimentalMaterial3Api::class)
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
        TopAppBar(
            title = {
                Text(text = "kembali")
            },
            navigationIcon = {
                IconButton(
                    onClick = { navigateBack(Destination.DashboardScreen.name) },
                    content = {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "kembali"
                        )
                    }
                )
            }
        )
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
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    .size(200.dp),
            )
        }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = 24.dp,
                        horizontal = 24.dp
                    )
                    .fillMaxWidth(),
                text = if (result.isNotBlank()) "Scan QR berhasil!" else "Scan QR Driver!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
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
                            Text(text = "Lanjut scan Truk")
                        }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        TopAppBar(
            title = {
                Text(text = "kembali")
            },
            navigationIcon = {
                IconButton(
                    onClick = { prevForm(Driver) },
                    content = {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "kembali"
                        )
                    }
                )
            }
        )
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
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    .size(200.dp),
            )
        }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = 24.dp,
                        horizontal = 24.dp
                    )
                    .fillMaxWidth(),
                text = if (result.isNotBlank()) "Scan QR berhasil!" else "Scan QR Truk!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
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
                            Text(text = "Lanjut scan Pos")
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
            Box(
                modifier = Modifier
                    .background(Color.Transparent)
                    .border(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    .size(200.dp),
            )
        }

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        vertical = 24.dp,
                        horizontal = 24.dp
                    )
                    .fillMaxWidth(),
                text = if (result.isNotBlank()) "Scan QR berhasil!" else "Scan QR Pos!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
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
                            Text(text = "Lanjut isi data")
                        }
                    )
                }
            )
        }
    }
}