package com.system.hasilkarya.material.presentation.component

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.system.hasilkarya.R
import com.system.hasilkarya.core.ui.theme.poppinsFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialCard(
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit
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
                            painter = painterResource(id = R.drawable.scanner),
                            contentDescription = "scan",
                            modifier = Modifier.size(86.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            modifier = Modifier.width(128.dp),
                            text = "Scan Material Movement",
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
        MaterialCard {

        }
    }
}