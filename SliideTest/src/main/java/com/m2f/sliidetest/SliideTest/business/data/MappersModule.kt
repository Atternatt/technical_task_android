package com.m2f.sliidetest.SliideTest.business.data

import com.m2f.sliidetest.SliideTest.business.data.features.users.mapper.UserEntityToUserMapper
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UserEntity
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.core_architecture.repository.mapper.Mapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object MappersModule {

    @Provides
    @Singleton
    fun providesUserEntityToUserMapper(): Mapper<UserEntity, User> = UserEntityToUserMapper

}