package com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import io.reactivex.Single

interface AddUserInteractor {

    operator fun invoke(name: String, email: String, userName: String): Single<User>
}