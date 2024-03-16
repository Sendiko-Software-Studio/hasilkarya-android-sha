package com.system.hasilkarya.qr.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.ui.theme.poppinsFont
import com.system.hasilkarya.dashboard.presentation.ScanOptions

@Composable
fun QrFormHeader(
    navigateBack: () -> Unit,
    result: String,
    currentlyScanning: ScanOptions
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { navigateBack() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "kembali"
            )
        }
        Text(
            modifier = Modifier
                .padding(
                    vertical = 24.dp,
                    horizontal = 24.dp
                )
                .fillMaxWidth(),
            text = if (result.isNotBlank()) "Scan QR berhasil!" else "Scan QR ${currentlyScanning.name}!",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontFamily = poppinsFont
        )
        IconButton(onClick = { navigateBack() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "kembali",
                tint = Color.Transparent
            )
        }
    }
}