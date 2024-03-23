package com.system.hasilkarya.dashboard.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.system.hasilkarya.R
import com.system.hasilkarya.core.ui.theme.poppinsFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuCard(
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit,
    text: String,
    icon: Painter,
) {
    Card(
        modifier = modifier,
        onClick = { onClickAction() },
        content = {
            Box(
                contentAlignment = Alignment.Center,
                content = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            painter = icon,
                            contentDescription = text,
                            modifier = Modifier.size(86.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            modifier = Modifier.width(128.dp),
                            text = text,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = poppinsFont
                        )
                    }
                }
            )
        }
    )
}

@Preview
@Composable
fun MaterialCardPrev() {
    Surface {
        MenuCard(
            text = "Scan Transaksi BBM",
            onClickAction = {},
            icon = painterResource(id = R.drawable.scan_gas),
            modifier = Modifier.padding(16.dp),
        )
    }
}