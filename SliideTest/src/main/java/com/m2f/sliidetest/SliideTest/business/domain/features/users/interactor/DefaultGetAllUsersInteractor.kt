package com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.UsersQuery
import com.m2f.sliidetest.SliideTest.core_architecture.addThreadPolicy
import com.m2f.sliidetest.SliideTest.core_architecture.repository.GetRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.CacheOperation
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.CacheSyncOperation
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.MainSyncOperation
import io.reactivex.Observable
import javax.inject.Inject

class DefaultGetAllUsersInteractor @Inject constructor(private val getUserRepository: GetRepository<User>) :
    GetAllUsersInteractor {
    override fun invoke(forceRefresh: Boolean): Observable<List<User>> {
        val operation = if (forceRefresh) MainSyncOperation else CacheSyncOperation
        return getUserRepository.getAll(UsersQuery, operation)
    }


}