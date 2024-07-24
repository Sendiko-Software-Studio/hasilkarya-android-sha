package com.system.shailendra.station.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.system.shailendra.core.ui.theme.poppinsFont

@Composable
fun StationLocation(
    modifier: Modifier = Modifier,
    stationName: String,
    onButtonClick: () -> Unit,
) {
    Card(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                start = 16.dp,
                end =  16.dp,
                top =  8.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Anda bertugas di: ", fontFamily = poppinsFont)
            OutlinedButton(
                onClick = onButtonClick,
                content = {
                    Text(
                        text = if (stationName == "Tidak ada.") "Pilih pos" else "Ganti Pos",
                        fontFamily = poppinsFont
                    )
                }
            )
        }
        Text(
            text = stationName,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFont,
            modifier = Modifier.padding(
                start = 16.dp,
                end =  16.dp,
                bottom =  8.dp
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}