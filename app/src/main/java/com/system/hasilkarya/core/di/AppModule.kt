package com.system.hasilkarya.core.di

import android.content.Context
import androidx.room.Room
import com.system.hasilkarya.core.network.ApiServices
import com.system.hasilkarya.core.network.NetworkConnectivityObserver
import com.system.hasilkarya.core.preferences.AppPreferences
import com.system.hasilkarya.core.preferences.dataStore
import com.system.hasilkarya.dashboard.domain.MaterialDao
import com.system.hasilkarya.dashboard.domain.MaterialDatabase
import com.system.hasilkarya.dashboard.domain.MaterialRepository
import com.system.hasilkarya.login.domain.LoginRepository
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

    @Singleton
    @Provides
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
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api-system.hasilkarya.co.id/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }

    @Singleton
    @Provides
    fun provideLoginRepository(apiServices: ApiServices): LoginRepository {
        return LoginRepository(apiServices)
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MaterialDatabase {
        return Room.databaseBuilder(context, MaterialDatabase::class.java, "material_db")
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(database: MaterialDatabase): MaterialDao {
        return database.dao
    }

    @Singleton
    @Provides
    fun provideMaterialRepository(dao: MaterialDao, apiServices: ApiServices): MaterialRepository {
        return MaterialRepository(dao, apiServices)
    }

    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): NetworkConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }

}