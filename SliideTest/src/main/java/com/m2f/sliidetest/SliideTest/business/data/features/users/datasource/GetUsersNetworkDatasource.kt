package com.m2f.sliidetest.SliideTest.business.data.features.users.datasource

import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UserEntity
import com.m2f.sliidetest.SliideTest.business.data.features.users.service.UserService
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.LastUsersQuery
import com.m2f.sliidetest.SliideTest.core_architecture.error.QueryNotSupportedException
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.GetDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Observable
import javax.inject.Inject


class GetUsersNetworkDatasource @Inject constructor(private val userService: UserService) :
    GetDataSource<UserEntity> {

    override fun get(query: Query): Observable<UserEntity> = Observable.error(NotImplementedError())

    override fun getAll(query: Query): Observable<List<UserEntity>> = when(query) {
        LastUsersQuery -> userService.getUsers()
                .flatMap { userService.getUsers(it.meta.pagination.pages) }
                .map { it.data }
        else -> Observable.error(QueryNotSupportedException())
    }


}