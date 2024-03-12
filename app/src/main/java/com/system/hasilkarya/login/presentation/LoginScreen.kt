package com.system.hasilkarya.login.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.ui.components.NormalTextField
import com.system.hasilkarya.core.ui.components.PasswordTextField

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    state: LoginScreenState,
    onEvent: (LoginScreenEvent) -> Unit,
    onNavigate: (String) -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Login", fontSize = 24.sp, modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "Silahkan login menggunakan email dan password.", fontSize = 16.sp, modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.size(8.dp))
            NormalTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.emailText,
                onNewValue = {
                             onEvent(LoginScreenEvent.OnEmailChange(it))
                },
                leadingIcon = Icons.Default.Email,
                onClearText = {
                    onEvent(LoginScreenEvent.OnEmailClear)
                }
            )
            PasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.passwordText,
                onNewValue = {
                             onEvent(LoginScreenEvent.OnPasswordChange(it))
                },
                leadingIcon = Icons.Default.Lock,
                onVisibiltyToggle = {
                    Log.i("PASSWORD_VISIBILIY", "LoginScreen: $it")
                    onEvent(LoginScreenEvent.OnPasswordVisibilityChange(it))
                },
                isVisible = state.isPasswordVisible
            )
            Spacer(modifier = Modifier.size(4.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNavigate(Destination.DashboardScreen.name) },
                content = {
                    Text(text = "Login")
                }
            )
        }
    }
}