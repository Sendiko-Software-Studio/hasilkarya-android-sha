package com.system.hasilkarya.gas.presentation

import androidx.lifecycle.ViewModel
import com.system.hasilkarya.core.repositories.GasRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GasQrScreenViewModel @Inject constructor(
    repository: GasRepository
): ViewModel() {

    private val _state = MutableStateFlow(GasQrScreenState())
    val state = _state.asStateFlow()

    fun onEvent(event: GasQrScreenEvent){
        when(event) {
            is GasQrScreenEvent.OnNavigateForm -> _state.update {
                it.copy(currentlyScanning = event.scanOptions)
            }
            is GasQrScreenEvent.OnTruckIdRegistered -> TODO()
            is GasQrScreenEvent.OnDriverIdRegistered -> TODO()
            is GasQrScreenEvent.OnStationIdRegistered -> TODO()
            is GasQrScreenEvent.OnVolumeRegistered -> TODO()
            is GasQrScreenEvent.OnOdometerChange -> TODO()
            GasQrScreenEvent.OnClearOdometer -> TODO()
            is GasQrScreenEvent.OnRemarksChange -> TODO()
            GasQrScreenEvent.OnClearRemarks -> TODO()
            GasQrScreenEvent.SaveGasTransaction -> TODO()
        }
    }
}