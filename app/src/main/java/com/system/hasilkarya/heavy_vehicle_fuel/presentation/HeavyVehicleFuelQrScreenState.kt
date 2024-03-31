package com.system.hasilkarya.heavy_vehicle_fuel.presentation

import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.ui.utils.FailedRequest
import com.system.hasilkarya.dashboard.presentation.component.ScanOptions

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
    val remarks: String = "",
)
