package com.system.shailendra.settings.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.system.shailendra.core.navigation.DashboardScreen
import com.system.shailendra.core.navigation.SplashScreen
import com.system.shailendra.core.ui.components.ContentBoxWithNotification
import com.system.shailendra.core.ui.theme.AppTheme
import com.system.shailendra.core.ui.theme.HasilKaryaTheme
import com.system.shailendra.core.ui.theme.poppinsFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsScreenState,
    onEvent: (SettingsScreenEvent) -> Unit,
    onNavigateBack: (destinations: Any) -> Unit
) {
    LaunchedEffect(
        key1 = state,
        block = {
            if (state.isPostSuccessful) {
                onNavigateBack(SplashScreen)
            }
        }
    )
    ContentBoxWithNotification(
        message = state.notificationMessage,
        isLoading = state.isLoading,
        isErrorNotification = state.isRequestFailed.isFailed,
        content = {
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            Scaffold(
                topBar = {
                    LargeTopAppBar(
                        title = { Text(text = "Pengaturan", fontFamily = poppinsFont) },
                        navigationIcon = {
                            IconButton(
                                onClick = { onNavigateBack(DashboardScreen) },
                                content = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "kembali"
                                    )
                                }
                            )
                        },
                        scrollBehavior = scrollBehavior,
                    )
                }
            ) {
                LazyColumn(
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .fillMaxSize(),
                    contentPadding = it,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    content = {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Akun saya",
                                fontFamily = poppinsFont,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "akun",
                                    modifier = Modifier.size(48.dp)
                                )
                                Column(modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp)) {
                                    Text(
                                        text = state.name,
                                        fontFamily = poppinsFont,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = state.email,
                                        fontFamily = poppinsFont,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        item {
                            Text(
                                text = "Preferensi",
                                fontFamily = poppinsFont,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Mode Cepat",
                                        fontFamily = poppinsFont,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = "Ketika aktif, pengguna akan langsung diarahkan untuk Scan QR tanpa melalui Dashboard.",
                                        fontFamily = poppinsFont,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                VerticalDivider(modifier = Modifier.height(32.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Switch(
                                    checked = state.rapidMode,
                                    onCheckedChange = {
                                        onEvent(SettingsScreenEvent.OnRapidModeChanged(!state.rapidMode))
                                    },
                                    thumbContent = {
                                        if (state.rapidMode) {
                                            Icon(
                                                imageVector = Icons.Default.Bolt,
                                                contentDescription = "Mode cepat"
                                            )
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Tema Aplikasi",
                                        fontFamily = poppinsFont,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = "Menyesuaikan tema aplikasi dengan sistem, atau menentukan sendiri tema Gelap/Terang.",
                                        fontFamily = poppinsFont,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 8.dp)
                                    )
                                }
                                VerticalDivider(modifier = Modifier
                                    .height(32.dp)
                                    .padding(end = 8.dp))
                                AnimatedVisibility(
                                    visible = !state.showingThemeOptions,
                                    enter = fadeIn(),
                                    exit = fadeOut(),
                                ) {
                                    AssistChip(
                                        onClick = {
                                            onEvent(
                                                SettingsScreenEvent.OnShowThemeOptionsChanged(
                                                    !state.showingThemeOptions
                                                )
                                            )
                                        },
                                        label = { Text(text = state.theme.name) }
                                    )
                                }
                            }
                        }
                        item {
                            AnimatedVisibility(
                                visible = state.showingThemeOptions,
                                enter = expandVertically(),
                                exit = shrinkVertically()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    AppTheme.entries.forEach { theme ->
                                        InputChip(
                                            selected = state.theme == theme,
                                            onClick = {
                                                onEvent(SettingsScreenEvent.OnShowThemeOptionsChanged(!state.showingThemeOptions))
                                                onEvent(SettingsScreenEvent.OnThemeChanged(theme))
                                            },
                                            label = {
                                                Text(
                                                    text = theme.name,
                                                    fontFamily = poppinsFont,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            TextButton(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                colors = ButtonDefaults.textButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                ),
                                onClick = { onEvent(SettingsScreenEvent.OnLogout) },
                                content = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Logout", fontFamily = poppinsFont)
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Logout,
                                            contentDescription = "Logout"
                                        )
                                    }
                                }
                            )
                        }
                        item {
                            val uriHandler = LocalUriHandler.current
                            Text(
                                text = "version: 1.4.1",
                                fontFamily = poppinsFont,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .clickable { uriHandler.openUri("https://github.com/Sendiko-Software-Studio") },
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                )
            }
        }
    )

}

@Preview
@Composable
private fun SettingsScreenPrev() {
    HasilKaryaTheme(
        darkTheme = true
    ) {
        SettingsScreen(
            state = SettingsScreenState(
                name = "Rizky Sendiko",
                email = "fuel_operator1@hasikarya.co.id",
                rapidMode = true
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}