@file:OptIn(ExperimentalMaterial3Api::class)

package com.system.hasilkarya.material.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Outbound
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
                                    onEvent(MaterialQrScreenEvent.OnNavigateForm(Pos))
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
                            text = "Masukkan volume material",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            fontFamily = poppinsFont
                        )
                        NormalTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.materialVolume,
                            onNewValue = { onEvent(MaterialQrScreenEvent.OnVolumeChange(it)) },
                            leadingIcon = Icons.AutoMirrored.Default.Outbound,
                            onClearText = {  },
                            keyboardType = KeyboardType.Decimal
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        Text(
                            text = "Tambah keterangan",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            fontFamily = poppinsFont
                        )
                        NormalTextField(
                            modifier = Modifier.fillMaxWidth()
                                .height(128.dp),
                            value = state.remarks,
                            onNewValue = { onEvent(MaterialQrScreenEvent.OnNewRemarks(it)) },
                            leadingIcon = Icons.AutoMirrored.Filled.TextSnippet,
                            onClearText = {  },
                            hint = "Keterangan",
                            shape = RoundedCornerShape(16.dp)
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