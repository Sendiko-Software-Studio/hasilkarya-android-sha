package com.system.shailendra.heavy_vehicle_fuel.presentation

import com.system.shailendra.core.network.Status
import com.system.shailendra.core.ui.utils.ErrorTextField
import com.system.shailendra.core.ui.utils.FailedRequest
import com.system.shailendra.dashboard.presentation.component.ScanOptions

data class HeavyVehicleFuelQrScreenState(
    val token: String = "",
    val notificationMessage: String = "",
    val isLoading: Boolean = false,
    val isPostSuccessful: Boolean = false,
    val isRequestFailed: FailedRequest = FailedRequest(),
    val connectionStatus: Status = Status.UnAvailable,
    val currentlyScanning: ScanOptions = ScanOptions.HeavyVehicle,
    val heavyVehicleId: String = "",
    val driverId: String = "",
    val userId: String = "",
    val stationId: String = "",
    val volume: Double = 0.0,
    val hourmeter: String = "",
    val hourmeterErrorState: ErrorTextField = ErrorTextField(),
    val remarks: String = "",
)
