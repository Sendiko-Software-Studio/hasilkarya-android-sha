package com.system.hasilkarya.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.ui.theme.HasilKaryaTheme
import com.system.hasilkarya.dashboard.presentation.DashboardScreen
import com.system.hasilkarya.dashboard.presentation.DashboardScreenViewModel
import com.system.hasilkarya.gas.presentation.GasQrScreen
import com.system.hasilkarya.gas.presentation.GasQrScreenViewModel
import com.system.hasilkarya.login.presentation.LoginScreen
import com.system.hasilkarya.login.presentation.LoginScreenViewModel
import com.system.hasilkarya.material.presentation.MaterialQrScreen
import com.system.hasilkarya.material.presentation.MaterialQrScreenViewModel
import com.system.hasilkarya.profile.presentation.ProfileScreen
import com.system.hasilkarya.profile.presentation.ProfileScreenViewModel
import com.system.hasilkarya.splash.presentation.SplashScreen
import com.system.hasilkarya.splash.presentation.SplashScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HasilKaryaTheme {
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
                                val viewModel = hiltViewModel<GasQrScreenViewModel>()
                                GasQrScreen(
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
                    }
                )
            }
        }
    }
}