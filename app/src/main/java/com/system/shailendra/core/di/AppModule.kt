package com.system.shailendra.core.di

import android.content.Context
import androidx.room.Room
import com.system.shailendra.core.network.ApiServices
import com.system.shailendra.core.network.NetworkConnectivityObserver
import com.system.shailendra.core.preferences.AppPreferences
import com.system.shailendra.core.preferences.dataStore
import com.system.shailendra.core.repositories.AppDatabase
import com.system.shailendra.core.repositories.fuel.heavy_vehicle.HeavyVehicleDao
import com.system.shailendra.core.repositories.fuel.heavy_vehicle.HeavyVehicleFuelRepository
import com.system.shailendra.core.repositories.fuel.truck.TruckFuelDao
import com.system.shailendra.core.repositories.fuel.truck.TruckFuelRepository
import com.system.shailendra.core.repositories.material.MaterialDao
import com.system.shailendra.core.repositories.material.MaterialRepository
import com.system.shailendra.core.repositories.station.StationDao
import com.system.shailendra.core.repositories.station.StationRepository
import com.system.shailendra.core.repositories.user.UserRepository
import com.system.shailendra.login.domain.LoginRepository
import com.system.shailendra.settings.domain.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStorePreferences(@ApplicationContext context: Context): AppPreferences {
        return AppPreferences(requireNotNull(context.dataStore))
    }

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            chain.proceed(chain.request().newBuilder().also {
                it.addHeader("Accept", "application/json")
            }.build())
        }.also {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            it.addInterceptor(logging)
        }
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api-shailendra.hasilkarya.co.id/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(apiServices: ApiServices, preferences: AppPreferences): LoginRepository {
        return LoginRepository(apiServices, preferences)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "material_db")
            .build()
    }

    @Provides
    @Singleton
    fun provideMaterialDao(database: AppDatabase): MaterialDao {
        return database.materialDao
    }

    @Provides
    @Singleton
    fun provideFuelDao(database: AppDatabase): TruckFuelDao {
        return database.truckFuelDao
    }

    @Provides
    @Singleton
    fun provideHeavyDao(database: AppDatabase): HeavyVehicleDao {
        return database.heavyVehicleDao
    }

    @Provides
    @Singleton
    fun provideMaterialRepository(dao: MaterialDao, apiServices: ApiServices, preferences: AppPreferences): MaterialRepository {
        return MaterialRepository(dao, apiServices, preferences)
    }

    @Provides
    @Singleton
    fun provideTruckFuelRepository(apiServices: ApiServices, preferences: AppPreferences, dao: TruckFuelDao): TruckFuelRepository {
        return TruckFuelRepository(apiServices, preferences, dao)
    }

    @Provides
    @Singleton
    fun provideHeavyVehicleFuelRepository(apiServices: ApiServices, preferences: AppPreferences, dao: HeavyVehicleDao): HeavyVehicleFuelRepository {
        return HeavyVehicleFuelRepository(apiServices, preferences, dao)
    }

    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): NetworkConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(appPreferences: AppPreferences, apiServices: ApiServices): SettingsRepository {
        return SettingsRepository(appPreferences, apiServices)
    }

    @Provides
    @Singleton
    fun provideStationDao(database: AppDatabase): StationDao {
        return database.stationDao
    }

    @Provides
    @Singleton
    fun provideStationRepository(dao: StationDao, apiServices: ApiServices): StationRepository {
        return StationRepository(dao, apiServices)
    }

    @Provides
    @Singleton
    fun provideUserRepository(apiServices: ApiServices, appPreferences: AppPreferences): UserRepository {
        return UserRepository(apiServices, appPreferences)
    }

}