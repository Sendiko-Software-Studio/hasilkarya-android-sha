package com.system.hasilkarya.dashboard.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardScreenViewModel: ViewModel() {

    private val _state = MutableStateFlow(DashboardScreenState())
    val state = _state.asStateFlow()

    fun onEvent(event: DashboardScreenEvent) {
        when(event) {
            is DashboardScreenEvent.OnClear -> _state.update {
                it.copy(currentlyScanning = ScanOptions.None)
            }
            is DashboardScreenEvent.OnScanDriverClick -> _state.update {
                it.copy(currentlyScanning = ScanOptions.Driver)
            }
            is DashboardScreenEvent.OnDriverIdRegistered -> _state.update {
                it.copy(driverId = event.driverId)
            }
            is DashboardScreenEvent.OnScanTruckClick -> _state.update {
                it.copy(currentlyScanning = ScanOptions.Truck)
            }
            is DashboardScreenEvent.OnTruckIdRegistered -> _state.update {
                it.copy(truckId = event.truckId)
            }
            is DashboardScreenEvent.OnScanPosClick -> _state.update {
                it.copy(currentlyScanning = ScanOptions.Pos)
            }
            is DashboardScreenEvent.OnPosIdRegistered -> _state.update {
                it.copy(posId = event.posId)
            }
        }
    }
}