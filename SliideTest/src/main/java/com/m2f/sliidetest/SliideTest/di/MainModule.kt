package com.m2f.sliidetest.SliideTest.di

import android.content.Context
import android.content.SharedPreferences
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.storage.DeviceStorageDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class MainModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences("MAIN", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providesDeviceStorageDatasource(sharedPreferences: SharedPreferences): DeviceStorageDataSource<String> =
        DeviceStorageDataSource(sharedPreferences)
}