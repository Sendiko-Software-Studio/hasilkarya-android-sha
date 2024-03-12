package com.system.hasilkarya.core.dashboard.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardScreenViewModel: ViewModel() {

    private val _state = MutableStateFlow(DashboardScreenState())
    val state = _state.asStateFlow()

}