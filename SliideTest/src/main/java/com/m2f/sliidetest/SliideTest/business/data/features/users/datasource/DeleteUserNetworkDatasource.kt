package com.m2f.sliidetest.SliideTest.business.data.features.users.datasource

import com.m2f.sliidetest.SliideTest.business.data.features.users.service.UserService
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.DeleteUserQuery
import com.m2f.sliidetest.SliideTest.core_architecture.error.DeleteObjectFailException
import com.m2f.sliidetest.SliideTest.core_architecture.error.QueryNotSupportedException
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.DeleteDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Completable
import javax.inject.Inject

class DeleteUserNetworkDatasource @Inject constructor(private val userService: UserService) :
    DeleteDataSource {
    override fun delete(query: Query): Completable = when (query) {
        is DeleteUserQuery -> {
            userService.deleteUser(query.id)
                .flatMapCompletable {
                    if (it.code == 204) {
                        Completable.complete()
                    } else {
                        Completable.error(DeleteObjectFailException())
                    }
                }
        }
        else -> Completable.error(QueryNotSupportedException())
    }

    override fun deleteAll(query: Query): Completable = Completable.error(NotImplementedError())
}