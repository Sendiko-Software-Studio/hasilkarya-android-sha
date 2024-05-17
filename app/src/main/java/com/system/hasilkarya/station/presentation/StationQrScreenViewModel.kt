package com.system.hasilkarya.station.presentation

import androidx.lifecycle.ViewModel
import com.system.hasilkarya.core.repositories.station.StationRepository
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class StationQrScreenViewModel(
    private val stationRepository: StationRepository
): ViewModel() {

}