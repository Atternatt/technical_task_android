package com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor

import com.m2f.sliidetest.SliideTest.business.data.features.users.di.qualifiers.UserQualifier
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.DeleteUserQuery
import com.m2f.sliidetest.SliideTest.core_architecture.repository.DeleteRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.MainOperation
import io.reactivex.Completable
import javax.inject.Inject

class DefaultDeleteUserInteractor @Inject constructor(@UserQualifier private val repository: DeleteRepository) :
    DeleteUserInteractor {

    override fun invoke(userId: Long): Completable =
        repository.delete(DeleteUserQuery(userId), MainOperation)
}