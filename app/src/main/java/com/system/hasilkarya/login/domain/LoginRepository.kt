package com.system.hasilkarya.login.domain

import com.system.hasilkarya.core.network.ApiServices
import com.system.hasilkarya.login.data.LoginRequest
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiServices: ApiServices) {

    fun login(request: LoginRequest) = apiServices.login(request)
}