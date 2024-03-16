package com.system.hasilkarya.qr.presentation

import com.system.hasilkarya.dashboard.presentation.ScanOptions

sealed class QrScreenEvent {
    data class OnDriverIdRegistered(val driverId: String): QrScreenEvent()
    data class OnTruckIdRegistered(val truckId: String): QrScreenEvent()
    data class OnPosIdRegistered(val posId: String): QrScreenEvent()
    data class OnSelectedRatio(val ratio: Double): QrScreenEvent()
    data class OnNewRemarks(val remarks: String): QrScreenEvent()
    data class OnClearRemarks(val remarks: String = ""): QrScreenEvent()
    data class OnNavigateForm(val scanOptions: ScanOptions): QrScreenEvent()
    data object OnClearNotification: QrScreenEvent()
    data object SaveMaterial: QrScreenEvent()
}