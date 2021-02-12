package com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.CreateUserQuery
import com.m2f.sliidetest.SliideTest.core_architecture.repository.PutRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.MainOperation
import io.reactivex.Single
import javax.inject.Inject

class DefaultAddUserInteractor @Inject constructor(val putRepository: PutRepository<User>) : AddUserInteractor {

    override operator fun invoke(name: String, email: String, gender: Gender): Single<User> {
        return putRepository.put(CreateUserQuery(
                name = name,
                email = email,
                gender = gender,
                status = "active"
        ), null, MainOperation).singleOrError()
    }

}