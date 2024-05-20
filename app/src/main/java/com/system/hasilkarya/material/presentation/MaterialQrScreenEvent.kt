package com.system.hasilkarya.material.presentation

import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions

sealed class MaterialQrScreenEvent {
    data class OnTruckIdRegistered(val truckId: String): MaterialQrScreenEvent()
    data class OnVolumeChange(val volume: String): MaterialQrScreenEvent()
    data class OnNewRemarks(val remarks: String): MaterialQrScreenEvent()
    data class OnClearRemarks(val remarks: String = ""): MaterialQrScreenEvent()
    data class OnNavigateForm(val scanOptions: ScanOptions): MaterialQrScreenEvent()
    data object OnClearNotification: MaterialQrScreenEvent()
    data object SaveMaterial: MaterialQrScreenEvent()
}