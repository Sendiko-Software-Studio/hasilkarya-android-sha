package com.system.hasilkarya.splash.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.system.hasilkarya.R
import com.system.hasilkarya.core.navigation.DashboardScreen
import com.system.hasilkarya.core.navigation.LoginScreen
import com.system.hasilkarya.core.ui.theme.HasilKaryaTheme
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreen(
    state: SplashScreenState,
    onNavigate: (destination: Any) -> Unit
) {
    LaunchedEffect(
        key1 = state.token,
        block = {
            delay(1000)
            if (state.token.isNotBlank())
                onNavigate(DashboardScreen)
            else onNavigate(LoginScreen)
        }
    )
    Scaffold {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background
                )
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter,
                content = {
                    Image(
                        painter = painterResource(id = R.drawable.new_s3),
                        contentDescription = "S3",
                        modifier = Modifier.size(64.dp)
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPrev() {
    HasilKaryaTheme(darkTheme = true) {
        SplashScreen(
            state = SplashScreenState(),
            onNavigate = {

            }
        )
    }
}