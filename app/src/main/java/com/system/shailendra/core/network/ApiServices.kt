package com.system.shailendra.core.network

import com.system.shailendra.dashboard.data.CheckDriverIdResponse
import com.system.shailendra.dashboard.data.CheckStationIdResponse
import com.system.shailendra.dashboard.data.CheckTokenResponse
import com.system.shailendra.dashboard.data.CheckTruckIdResponse
import com.system.shailendra.heavy_vehicle_fuel.data.CheckHeavyVehicleIdResponse
import com.system.shailendra.heavy_vehicle_fuel.data.HeavyVehicleFuelLogRequest
import com.system.shailendra.heavy_vehicle_fuel.data.HeavyVehicleFuelRequest
import com.system.shailendra.heavy_vehicle_fuel.data.HeavyVehicleFuelResponse
import com.system.shailendra.heavy_vehicle_fuel.data.HeavyVehicleLogResponse
import com.system.shailendra.login.data.LoginRequest
import com.system.shailendra.login.data.LoginResponse
import com.system.shailendra.material.data.PostMaterialLogRequest
import com.system.shailendra.material.data.PostMaterialRequest
import com.system.shailendra.material.data.PostMaterialResponse
import com.system.shailendra.material.data.PostToLogResponse
import com.system.shailendra.settings.data.LogoutResponse
import com.system.shailendra.station.data.GetStationResponse
import com.system.shailendra.truck_fuel.data.TruckFuelLogRequest
import com.system.shailendra.truck_fuel.data.TruckFuelLogResponse
import com.system.shailendra.truck_fuel.data.TruckFuelRequest
import com.system.shailendra.truck_fuel.data.TruckFuelResponse
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

    @GET("station/check-availability/gas/{id}")
    fun checkGasStationId(
        @Path("id") stationId: String,
        @Header("Authorization") token: String
    ): Call<CheckStationIdResponse>

    @GET("station/check-availability/mine/{id}")
    fun checkMineStationId(
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
        @Body request: PostMaterialLogRequest
    ): Call<PostToLogResponse>

    @POST("gas-operator/fuel-log/truck/store")
    fun postFuelTruck(
        @Header("Authorization") token: String,
        @Body truckFuelRequest: TruckFuelRequest
    ): Call<TruckFuelResponse>

    @POST("gas-operator/fuel-log-error-log/truck/store")
    fun postFuelLog(
        @Header("Authorization") token: String,
        @Body truckFuelLogRequest: TruckFuelLogRequest
    ): Call<TruckFuelLogResponse>

    @POST("gas-operator/fuel-log/heavy-vehicle/store")
    fun postFuelHeavyVehicle(
        @Header("Authorization") token: String,
        @Body heavyVehicleFuelRequest: HeavyVehicleFuelRequest
    ): Call<HeavyVehicleFuelResponse>

    @POST("gas-operator/fuel-log-error-log/heavy-vehicle/store")
    fun postFuelHeavyVehicleLog(
        @Header("Authorization") token: String,
        @Body heavyVehicleLogRequest: HeavyVehicleFuelLogRequest
    ): Call<HeavyVehicleLogResponse>

    @GET("heavy-vehicle/check-availability/{id}")
    fun checkHeavyVehicleId(
        @Path("id") heavyVehicleId: String,
        @Header("Authorization") token: String
    ): Call<CheckHeavyVehicleIdResponse>

    @GET("checker/station/{id}")
    fun getStation(
        @Path("id") stationId: String,
        @Header("Authorization") token: String
    ): Call<GetStationResponse>

    @GET("me")
    fun checkToken(
        @Header("Authorization") token: String
    ): Call<CheckTokenResponse>

}