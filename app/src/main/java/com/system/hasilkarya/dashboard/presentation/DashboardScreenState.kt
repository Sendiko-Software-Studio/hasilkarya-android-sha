package com.system.hasilkarya.dashboard.presentation

import com.system.hasilkarya.dashboard.presentation.ScanOptions.None

data class DashboardScreenState(
    val name: String = "",
    val currentlyScanning: ScanOptions = None,
    val driverId: String = "",
    val truckId: String = "",
    val posId: String = "",
)
