package com.system.hasilkarya.qr.presentation

import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.ui.utils.FailedRequest
import com.system.hasilkarya.dashboard.presentation.ScanOptions

data class QrScreenState(
    val currentlyScanning: ScanOptions = ScanOptions.Truck,
    val driverId: String = "",
    val truckId: String = "",
    val posId: String = "",
    val observationRatioPercentage: Double = 0.0,
    val remarks: String = "",
    val token: String = "",
    val connectionStatus: Status = Status.UnAvailable,
    val isPostSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val notificationMessage: String = "",
    val isRequestFailed: FailedRequest = FailedRequest(),
    val userId: String = "",
)