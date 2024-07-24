package com.system.shailendra.station.presentation

import com.system.shailendra.core.network.Status

sealed class StationQrScreenEvent {
    data class OnQrCodeScanned(val qrCode: String, val connectionStatus: Status) : StationQrScreenEvent()
}