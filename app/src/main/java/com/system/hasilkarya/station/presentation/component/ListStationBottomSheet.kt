package com.system.hasilkarya.station.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.system.hasilkarya.core.entities.StationEntity
import com.system.hasilkarya.core.navigation.Destination
import com.system.hasilkarya.core.ui.theme.poppinsFont
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListStationBottomSheet(
    listStation: List<StationEntity>,
    onItemClick: (StationEntity) -> Unit,
    onNavigate: (Destination) -> Unit,
    onDismiss: () -> Unit,
    isVisible: Boolean,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = isVisible) {
        if (isVisible) {
            sheetState.show()
        } else sheetState.hide()
    }
    if (isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss,
            content = {
                Column(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "List pos",
                        fontFamily = poppinsFont,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    listStation.forEach { station ->
                        Text(
                            text = station.name,
                            fontFamily = poppinsFont,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                .clickable {
                                    onItemClick(station)
                                    scope.launch { sheetState.hide() }
                                }
                                .fillMaxWidth()
                        )
                    }
                    Text(
                        text = " + Tambah Pos",
                        fontFamily = poppinsFont,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable {
                                onNavigate(Destination.SplashScreen)
                            }
                            .fillMaxWidth()
                    )
                }
            }
        )
    }
}