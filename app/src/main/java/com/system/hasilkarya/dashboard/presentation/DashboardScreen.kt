package com.system.hasilkarya.dashboard.presentation

import android.Manifest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.system.hasilkarya.core.ui.theme.poppinsFont
import com.system.hasilkarya.material.presentation.MaterialCard
import com.system.hasilkarya.material.presentation.MaterialListItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(
    state: DashboardScreenState,
    connectionStatus: Status,
    onEvent: (DashboardScreenEvent) -> Unit,
    onNavigate: (String) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    LaunchedEffect(
        key1 = cameraPermissionState.hasPermission,
        key2 = state,
        key3 = connectionStatus,
        block = {

            if (!cameraPermissionState.hasPermission)
                cameraPermissionState.launchPermissionRequest()

            if (connectionStatus == Status.Available && state.materials.isNotEmpty()){
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
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                            )
                        }
                    }
                )
            },
    ) { paddingValues  ->
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
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    AnimatedVisibility(visible = state.materials.isNotEmpty()) {
                        Text(text = "Data yang belum terupload", fontFamily = poppinsFont, fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(state.materials) {
                    MaterialListItem(materialEntity = it)
                }
            }
        )
    }
}