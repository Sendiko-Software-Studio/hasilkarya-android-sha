@file:OptIn(ExperimentalMaterial3Api::class)

package com.system.hasilkarya.material.presentation

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Outbound
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.system.hasilkarya.R
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.ui.components.ContentBoxWithNotification
import com.system.hasilkarya.core.ui.components.NormalTextField
import com.system.hasilkarya.core.ui.theme.poppinsFont
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions.Driver
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions.None
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions.Pos
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions.Truck
import com.system.hasilkarya.qr.presentation.QrScanComponent
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialQrScreen(
    state: MaterialQrScreenState,
    onEvent: (MaterialQrScreenEvent) -> Unit,
    connectionStatus: Status,
    onNavigateBack: (Destination) -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(
        key1 = state,
        block = {
            if (state.isPostSuccessful) {
                delay(1000)
                onNavigateBack(Destination.DashboardScreen)
            }

            if (state.notificationMessage.isNotBlank()) {
                delay(2000)
                onEvent(MaterialQrScreenEvent.OnClearNotification)
            }
        }
    )
    LaunchedEffect(
        key1 = state.truckId,
        key2 = state.driverId,
        key3 = state.posId
    ) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.ding_sfx)

        if (state.truckId.isNotBlank())
            mediaPlayer.start()

        if (state.driverId.isNotBlank())
            mediaPlayer.start()

        if (state.posId.isNotBlank())
            mediaPlayer.start()
    }
    ContentBoxWithNotification(
        message = state.notificationMessage,
        isLoading = state.isLoading,
        isErrorNotification = state.isRequestFailed.isFailed
    ) {
        Scaffold { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 8.dp,
                        end = 8.dp
                    )
            ) {
                AnimatedVisibility(
                    visible = state.currentlyScanning == Truck,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally(),
                    content = {
                        QrScanComponent(
                            onResult = {
                                onEvent(MaterialQrScreenEvent.OnTruckIdRegistered(it, connectionStatus))
                            },
                            navigateBack = {
                                onNavigateBack(Destination.DashboardScreen)
                            },
                            title = "Truck",
                            textButton = "Lanjut scan driver"
                        )
                    }
                )
                AnimatedVisibility(
                    visible = state.currentlyScanning == Driver,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally(),
                    content = {
                        QrScanComponent(
                            onResult = {
                                onEvent(MaterialQrScreenEvent.OnDriverIdRegistered(it, connectionStatus))
                            },
                            navigateBack = {
                                onEvent(MaterialQrScreenEvent.OnNavigateForm(Truck))
                            },
                            title = "Driver",
                            textButton = "Lanjut scan pos"
                        )
                    }
                )
                AnimatedVisibility(
                    visible = state.currentlyScanning == Pos,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally(),
                    content = {
                        QrScanComponent(
                            onResult = {
                                onEvent(MaterialQrScreenEvent.OnPosIdRegistered(it, connectionStatus))
                            },
                            navigateBack = {
                                onEvent(MaterialQrScreenEvent.OnNavigateForm(Truck))
                            },
                            title = "Pos",
                            textButton = "Lanjut isi data"
                        )
                    }
                )
                AnimatedVisibility(visible = state.currentlyScanning == None) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text(text = "Masukkan data", fontFamily = poppinsFont) },
                                navigationIcon = {
                                    IconButton(onClick = { onEvent(MaterialQrScreenEvent.OnNavigateForm(Pos)) }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "kembali"
                                        )
                                    }
                                }
                            )
                        }
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(
                                    text = "Masukkan volume material",
                                    fontFamily = poppinsFont,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                NormalTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = state.materialVolume,
                                    onNewValue = { onEvent(MaterialQrScreenEvent.OnVolumeChange(it)) },
                                    leadingIcon = Icons.AutoMirrored.Default.Outbound,
                                    onClearText = {  },
                                    keyboardType = KeyboardType.Decimal,
                                    hint = "Dalam meter kubik (m³)",
                                    errorState = state.materialVolumeErrorState
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tambah keterangan",
                                    fontFamily = poppinsFont,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                NormalTextField(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(128.dp),
                                    value = state.remarks,
                                    onNewValue = { onEvent(MaterialQrScreenEvent.OnNewRemarks(it)) },
                                    leadingIcon = Icons.AutoMirrored.Filled.TextSnippet,
                                    onClearText = {  },
                                    hint = "Keterangan",
                                    shape = RoundedCornerShape(16.dp),
                                    singleLine = false,
                                )
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        onEvent(MaterialQrScreenEvent.SaveMaterial(connectionStatus))
                                    }
                                ) {
                                    Text(text = "Simpan data", fontFamily = poppinsFont)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}