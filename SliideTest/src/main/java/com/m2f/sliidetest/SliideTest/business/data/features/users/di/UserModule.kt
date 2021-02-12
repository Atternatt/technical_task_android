package com.m2f.sliidetest.SliideTest.business.data.features.users.di

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.m2f.sliidetest.SliideTest.business.data.features.users.datasource.GetUsersNetworkDatasource
import com.m2f.sliidetest.SliideTest.business.data.features.users.datasource.PutNetworkUserDataSource
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UserEntity
import com.m2f.sliidetest.SliideTest.business.data.features.users.service.UserService
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.AddUserInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.DefaultAddUserInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.DefaultGetAllUsersInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.GetAllUsersInteractor
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.core_architecture.repository.CacheRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.GetRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.PutRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.VoidDeleteDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.VoidPutDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.storage.DeviceStorageDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.storage.DeviceStorageObjectAssemblerDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.mapper.*
import com.m2f.sliidetest.SliideTest.core_architecture.repository.withMapping
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun ptovidesUserService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun providesGetUsersInterctor(interactor: DefaultGetAllUsersInteractor): GetAllUsersInteractor =
        interactor

    @Provides
    @Singleton
    fun providesAddUserInteractor(interactor: DefaultAddUserInteractor): AddUserInteractor = interactor

    @Provides
    @Singleton
    fun providesUsersRepository(
        getUsersNetworkDatasource: GetUsersNetworkDatasource,
        deviceStorageDataSource: DeviceStorageDataSource<String>,
        putNetworkUserDataSource: PutNetworkUserDataSource
    ): CacheRepository<UserEntity> {

        val gson = Gson()
        val toStringMapper = ModelToStringMapper<UserEntity>(gson)
        val toModelMapper = StringToModelMapper(UserEntity::class.java, gson)
        val toListModelMapper = ListModelToStringMapper<UserEntity>(gson)
        val toStringListMapper =
            StringToListModelMapper(object : TypeToken<List<UserEntity>>() {}, gson)

        val deviceStorage = DeviceStorageObjectAssemblerDataSource(
            toStringMapper,
            toModelMapper,
            toListModelMapper,
            toStringListMapper,
            deviceStorageDataSource
        )

        return CacheRepository(
            getMain = getUsersNetworkDatasource,
            putMain = putNetworkUserDataSource,
            deleteMain = VoidDeleteDataSource(),
            getCache = deviceStorage,
            putCache = deviceStorage,
            deleteCache = deviceStorage
        )
    }

    @Provides
    @Singleton
    fun providesGetRepository(
        cacheRepo: CacheRepository<UserEntity>,
        mapper: @JvmSuppressWildcards Mapper<UserEntity, User>
    ): GetRepository<User> =
        cacheRepo.withMapping(mapper)

    @Provides
    @Singleton
    fun providesPutRepository(
            cacheRepo: CacheRepository<UserEntity>,
            mapper: @JvmSuppressWildcards Mapper<UserEntity, User>
    ): PutRepository<User> =
            cacheRepo.withMapping(mapper, VoidMapper())
}