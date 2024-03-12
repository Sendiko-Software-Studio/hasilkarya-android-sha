package com.system.hasilkarya.dashboard.presentation

sealed class DashboardScreenEvent {
    data class OnScanDriverClick(val scanOptions: ScanOptions): DashboardScreenEvent()
    data class OnDriverIdRegistered(val driverId: String): DashboardScreenEvent()
    data class OnScanTruckClick(val scanOptions: ScanOptions): DashboardScreenEvent()
    data class OnTruckIdRegistered(val truckId: String): DashboardScreenEvent()
    data class OnScanPosClick(val scanOptions: ScanOptions): DashboardScreenEvent()
    data class OnPosIdRegistered(val posId: String): DashboardScreenEvent()
    data class OnClear(val scanOptions: ScanOptions): DashboardScreenEvent()
}