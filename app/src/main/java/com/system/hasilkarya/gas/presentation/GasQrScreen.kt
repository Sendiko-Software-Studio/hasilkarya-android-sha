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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.ui.components.ContentBoxWithNotification
import com.system.hasilkarya.dashboard.presentation.ScanOptions
import com.system.hasilkarya.qr.presentation.QrScanComponent
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GasQrScreen(
    state: GasQrScreenState,
    onEvent: (GasQrScreenEvent) -> Unit,
    connectionStatus: Status,
    onNavigateBack: (Destination) -> Unit,
) {
    LaunchedEffect(
        key1 = state,
        block = {
            if (state.notificationMessage.isNotBlank()){
                delay(2000)
                onEvent(GasQrScreenEvent.NotificationClear)
            }

            if (state.isPostSuccessful){
                delay(1000)
                onNavigateBack(Destination.DashboardScreen)
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
                    .padding(horizontal = 8.dp)
            ) {
                AnimatedVisibility(
                    visible = state.currentlyScanning == ScanOptions.Truck,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    QrScanComponent(
                        onResult = { onEvent(GasQrScreenEvent.OnTruckIdRegistered(it)) },
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
                        onResult = { onEvent(GasQrScreenEvent.OnDriverIdRegistered(it)) },
                        navigateBack = { onEvent(GasQrScreenEvent.OnNavigateForm(ScanOptions.Truck)) },
                        title = "Driver",
                        textButton = "Lanjut scan pos"
                    )
                }
                AnimatedVisibility(
                    visible = state.currentlyScanning == ScanOptions.Pos,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    QrScanComponent(
                        onResult = { onEvent(GasQrScreenEvent.OnStationIdRegistered(it)) },
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
                        onResult = { onEvent(GasQrScreenEvent.OnVolumeRegistered(it.toDouble())) },
                        navigateBack = { onEvent(GasQrScreenEvent.OnNavigateForm(ScanOptions.Pos)) },
                        title = "Jumlah BBM",
                        textButton = "Lanjut isi data"
                    )
                }
                AnimatedVisibility(visible = state.currentlyScanning == ScanOptions.None) {
                    GasInputForm(
                        odometer = state.odometer.toString(),
                        remarks = state.remarks,
                        onOdometerChange = { onEvent(GasQrScreenEvent.OnOdometerChange(it)) },
                        onOdometerClear = { onEvent(GasQrScreenEvent.OnClearOdometer) },
                        onRemarksChange = { onEvent(GasQrScreenEvent.OnRemarksChange(it)) },
                        onRemarksClear = { onEvent(GasQrScreenEvent.OnClearRemarks) },
                        onNavigateBack = { onEvent(GasQrScreenEvent.OnNavigateForm(it)) },
                        onSubmit = { onEvent(GasQrScreenEvent.SaveGasTransaction) }
                    )
                }
            }
        }
    }
}