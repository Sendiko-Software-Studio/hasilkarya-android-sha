package com.system.hasilkarya.station.presentation

import com.system.hasilkarya.core.network.Status

sealed class StationQrScreenEvent {
    data class OnQrCodeScanned(val qrCode: String, val connectionStatus: Status) : StationQrScreenEvent()
}