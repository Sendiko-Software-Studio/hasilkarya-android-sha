package com.system.shailendra.material.presentation

import com.system.shailendra.core.network.Status
import com.system.shailendra.core.ui.utils.ErrorTextField
import com.system.shailendra.core.ui.utils.FailedRequest
import com.system.shailendra.dashboard.presentation.component.ScanOptions

data class MaterialQrScreenState(
    val currentlyScanning: ScanOptions = ScanOptions.Truck,
    val driverId: String = "",
    val truckId: String = "",
    val stationId: String = "",
    val materialVolume: String = "",
    val materialVolumeErrorState: ErrorTextField = ErrorTextField(),
    val remarks: String = "",
    val token: String = "",
    val connectionStatus: Status = Status.UnAvailable,
    val isPostSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val notificationMessage: String = "",
    val isRequestFailed: FailedRequest = FailedRequest(),
    val userId: String = "",
    val stationName: String = "",
    val rapidMode: Boolean = false,
)