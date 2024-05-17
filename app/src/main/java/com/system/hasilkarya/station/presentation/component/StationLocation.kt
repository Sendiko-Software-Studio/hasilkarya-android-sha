package com.system.hasilkarya.station.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.system.hasilkarya.core.ui.theme.poppinsFont

@Composable
fun StationLocation(
    modifier: Modifier = Modifier,
    stationName: String,
    onButtonClick: () -> Unit,
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Anda bertugas di: ", fontFamily = poppinsFont)
            TextButton(
                onClick = onButtonClick,
                content = {
                    Text(text = "Ganti Pos", fontFamily = poppinsFont)
                }
            )
        }
        Text(
            text = stationName,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFont
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}