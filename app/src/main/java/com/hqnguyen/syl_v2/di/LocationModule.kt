package com.hqnguyen.syl_v2.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.hqnguyen.syl_v2.data.location.LocationAction
import com.hqnguyen.syl_v2.data.location.LocationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocationModule {

    @Provides
    @Singleton
    fun provideLocationManager(@ApplicationContext appContext: Context, client: FusedLocationProviderClient): LocationAction {
        return LocationManager(appContext, client)
    }

    @Provides
    @Singleton
    fun provideLocationProvider(@ApplicationContext appContext: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(appContext)
    }

}