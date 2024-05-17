package com.system.hasilkarya.station.presentation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.qr.presentation.QrScanComponent
import com.system.hasilkarya.truck_fuel.presentation.TruckFuelQrScreenEvent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StationQrScreen(
    modifier: Modifier = Modifier,
    state: StationQrState,
    connectionStatus: Status,
    onNavigateBack: (Destination) -> Unit,
) {

    Scaffold {
        QrScanComponent(
            onResult = {

            },
            navigateBack = { onNavigateBack(Destination.DashboardScreen) },
            title = "Pos",
            isValid = state.stationId.isNotBlank()
        )
    }

}