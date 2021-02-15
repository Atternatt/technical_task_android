package com.m2f.sliidetest.SliideTest.business.data.features

import com.m2f.sliidetest.SliideTest.business.data.features.users.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * This modules purpose is to provide the services in a separate way in order
 * to inject mocks in integration tests.
 */
@Module
@InstallIn(ApplicationComponent::class)
object FeaturesModule {

    @Provides
    @Singleton
    fun ptovidesUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)


}