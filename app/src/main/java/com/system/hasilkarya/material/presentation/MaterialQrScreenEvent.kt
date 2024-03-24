package com.system.hasilkarya.material.presentation

import com.system.hasilkarya.dashboard.presentation.ScanOptions

sealed class MaterialQrScreenEvent {
    data class OnDriverIdRegistered(val driverId: String): MaterialQrScreenEvent()
    data class OnTruckIdRegistered(val truckId: String): MaterialQrScreenEvent()
    data class OnPosIdRegistered(val posId: String): MaterialQrScreenEvent()
    data class OnSelectedRatio(val ratio: Double): MaterialQrScreenEvent()
    data class OnNewRemarks(val remarks: String): MaterialQrScreenEvent()
    data class OnClearRemarks(val remarks: String = ""): MaterialQrScreenEvent()
    data class OnNavigateForm(val scanOptions: ScanOptions): MaterialQrScreenEvent()
    data object OnClearNotification: MaterialQrScreenEvent()
    data object SaveMaterial: MaterialQrScreenEvent()
}