package com.m2f.sliidetest.SliideTest

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TestEndpointModule {

    private val mockWebServer = MockWebServer().apply {
        dispatcher = TestNetworkDispatcher
    }

    @Provides
    @Singleton
    fun providesUrl(): HttpUrl = mockWebServer.url("/")
}