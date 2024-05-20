package com.system.hasilkarya.station.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.ui.components.ContentBoxWithNotification
import com.system.hasilkarya.qr.presentation.QrScanComponent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StationQrScreen(
    modifier: Modifier = Modifier,
    state: StationQrScreenState,
    connectionStatus: Status,
    onNavigateBack: () -> Unit,
    onEvent: (StationQrScreenEvent) -> Unit,
) {

    LaunchedEffect(state.isRequestSuccess) {
        if (state.isRequestSuccess) {
            onNavigateBack()
        }
    }
    ContentBoxWithNotification(
        modifier = modifier.fillMaxSize(),
        message = state.notificationMessage,
        isLoading = state.isLoading,
        isErrorNotification = state.isRequestFailed,
        content = {
            Scaffold {
                QrScanComponent(
                    onResult = {
                        onEvent(StationQrScreenEvent.OnQrCodeScanned(it, connectionStatus))
                    },
                    navigateBack = { onNavigateBack() },
                    title = "Pos",
                    isValid = state.stationId.isNotBlank()
                )
            }
        }
    )

}