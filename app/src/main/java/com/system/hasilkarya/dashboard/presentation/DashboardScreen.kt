package com.system.hasilkarya.dashboard.presentation

import android.Manifest
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.system.hasilkarya.R
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.ui.theme.poppinsFont
import com.system.hasilkarya.fuel.presentation.component.FuelTruckCard
import com.system.hasilkarya.material.presentation.component.MaterialListItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(
    state: DashboardScreenState,
    connectionStatus: Status,
    onEvent: (DashboardScreenEvent) -> Unit,
    onNavigate: (Destination) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    LaunchedEffect(
        key1 = cameraPermissionState.hasPermission,
        key2 = state.materials,
        key3 = connectionStatus,
        block = {
            Log.i("FUELS", "DashboardScreen: ${state.fuels}")

            if (!cameraPermissionState.hasPermission)
                cameraPermissionState.launchPermissionRequest()

            if (connectionStatus == Status.Available && state.materials.isNotEmpty()) {
                onEvent(DashboardScreenEvent.CheckDataAndPost)
            }

            if (connectionStatus == Status.Available && state.fuels.isNotEmpty()){
                onEvent(DashboardScreenEvent.CheckDataAndPost)
            }
        }
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Halo, ${state.name}",
                        fontFamily = poppinsFont
                    )
                },
                actions = {
                    AnimatedVisibility(
                        visible = state.isLoading,
                        enter = expandHorizontally() + expandVertically(),
                        exit = shrinkHorizontally() + shrinkVertically()
                    ) {
                        Icon(imageVector = Icons.Default.Sync, contentDescription = "sinkronisasi")
                    }
                    IconButton(onClick = { onNavigate(Destination.ProfileScreen) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                item {
                    AnimatedVisibility(
                        visible = state.role == "checker",
                        enter = expandHorizontally(),
                        exit = shrinkHorizontally()
                    ) {
                        Row {
                            MenuCard(
                                text = "Scan Material Movement",
                                icon = painterResource(id = R.drawable.scan_material_movement),
                                onClickAction = {
                                    onNavigate(Destination.MaterialQrScreen)
                                },
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = state.role == "gas-operator",
                        enter = expandHorizontally(),
                        exit = shrinkHorizontally()
                    ) {
                        MenuCardExpendable(
                            text = "Scan Transaksi BBM",
                            icon = painterResource(id = R.drawable.scan_gas),
                            onClickAction = {
                                onNavigate(Destination.GasQrScreen)
                            }
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedVisibility(
                        visible = state.materials.isNotEmpty() || state.fuels.isNotEmpty(),
                        enter = expandHorizontally(),
                        exit = shrinkHorizontally()
                    ) {
                        Text(
                            text = "Data yang belum terupload",
                            fontFamily = poppinsFont,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                items(state.materials) {
                    MaterialListItem(materialEntity = it)
                }
                items(state.fuels) {
                    FuelTruckCard(fuelTruckEntity = it)
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        )
    }
}