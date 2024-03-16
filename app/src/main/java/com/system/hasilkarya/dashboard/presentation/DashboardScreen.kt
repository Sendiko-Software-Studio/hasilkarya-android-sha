package com.system.hasilkarya.dashboard.presentation

import android.Manifest
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.network.Status
import com.system.hasilkarya.core.ui.components.ContentBoxWithNotification
import com.system.hasilkarya.material.presentation.MaterialCard
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
                onEvent(DashboardScreenEvent.CheckDataAndPost)
            }
        }
    )

    ContentBoxWithNotification(
        message = state.notificationMessage,
        isLoading = state.isLoading,
        isErrorNotification = state.isRequestFailed.isFailed
    ) {
        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = { Text(text = "Halo, ${state.name}", fontWeight = FontWeight.Bold) }
                )
            },
        ) {
            LazyColumn(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = PaddingValues(
                    top = it.calculateTopPadding() + 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                content = {
                    item {
                        MaterialCard(
                            modifier = Modifier.fillMaxWidth(),
                            onClickAction = {
                                onNavigate(Destination.QrScreen.name)
                            }
                        )
                    }
                }
            )
        }
    }
}