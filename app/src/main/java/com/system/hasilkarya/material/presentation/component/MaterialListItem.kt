package com.system.hasilkarya.material.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.system.hasilkarya.core.entities.MaterialEntity
import com.system.hasilkarya.core.ui.theme.poppinsFont

@Composable
fun MaterialListItem(
    modifier: Modifier = Modifier,
    materialEntity: MaterialEntity
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Top),
        horizontalAlignment = Alignment.End,
    ) {
        AssistChip(onClick = {  }, label = { Text(text = "Menunggu jaringan", fontFamily = poppinsFont) })
        Text(
            text = "driverId: ${materialEntity.driverId.take(8)}...",
            fontFamily = poppinsFont,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Text(
            text = "truckId: ${materialEntity.truckId.take(8)}...",
            fontFamily = poppinsFont,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Text(
            text = "stationId: ${materialEntity.stationId.take(8)}...",
            fontFamily = poppinsFont,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Text(
            text = "checkerId: ${materialEntity.checkerId.take(8)}...",
            fontFamily = poppinsFont,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Text(
            text = "rasio observasi: ${materialEntity.ratio}",
            fontFamily = poppinsFont,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Text(
            text = "keterangan: ${materialEntity.remarks}",
            fontFamily = poppinsFont,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
    Divider(modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
fun MaterialListPrev() {
    Surface {
        val data = MaterialEntity(
            checkerId = "2308b833-4bd3-4dcf-8813-6df57e5c22d6",
            driverId = "c12639e5-ccaa-4cab-82e8-6d768d0db174",
            ratio = 0.6,
            remarks = "sesuai test gagal",
            stationId = "854c43db-d477-4329-9c2c-3e24d700a2d6",
            truckId = "f0b6efac-0ee6-4995-8532-a6c32402f402"
        )
        Column {
            MaterialListItem(materialEntity = data, modifier = Modifier.padding(16.dp))
            MaterialListItem(materialEntity = data, modifier = Modifier.padding(16.dp))
            MaterialListItem(materialEntity = data, modifier = Modifier.padding(16.dp))
            MaterialListItem(materialEntity = data, modifier = Modifier.padding(16.dp))

        }
    }
}