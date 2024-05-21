package com.system.hasilkarya.dashboard.presentation

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.system.hasilkarya.R
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.ui.theme.poppinsFont
import com.system.hasilkarya.dashboard.presentation.component.MenuCard
import com.system.hasilkarya.station.presentation.component.StationLocation
import com.system.hasilkarya.dashboard.presentation.component.UnsentItemCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(
    state: DashboardScreenState,
    connectionStatus: Status,
    onEvent: (DashboardScreenEvent) -> Unit,
    onNavigate: (Destination) -> Unit,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val noStation = state.activeStation == null

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    LaunchedEffect(
        key1 = cameraPermissionState.hasPermission,
        block = {
            if (!cameraPermissionState.hasPermission)
                cameraPermissionState.launchPermissionRequest()
        }
    )
    LaunchedEffect(
        key1 = state.totalData,
        key2 = connectionStatus,
        block = {
            if (state.totalData != 0 && connectionStatus == Status.Available)
                onEvent(DashboardScreenEvent.CheckDataAndPost)
        }
    )
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Selamat datang, ${state.name}",
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
                ),
                scrollBehavior = scrollBehavior
            )
        },
    ) { paddingValues ->
        Column {
            StationLocation(
                stationName = if (noStation)
                    "Tidak ada."
                else {
                    if (state.activeStation!!.name == "Station berhasil disimpan.") {
                        "Pos baru disimpan."
                    } else {
                        "${state.activeStation.name}, ${state.activeStation.province}."
                    }
                },
                onButtonClick = {
                    onNavigate(Destination.StationQrScreen)
                },
                modifier = Modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                content = {
                    if (state.role == "checker" || state.role == "admin") {
                        item {
                            MenuCard(
                                text = "Scan Material Movement",
                                icon = painterResource(id = R.drawable.scan_material_movement),
                                onClickAction = {
                                    onNavigate(Destination.MaterialQrScreen)
                                },
                                enabled = !noStation
                            )
                        }
                    }
                    if (state.role == "gas-operator" || state.role == "admin") {
                        item {
                            MenuCard(
                                text = "Scan Transaksi BBM Truk",
                                icon = painterResource(id = R.drawable.scan_truck),
                                onClickAction = {
                                    onNavigate(Destination.GasQrScreen)
                                },
                            )
                        }
                    }
                    if (state.role == "gas-operator" || state.role == "admin") {
                        item {
                            MenuCard(
                                text = "Scan Transaksi BBM Alat Berat",
                                icon = painterResource(id = R.drawable.scan_exca),
                                onClickAction = {
                                    onNavigate(Destination.GasHVQrScreen)
                                },
                            )
                        }
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(
                visible = state.totalData != 0,
                enter = expandHorizontally(),
                exit = shrinkHorizontally()
            ) {
                UnsentItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    itemCount = state.totalData
                )
            }
            if (noStation) {
                Snackbar(
                    content = {
                        Text(text = "Mohon pilih Pos terlebih dulu.")
                    },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}