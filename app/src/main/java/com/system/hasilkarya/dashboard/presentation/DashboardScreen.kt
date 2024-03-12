package com.system.hasilkarya.dashboard.presentation

import android.Manifest
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.system.hasilkarya.dashboard.presentation.ScanOptions.Driver
import com.system.hasilkarya.dashboard.presentation.ScanOptions.Pos
import com.system.hasilkarya.dashboard.presentation.ScanOptions.Truck
import com.system.hasilkarya.material.presentation.MaterialCard
import com.system.hasilkarya.qr.presentation.ScanDriverModal
import com.system.hasilkarya.qr.presentation.ScanPosModal
import com.system.hasilkarya.qr.presentation.ScanTruckModal

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(
    state: DashboardScreenState,
    onEvent: (DashboardScreenEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(
        key1 = !cameraPermissionState.hasPermission,
        block = {
            cameraPermissionState.launchPermissionRequest()
        }
    )

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = "Hi, Sen!", fontWeight = FontWeight.Bold) },
                scrollBehavior = scrollBehavior
            )
        },
    ) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        when(state.currentlyScanning) {
            Driver -> {
                ScanDriverModal(
                    sheetState,
                    onResult = { driverId ->
                        onEvent(DashboardScreenEvent.OnDriverIdRegistered(driverId))
                        onEvent(DashboardScreenEvent.OnScanTruckClick(Truck))
                    }
                )
            }
            Truck -> ScanTruckModal(
                sheetState,
                onResult = { trukId ->
                    onEvent(DashboardScreenEvent.OnTruckIdRegistered(trukId))
                    onEvent(DashboardScreenEvent.OnScanPosClick(Pos)) }
            )
            Pos -> ScanPosModal(
                sheetState,
                onResult = { posId ->
                    onEvent(DashboardScreenEvent.OnPosIdRegistered(posId))
//                    onEvent(DashboardScreenEvent.OnScanTruckClick(Truck))
                }
            )
            else -> null
        }
        LazyColumn(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(
                top = it.calculateTopPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            content = {
                item {
                    Row {
                        MaterialCard(
                            modifier = Modifier.weight(1f),
                            onClickAction = {
                                onEvent(DashboardScreenEvent.OnScanDriverClick(Driver))
                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        )
    }
}