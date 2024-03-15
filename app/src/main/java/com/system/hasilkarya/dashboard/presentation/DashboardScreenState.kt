package com.system.hasilkarya.dashboard.presentation

import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.ui.utils.FailedRequest
import com.system.hasilkarya.dashboard.data.MaterialEntity

data class DashboardScreenState(
    val name: String = "",
    val token: String = "",
    val connectionStatus: Status = Status.UnAvailable,
    val isPostSuccessful: Boolean = false,
    val isLoading: Boolean = false,
    val notificationMessage: String = "",
    val isRequestFailed: FailedRequest = FailedRequest(),
    val materials: List<MaterialEntity> = emptyList(),
    val showingForm: Boolean = false,
)
