package com.system.shailendra.login.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.system.shailendra.R
import com.system.shailendra.core.navigation.DashboardScreen
import com.system.shailendra.core.ui.components.ContentBoxWithNotification
import com.system.shailendra.core.ui.components.NormalTextField
import com.system.shailendra.core.ui.components.PasswordTextField
import com.system.shailendra.core.ui.theme.poppinsFont
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    state: LoginScreenState,
    onEvent: (LoginScreenEvent) -> Unit,
    onNavigate: (destination: Any) -> Unit
) {
    val passwordFocusRequester = FocusRequester()

    LaunchedEffect(
        key1 = state,
        block = {
            if (state.isLoginSuccessful)
                onNavigate(DashboardScreen)

            if (state.isRequestFailed.isFailed){
                delay(1000)
                onEvent(LoginScreenEvent.OnClearNotification)
            }

            if (state.notificationMessage.isNotBlank()){
                delay(2000)
                onEvent(LoginScreenEvent.OnClearNotification)
            }
        }
    )
    ContentBoxWithNotification(
        message = state.notificationMessage,
        isErrorNotification = state.isRequestFailed.isFailed,
        isLoading = state.isLoading,
        content = {
            Scaffold {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Image(
                            painter = painterResource(id = R.drawable.simonro),
                            contentDescription = null,
                            modifier = Modifier.size(156.dp)
                        )
                    }
                    item {
                        Text(
                            text = "Login",
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFont
                        )
                        Text(
                            text = "Silahkan login menggunakan email dan password.",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp),
                            fontFamily = poppinsFont
                        )
                    }
                    item {
                        NormalTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.emailText,
                            leadingIcon = Icons.Default.Email,
                            hint = "Contoh: contoh@gmail.com",
                            errorState = state.emailErrorState,
                            onNewValue = {
                                onEvent(LoginScreenEvent.OnEmailChange(it))
                            },
                            onClearText = {
                                onEvent(LoginScreenEvent.OnEmailClear)
                            },
                            keyboardType = KeyboardType.Email,
                            keyboardActions = KeyboardActions(
                                onNext = { passwordFocusRequester.requestFocus() }
                            )
                        )
                        PasswordTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.passwordText,
                            leadingIcon = Icons.Default.Lock,
                            isVisible = state.isPasswordVisible,
                            hint = "Masukkan password anda",
                            errorState = state.passwordErrorState,
                            onNewValue = {
                                onEvent(LoginScreenEvent.OnPasswordChange(it))
                            },
                            onVisibilityToggle = {
                                onEvent(LoginScreenEvent.OnPasswordVisibilityChange(it))
                            },
                            onDone = { onEvent(LoginScreenEvent.OnLoginClick) },
                            focusRequester = passwordFocusRequester
                        )
                    }
                    item {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onEvent(LoginScreenEvent.OnLoginClick) },
                            content = {
                                Text(text = "Login", fontFamily = poppinsFont)
                            }
                        )
                    }
                }
            }
        }
    )
}