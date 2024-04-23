package com.system.hasilkarya.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.preferences.ThemeViewModel
import com.system.hasilkarya.core.ui.theme.AppTheme.Dark
import com.system.hasilkarya.core.ui.theme.AppTheme.Default
import com.system.hasilkarya.core.ui.theme.AppTheme.Light
import com.system.hasilkarya.core.ui.theme.HasilKaryaTheme
import com.system.hasilkarya.dashboard.presentation.DashboardScreen
import com.system.hasilkarya.dashboard.presentation.DashboardScreenViewModel
import com.system.hasilkarya.heavy_vehicle_fuel.presentation.HeavyVehicleFuelQrScreen
import com.system.hasilkarya.heavy_vehicle_fuel.presentation.HeavyVehicleFuelQrScreenViewModel
import com.system.hasilkarya.login.presentation.LoginScreen
import com.system.hasilkarya.login.presentation.LoginScreenViewModel
import com.system.hasilkarya.material.presentation.MaterialQrScreen
import com.system.hasilkarya.material.presentation.MaterialQrScreenViewModel
import com.system.hasilkarya.profile.presentation.ProfileScreen
import com.system.hasilkarya.profile.presentation.ProfileScreenViewModel
import com.system.hasilkarya.splash.presentation.SplashScreen
import com.system.hasilkarya.splash.presentation.SplashScreenViewModel
import com.system.hasilkarya.truck_fuel.presentation.TruckFuelQrScreen
import com.system.hasilkarya.truck_fuel.presentation.TruckFuelQrScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel = hiltViewModel<ThemeViewModel>()
            HasilKaryaTheme(
                darkTheme = when (themeViewModel.state.collectAsState().value.theme){
                    Default -> isSystemInDarkTheme()
                    Dark -> true
                    Light -> false
                }
            ) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Destination.SplashScreen.name,
                    builder = {
                        composable(
                            route = Destination.SplashScreen.name,
                            content = {
                                val viewModel = hiltViewModel<SplashScreenViewModel>()
                                SplashScreen(
                                    state = viewModel.state.collectAsState().value,
                                    onNavigate = { route ->
                                        navController.navigate(
                                            route = route.name
                                        ) {
                                            popUpTo(
                                                route.name,
                                            ) { inclusive = true }
                                        }
                                    }
                                )
                            }
                        )
                        composable(
                            route = Destination.LoginScreen.name,
                            content = {
                                val viewModel = hiltViewModel<LoginScreenViewModel>()
                                LoginScreen(
                                    state = viewModel.state.collectAsState().value,
                                    onEvent = viewModel::onEvent,
                                    onNavigate = { route ->
                                        navController.navigate(
                                            route = route.name
                                        ) {
                                            popUpTo(
                                                route.name,
                                            ) { inclusive = true }
                                        }
                                    }
                                )
                            }
                        )
                        composable(
                            route = Destination.DashboardScreen.name,
                            content = {
                                val viewModel = hiltViewModel<DashboardScreenViewModel>()
                                val connectionStatus = viewModel.connectionStatus.collectAsState().value.connectionStatus
                                DashboardScreen(
                                    state = viewModel.state.collectAsState().value,
                                    onEvent = viewModel::onEvent,
                                    connectionStatus = connectionStatus,
                                    onNavigate = { route ->
                                        navController.navigate(route = route.name)
                                    }
                                )
                            }
                        )
                        composable(
                            route = Destination.MaterialQrScreen.name,
                            content = {
                                val viewModel = hiltViewModel<MaterialQrScreenViewModel>()
                                MaterialQrScreen(
                                    state = viewModel.state.collectAsState().value,
                                    onEvent = viewModel::onEvent,
                                    connectionStatus = viewModel.connectionStatus.collectAsState().value.connectionStatus,
                                    onNavigateBack = { route ->
                                        navController.navigate(
                                            route = route.name
                                        ) {
                                            popUpTo(
                                                route.name,
                                            ) { inclusive = true }
                                        }
                                    }
                                )
                            }
                        )
                        composable(
                            route = Destination.ProfileScreen.name,
                            content = {
                                val viewModel = hiltViewModel<ProfileScreenViewModel>()
                                ProfileScreen(
                                    state = viewModel.state.collectAsState().value,
                                    onEvent = viewModel::onEvent,
                                    onNavigateBack = {
                                        navController.navigate(it.name){
                                            popUpTo(it.name) { inclusive = true }
                                        }
                                    }
                                )
                            }
                        )
                        composable(
                            route = Destination.GasQrScreen.name,
                            content = {
                                val viewModel = hiltViewModel<TruckFuelQrScreenViewModel>()
                                TruckFuelQrScreen(
                                    state = viewModel.state.collectAsState().value,
                                    onEvent = viewModel::onEvent,
                                    connectionStatus = viewModel.connectionStatus.collectAsState().value.connectionStatus,
                                    onNavigateBack = {
                                        navController.navigate(it.name){
                                            popUpTo(it.name) { inclusive = true }
                                        }
                                    }
                                )
                            }
                        )
                        composable(
                            route = Destination.GasHVQrScreen.name,
                            content = {
                                val viewModel = hiltViewModel<HeavyVehicleFuelQrScreenViewModel>()
                                HeavyVehicleFuelQrScreen(
                                    state = viewModel.state.collectAsState().value,
                                    onEvent = viewModel::onEvent,
                                    onNavigateBack = {
                                        navController.navigate(it.name){
                                            popUpTo(it.name) { inclusive = true }
                                        }
                                    },
                                    connectionStatus = viewModel.connectionStatus.collectAsState().value.connectionStatus
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}