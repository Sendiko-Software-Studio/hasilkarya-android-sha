package com.system.hasilkarya.truck_fuel.presentation

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
import com.system.hasilkarya.qr.presentation.QrScanComponent
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TruckFuelQrScreen(
    state: TruckFuelQrScreenState,
    onEvent: (TruckFuelQrScreenEvent) -> Unit,
    connectionStatus: Status,
    onNavigateBack: (Destination) -> Unit,
) {
    LaunchedEffect(
        key1 = state,
        block = {
            if (state.notificationMessage.isNotBlank()) {
                delay(2000)
                onEvent(TruckFuelQrScreenEvent.NotificationClear)
            }

            if (state.isPostSuccessful) {
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
                        onResult = {
                            onEvent(
                                TruckFuelQrScreenEvent.OnTruckIdRegistered(
                                    it,
                                    connectionStatus
                                )
                            )
                        },
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
                        onResult = {
                            onEvent(
                                TruckFuelQrScreenEvent.OnDriverIdRegistered(
                                    it,
                                    connectionStatus
                                )
                            )
                        },
                        navigateBack = { onEvent(TruckFuelQrScreenEvent.OnNavigateForm(ScanOptions.Truck)) },
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
                        onResult = {
                            onEvent(
                                TruckFuelQrScreenEvent.OnStationIdRegistered(
                                    it,
                                    connectionStatus
                                )
                            )
                        },
                        navigateBack = { onEvent(TruckFuelQrScreenEvent.OnNavigateForm(ScanOptions.Driver)) },
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
                        onResult = { onEvent(TruckFuelQrScreenEvent.OnVolumeRegistered(it.toDoubleOrNull())) },
                        navigateBack = { onEvent(TruckFuelQrScreenEvent.OnNavigateForm(ScanOptions.Pos)) },
                        title = "Jumlah BBM",
                        textButton = "Lanjut isi data"
                    )
                }
                AnimatedVisibility(visible = state.currentlyScanning == ScanOptions.None) {
                    FuelInputForm(
                        odometer = state.odometer,
                        odometerErrorState = state.odometerErrorState,
                        remarks = state.remarks,
                        onOdometerChange = { onEvent(TruckFuelQrScreenEvent.OnOdometerChange(it)) },
                        onOdometerClear = { onEvent(TruckFuelQrScreenEvent.OnClearOdometer) },
                        onRemarksChange = { onEvent(TruckFuelQrScreenEvent.OnRemarksChange(it)) },
                        onRemarksClear = { onEvent(TruckFuelQrScreenEvent.OnClearRemarks) },
                        onNavigateBack = { onEvent(TruckFuelQrScreenEvent.OnNavigateForm(it)) },
                        onSubmit = {
                            onEvent(
                                TruckFuelQrScreenEvent.SaveTruckFuelTransaction(
                                    connectionStatus
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}