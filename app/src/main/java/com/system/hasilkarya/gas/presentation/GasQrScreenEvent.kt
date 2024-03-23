package com.system.hasilkarya.gas.presentation

import com.system.hasilkarya.dashboard.presentation.ScanOptions

sealed class GasQrScreenEvent {

    data class OnTruckIdRegistered(val truckId: String): GasQrScreenEvent()
    data class OnDriverIdRegistered(val driverId: String): GasQrScreenEvent()
    data class OnStationIdRegistered(val stationId: String): GasQrScreenEvent()
    data class OnVolumeRegistered(val volume: Double): GasQrScreenEvent()
    data class OnNavigateForm(val scanOptions: ScanOptions): GasQrScreenEvent()
    data class OnOdometerChange(val odometer: Double): GasQrScreenEvent()
    data object OnClearOdometer: GasQrScreenEvent()
    data class OnRemarksChange(val remarks: String): GasQrScreenEvent()
    data object OnClearRemarks: GasQrScreenEvent()
    data object SaveGasTransaction: GasQrScreenEvent()
}