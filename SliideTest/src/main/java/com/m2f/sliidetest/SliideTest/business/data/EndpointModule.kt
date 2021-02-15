package com.m2f.sliidetest.SliideTest.business.data

import com.m2f.sliidetest.SliideTest.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object EndpointModule {

    @Singleton
    @Provides
    fun providesUrl(): HttpUrl = BuildConfig.API_URL.toHttpUrl()

}