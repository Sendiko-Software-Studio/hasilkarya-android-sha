package com.system.hasilkarya.core.repositories

import com.system.hasilkarya.core.network.ApiServices
import com.system.hasilkarya.core.preferences.AppPreferences
import com.system.hasilkarya.dashboard.domain.GasDao
import javax.inject.Inject

class GasRepository @Inject constructor(
    private val apiServices: ApiServices,
    private val preferences: AppPreferences,
    private val dao: GasDao
){


}