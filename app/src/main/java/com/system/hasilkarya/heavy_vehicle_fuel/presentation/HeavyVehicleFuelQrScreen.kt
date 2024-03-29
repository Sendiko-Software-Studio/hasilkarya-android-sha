package com.system.hasilkarya.heavy_vehicle_fuel.presentation

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
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions
import com.system.hasilkarya.heavy_vehicle_fuel.presentation.component.HeavyVehicleFuelInputForm
import com.system.hasilkarya.qr.presentation.QrScanComponent
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HeavyVehicleFuelQrScreen(
    state: HeavyVehicleFuelQrScreenState,
    onEvent: (HeavyVehicleFuelQrScreenEvent) -> Unit,
    onNavigateBack: (Destination) -> Unit,
    connectionStatus: Status
) {
    LaunchedEffect(
        key1 = state.notificationMessage,
        key2 = state.isPostSuccessful,
        block = {
            if (state.notificationMessage.isNotBlank()) {
                delay(2000)
                onEvent(HeavyVehicleFuelQrScreenEvent.NotificationClear)
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
                    visible = state.currentlyScanning == ScanOptions.HeavyVehicle,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    QrScanComponent(
                        onResult = { onEvent(HeavyVehicleFuelQrScreenEvent.OnHeavyVehicleIdRegistered(it)) },
                        navigateBack = { onNavigateBack(Destination.DashboardScreen) },
                        title = "Alat Berat",
                        textButton = "Lanjut scan driver"
                    )
                }
                AnimatedVisibility(
                    visible = state.currentlyScanning == ScanOptions.Driver,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    QrScanComponent(
                        onResult = { onEvent(HeavyVehicleFuelQrScreenEvent.OnDriverIdRegistered(it)) },
                        navigateBack = { onEvent(HeavyVehicleFuelQrScreenEvent.OnNavigateForm(ScanOptions.HeavyVehicle)) },
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
                        onResult = { onEvent(HeavyVehicleFuelQrScreenEvent.OnStationIdRegistered(it)) },
                        navigateBack = { onEvent(HeavyVehicleFuelQrScreenEvent.OnNavigateForm(ScanOptions.Driver)) },
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
                        onResult = { onEvent(HeavyVehicleFuelQrScreenEvent.OnVolumeRegistered(it.toDouble())) },
                        navigateBack = { onEvent(HeavyVehicleFuelQrScreenEvent.OnNavigateForm(ScanOptions.Pos)) },
                        title = "Jumlah BBM",
                        textButton = "Lanjut isi data"
                    )
                }
                AnimatedVisibility(
                    visible = state.currentlyScanning == ScanOptions.None,
                    enter = slideInHorizontally(),
                    exit = slideOutHorizontally()
                ) {
                    HeavyVehicleFuelInputForm(
                        hourmeter = state.hourmeter,
                        remarks = state.remarks,
                        onHourmeterChange = { onEvent(HeavyVehicleFuelQrScreenEvent.OnHourmeterChange(it)) },
                        onHourmeterClear = { onEvent(HeavyVehicleFuelQrScreenEvent.OnClearHourmeter) },
                        onRemarksChange = { onEvent(HeavyVehicleFuelQrScreenEvent.OnRemarksChange(it)) },
                        onRemarksClear = { onEvent(HeavyVehicleFuelQrScreenEvent.OnClearRemarks) },
                        onNavigateBack = { onEvent(HeavyVehicleFuelQrScreenEvent.OnNavigateForm(it)) },
                        onSubmit = {
                            onEvent(HeavyVehicleFuelQrScreenEvent.SaveHeavyVehicleFuelTransaction)
                        }
                    )
                }
            }
        }
    }
}