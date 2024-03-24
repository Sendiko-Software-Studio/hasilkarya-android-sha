package com.system.hasilkarya.gas.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.ui.components.ContentBoxWithNotification
import com.system.hasilkarya.dashboard.presentation.ScanOptions
import com.system.hasilkarya.qr.presentation.QrScanComponent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GasQrScreen(
    state: GasQrScreenState,
    onEvent: (GasQrScreenEvent) -> Unit,
    onNavigateBack: (Destination) -> Unit,
) {
    ContentBoxWithNotification(message = state.notificationMessage) {
        Scaffold { _ ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                AnimatedVisibility(
                    visible = state.currentlyScanning == ScanOptions.Truck,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    QrScanComponent(
                        onResult = {},
                        navigateBack = { onNavigateBack(Destination.DashboardScreen) },
                        title = "Truck",
                        textButton = "Lanjut scan driver"
                    )
                }
                AnimatedVisibility(
                    visible = state.currentlyScanning == ScanOptions.Driver,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    QrScanComponent(
                        onResult = {},
                        navigateBack = { onEvent(GasQrScreenEvent.OnNavigateForm(ScanOptions.Truck)) },
                        title = "Driver",
                        textButton = "Lanjut scan BBM"
                    )
                }
                AnimatedVisibility(
                    visible = state.currentlyScanning == ScanOptions.Pos,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    QrScanComponent(
                        onResult = {},
                        navigateBack = { onEvent(GasQrScreenEvent.OnNavigateForm(ScanOptions.Driver)) },
                        title = "Pos",
                        textButton = "Lanjut scan BBM"
                    )
                }
                AnimatedVisibility(
                    visible = state.currentlyScanning == ScanOptions.Volume,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    QrScanComponent(
                        onResult = {},
                        navigateBack = { onEvent(GasQrScreenEvent.OnNavigateForm(ScanOptions.Pos)) },
                        title = "Jumlah BBM",
                        textButton = "Lanjut isi data"
                    )
                }
            }
        }
    }
}