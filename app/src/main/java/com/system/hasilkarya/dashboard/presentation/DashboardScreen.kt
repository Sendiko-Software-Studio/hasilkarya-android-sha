package com.system.hasilkarya.dashboard.presentation

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.ui.theme.poppinsFont
import com.system.hasilkarya.material.presentation.MaterialCard
import com.system.hasilkarya.material.presentation.MaterialListItem
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(
    state: DashboardScreenState,
    onEvent: (DashboardScreenEvent) -> Unit,
    onNavigate: (String) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    Log.i("DATA_STATE", "DashboardScreen: ${state.name}")
    LaunchedEffect(
        key1 = !cameraPermissionState.hasPermission,
        key2 = state,
        block = {
            cameraPermissionState.launchPermissionRequest()
            if (state.notificationMessage.isNotBlank()) {
                delay(2000)
                onEvent(DashboardScreenEvent.ClearNotificationState)
            }

            if (state.connectionStatus == Status.Available && state.materials.isNotEmpty()) {
                delay(1000)
                onEvent(DashboardScreenEvent.CheckDataAndPost)
            }
        }
    )
    Scaffold(
            topBar = {
                Log.i("NAME_STATE", "DashboardScreen: ${state.name}")
                TopAppBar(
                    title = {
                        Text(
                            text = "Halo, ${state.name}",
                            fontFamily = poppinsFont
                        )
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                            )
                        }
                    }
                )
            },
    ) { paddingValues    ->
        LazyColumn(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            content = {
                item {
                    MaterialCard(
                        onClickAction = {
                            onNavigate(Destination.QrScreen.name)
                        }
                    )
                }
                items(state.materials) {
                    MaterialListItem(materialEntity = it)
                }
            }
        )
    }
}