package com.system.hasilkarya.material.presentation

import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions

sealed class MaterialQrScreenEvent {
    data class OnDriverIdRegistered(val driverId: String, val connectionStatus: Status): MaterialQrScreenEvent()
    data class OnTruckIdRegistered(val truckId: String, val connectionStatus: Status): MaterialQrScreenEvent()
    data class OnPosIdRegistered(val posId: String, val connectionStatus: Status): MaterialQrScreenEvent()
    data class OnVolumeChange(val volume: String): MaterialQrScreenEvent()
    data class OnNewRemarks(val remarks: String): MaterialQrScreenEvent()
    data class OnClearRemarks(val remarks: String = ""): MaterialQrScreenEvent()
    data class OnNavigateForm(val scanOptions: ScanOptions): MaterialQrScreenEvent()
    data object OnClearNotification: MaterialQrScreenEvent()
    data class SaveMaterial(val connectionStatus: Status): MaterialQrScreenEvent()
}