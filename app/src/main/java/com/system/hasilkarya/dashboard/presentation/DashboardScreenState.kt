package com.system.hasilkarya.dashboard.presentation

import com.system.hasilkarya.core.entities.FuelHeavyVehicleEntity
import com.system.hasilkarya.core.entities.FuelTruckEntity
import com.system.hasilkarya.core.entities.MaterialEntity
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.ui.utils.FailedRequest

data class DashboardScreenState(
    val userId: String = "",
    val name: String = "",
    val token: String = "",
    val connectionStatus: Status = Status.UnAvailable,
    val isPostSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val isRequestFailed: FailedRequest = FailedRequest(),
    val materials: List<MaterialEntity> = emptyList(),
    val role: String = "",
    val fuels: List<FuelTruckEntity> = emptyList(),
    val heavyFuels: List<FuelHeavyVehicleEntity> = emptyList(),
)
