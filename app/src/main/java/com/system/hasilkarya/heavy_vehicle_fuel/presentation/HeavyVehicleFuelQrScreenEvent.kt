package com.system.hasilkarya.heavy_vehicle_fuel.presentation

import com.system.hasilkarya.dashboard.presentation.component.ScanOptions

sealed class HeavyVehicleFuelQrScreenEvent {
    data class OnHeavyVehicleIdRegistered(val vHId: String): HeavyVehicleFuelQrScreenEvent()
    data class OnDriverIdRegistered(val driverId: String): HeavyVehicleFuelQrScreenEvent()
    data class OnStationIdRegistered(val stationId: String): HeavyVehicleFuelQrScreenEvent()
    data class OnVolumeRegistered(val volume: Double): HeavyVehicleFuelQrScreenEvent()
    data class OnNavigateForm(val scanOptions: ScanOptions): HeavyVehicleFuelQrScreenEvent()
    data class OnHourmeterChange(val odometer: String): HeavyVehicleFuelQrScreenEvent()
    data object OnClearHourmeter: HeavyVehicleFuelQrScreenEvent()
    data class OnRemarksChange(val remarks: String): HeavyVehicleFuelQrScreenEvent()
    data object OnClearRemarks: HeavyVehicleFuelQrScreenEvent()
    data object SaveHeavyVehicleFuelTransaction: HeavyVehicleFuelQrScreenEvent()
    data object NotificationClear: HeavyVehicleFuelQrScreenEvent()

}
