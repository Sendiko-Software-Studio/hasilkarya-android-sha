package com.system.shailendra.dashboard.presentation

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.system.shailendra.R
import com.system.shailendra.core.navigation.GasHeavyVehicleScreen
import com.system.shailendra.core.navigation.GasTruckScreen
import com.system.shailendra.core.navigation.MaterialScreen
import com.system.shailendra.core.navigation.ProfileScreen
import com.system.shailendra.core.navigation.StationScreen
import com.system.shailendra.core.network.Status
import com.system.shailendra.core.ui.theme.poppinsFont
import com.system.shailendra.dashboard.presentation.component.MenuCard
import com.system.shailendra.station.presentation.component.StationLocation
import com.system.shailendra.dashboard.presentation.component.InfoItemCard
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(
    state: DashboardScreenState,
    connectionStatus: Status,
    onEvent: (DashboardScreenEvent) -> Unit,
    onNavigate: (destination: Any) -> Unit,
) {
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
            CenterAlignedTopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.simonro),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { onNavigate(ProfileScreen) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            AnimatedVisibility(visible = state.isConnecting || state.isUploading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    item {
                        val time = LocalDateTime.now().hour
                        val greeting = when (time) {
                            in 5..11 -> {
                                "Pagi"
                            }
                            in 12..15 -> {
                                "Siang"
                            }
                            in 15..17 -> {
                                "Sore"
                            }
                            else -> {
                                "Malam"
                            }
                        }
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "Selamat $greeting,",
                                fontFamily = poppinsFont,
                                fontSize = 18.sp
                            )
                            Text(
                                text = state.name,
                                fontFamily = poppinsFont,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (state.role == "checker") {
                        item {
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
                                    onNavigate(StationScreen)
                                },
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                    if (state.role == "checker" || state.role == "admin") {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                MenuCard(
                                    text = "Scan Material Movement",
                                    icon = painterResource(id = R.drawable.scan_material_movement),
                                    onClickAction = {
                                        onNavigate(MaterialScreen)
                                    },
                                    enabled = !noStation,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    if (state.role == "gas-operator" || state.role == "admin") {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                MenuCard(
                                    text = "Scan Transaksi BBM Truk",
                                    icon = painterResource(id = R.drawable.scan_truck),
                                    onClickAction = {
                                        onNavigate(GasTruckScreen)
                                    },
                                    enabled = true,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                MenuCard(
                                    text = "Scan Transaksi BBM Alat Berat",
                                    icon = painterResource(id = R.drawable.scan_exca),
                                    onClickAction = {
                                        onNavigate(GasHeavyVehicleScreen)
                                    },
                                    enabled = true,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    if (noStation && state.role == "checker") {
                        item {
                            InfoItemCard(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                info = "Mohon pilih Pos terlebih dulu.",
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "info"
                                    )
                                }
                            )
                        }
                    }
                    item {
                        AnimatedVisibility(
                            visible = state.totalData > 0,
                            enter = expandHorizontally(),
                            exit = shrinkHorizontally()
                        ) {
                            InfoItemCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(),
                                info = "Sedang mengupload data..",
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "info"
                                    )
                                }
                            )
                        }
                    }
                    item {
                        AnimatedVisibility(
                            visible = state.isTokenExpired,
                            enter = expandHorizontally(),
                            exit = shrinkHorizontally()
                        ) {
                            InfoItemCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                info = "Maaf, aplikasi tidak bisa terhubung ke server.",
                                icon = {
                                    TextButton(
                                        onClick = { onEvent(DashboardScreenEvent.RetryLogin) },
                                        content = {
                                            Text(
                                                text = "Coba lagi",
                                                color = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                        }
                                    )
                                },
                                isError = true
                            )
                        }
                    }
                }
            )
        }
    }
}