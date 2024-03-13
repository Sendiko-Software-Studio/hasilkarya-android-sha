package com.system.hasilkarya.core.di

import android.content.Context
import com.system.hasilkarya.core.preferences.AppPreferences
import com.system.hasilkarya.core.preferences.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStorePreferences(@ApplicationContext context: Context): AppPreferences {
        return AppPreferences(requireNotNull(context.dataStore))
    }
}