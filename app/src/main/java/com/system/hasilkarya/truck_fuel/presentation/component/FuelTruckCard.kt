package com.system.hasilkarya.truck_fuel.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AssistChip
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.system.hasilkarya.core.entities.FuelTruckEntity
import com.system.hasilkarya.core.ui.theme.poppinsFont

@Composable
fun FuelTruckCard(
    modifier: Modifier = Modifier,
    fuelTruckEntity: FuelTruckEntity
) {
    OutlinedCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp, alignment = Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            Row {
                AssistChip(
                    onClick = { },
                    label = { Text(text = "Menunggu jaringan", fontFamily = poppinsFont) },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                AssistChip(
                    onClick = { },
                    label = { Text(text = "Transaksi BBM", fontFamily = poppinsFont) },
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                text = "ID Driver: ${fuelTruckEntity.driverId.take(8)}...",
                fontFamily = poppinsFont,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = "ID Truk: ${fuelTruckEntity.truckId.take(8)}...",
                fontFamily = poppinsFont,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = "ID Pos: ${fuelTruckEntity.stationId.take(8)}...",
                fontFamily = poppinsFont,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = "ID Operator BBM: ${fuelTruckEntity.userId.take(8)}...",
                fontFamily = poppinsFont,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = "Jumlah liter BBM: ${fuelTruckEntity.volume}",
                fontFamily = poppinsFont,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = "Odometer: ${fuelTruckEntity.odometer}",
                fontFamily = poppinsFont,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Text(
                text = "Keterangan: ${fuelTruckEntity.remarks}",
                fontFamily = poppinsFont,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Preview
@Composable
fun FuelTruckCardPrev() {
    Surface {
        val data = FuelTruckEntity(
            userId = "2308b833-4bd3-4dcf-8813-6df57e5c22d6",
            driverId = "c12639e5-ccaa-4cab-82e8-6d768d0db174",
            volume = 0.6,
            remarks = "sesuai test gagal",
            stationId = "854c43db-d477-4329-9c2c-3e24d700a2d6",
            truckId = "f0b6efac-0ee6-4995-8532-a6c32402f402",
            odometer = 89.9
        )
        Column {
            FuelTruckCard(fuelTruckEntity = data)
            FuelTruckCard(fuelTruckEntity = data)
            FuelTruckCard(fuelTruckEntity = data)
        }
    }
}