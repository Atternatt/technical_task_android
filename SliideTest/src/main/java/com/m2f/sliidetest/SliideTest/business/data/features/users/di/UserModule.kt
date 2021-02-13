package com.m2f.sliidetest.SliideTest.business.data.features.users.di

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.m2f.sliidetest.SliideTest.business.data.features.users.datasource.DeleteUserNetworkDatasource
import com.m2f.sliidetest.SliideTest.business.data.features.users.datasource.GetUsersNetworkDatasource
import com.m2f.sliidetest.SliideTest.business.data.features.users.datasource.PutNetworkUserDataSource
import com.m2f.sliidetest.SliideTest.business.data.features.users.di.qualifiers.UserQualifier
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UserEntity
import com.m2f.sliidetest.SliideTest.business.data.features.users.service.UserService
import com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor.*
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.core_architecture.repository.*
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.VoidDeleteDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.storage.DeviceStorageDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.storage.DeviceStorageObjectAssemblerDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.mapper.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import java.lang.annotation.Documented
import javax.inject.Qualifier
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
    fun providesDeleteUserInteractor(interactor: DefaultDeleteUserInteractor): DeleteUserInteractor = interactor

    @Provides
    @Singleton
    fun providesUsersRepository(
        getUsersNetworkDatasource: GetUsersNetworkDatasource,
        deviceStorageDataSource: DeviceStorageDataSource<String>,
        putNetworkUserDataSource: PutNetworkUserDataSource,
        deleteUserNetworkDatasource: DeleteUserNetworkDatasource
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
            deleteMain = deleteUserNetworkDatasource,
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

    @Provides
    @Singleton
    @UserQualifier
    fun providesDeteleRepository(cacheRepo: CacheRepository<UserEntity>): DeleteRepository = cacheRepo
}