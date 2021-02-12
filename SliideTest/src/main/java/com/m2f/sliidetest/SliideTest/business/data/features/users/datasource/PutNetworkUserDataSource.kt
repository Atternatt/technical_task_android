package com.m2f.sliidetest.SliideTest.business.data.features.users.datasource

import com.m2f.sliidetest.SliideTest.business.data.features.users.model.CreateUserEntity
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UserEntity
import com.m2f.sliidetest.SliideTest.business.data.features.users.service.UserService
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.CreateUserQuery
import com.m2f.sliidetest.SliideTest.core_architecture.error.QueryNotSupportedException
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.PutDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Observable
import javax.inject.Inject


class PutNetworkUserDataSource @Inject constructor(private val apiService: UserService) : PutDataSource<UserEntity> {
    override fun put(query: Query, value: UserEntity?): Observable<UserEntity> =
            when (query) {
                is CreateUserQuery -> {
                    apiService.createUser(
                            CreateUserEntity(
                                    name = query.name,
                                    email = query.email,
                                    gender = query.gender.value,
                                    status = "Active"
                            )
                    ).map { it.data }

                }
                else -> Observable.error(QueryNotSupportedException())
            }

    override fun putAll(query: Query, value: List<UserEntity>?): Observable<List<UserEntity>> =
            Observable.error(NotImplementedError())
}