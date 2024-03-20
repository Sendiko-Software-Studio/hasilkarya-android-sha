package com.system.hasilkarya.core.network

import com.system.hasilkarya.dashboard.data.PostMaterialRequest
import com.system.hasilkarya.dashboard.data.PostMaterialResponse
import com.system.hasilkarya.dashboard.data.PostToLogRequest
import com.system.hasilkarya.login.data.LoginRequest
import com.system.hasilkarya.login.data.LoginResponse
import com.system.hasilkarya.profile.data.LogoutResponse
import com.system.hasilkarya.qr.data.CheckDriverIdResponse
import com.system.hasilkarya.qr.data.CheckStationIdResponse
import com.system.hasilkarya.qr.data.CheckTruckIdResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServices {

    @POST("login")
    fun login(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @POST("checker/material-movement/store")
    fun postMaterial(
        @Header("Authorization") token: String,
        @Body request: PostMaterialRequest
    ): Call<PostMaterialResponse>

    @GET("driver/check-availability/{id}")
    fun checkDriverId(
        @Path("id") driverId: String,
        @Header("Authorization") token: String
    ): Call<CheckDriverIdResponse>

    @GET("truck/check-availability/{id}")
    fun checkTruckId(
        @Path("id") truckId: String,
        @Header("Authorization") token: String
    ): Call<CheckTruckIdResponse>

    @GET("station/check-availability/{id}")
    fun checkStationId(
        @Path("id") stationId: String,
        @Header("Authorization") token: String
    ): Call<CheckStationIdResponse>

    @POST("logout")
    fun logout(
        @Header("Authorization") token: String
    ): Call<LogoutResponse>

    @POST("checker/material-movement-error-log/store")
    fun postToLog(
        @Header("Authorization") token: String,
        @Body request: PostToLogRequest
    ): Call<PostMaterialResponse>

}