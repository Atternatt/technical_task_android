package com.m2f.sliidetest.SliideTest.business.data.features.authentication.di

import com.m2f.sliidetest.SliideTest.business.data.features.authentication.datasource.LocalAuthTokenDataSource
import com.m2f.sliidetest.SliideTest.business.domain.features.authentication.interactor.DefaultGetAuthTokenInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.authentication.interactor.GetAuthTokenInteractor
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.toGetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun providesAuthTokenInteractor(): GetAuthTokenInteractor =
        DefaultGetAuthTokenInteractor(LocalAuthTokenDataSource.toGetRepository())
}