package com.system.hasilkarya.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.ui.theme.HasilKaryaTheme
import com.system.hasilkarya.login.presentation.LoginScreen
import com.system.hasilkarya.login.presentation.LoginScreenViewModel
import com.system.hasilkarya.splash.presentation.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HasilKaryaTheme {
                val navController = rememberNavController()
                enableEdgeToEdge()
                NavHost(
                    navController = navController,
                    startDestination = Destination.SplashScreen.name,
                    builder = {
                        composable(
                            route = Destination.SplashScreen.name,
                            content = {
                                SplashScreen(
                                    onNavigate = { route ->
                                        navController.navigate(
                                            route = route
                                        ) {
                                            popUpTo(
                                                route,
                                            ) { inclusive = true }
                                        }
                                    }
                                )
                            }
                        )
                        composable(
                            route = Destination.LoginScreen.name,
                            content = {
                                val viewModel: LoginScreenViewModel = viewModel()
                                LoginScreen(
                                    state = viewModel.state.collectAsState().value,
                                    onEvent = viewModel::onEvent
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}