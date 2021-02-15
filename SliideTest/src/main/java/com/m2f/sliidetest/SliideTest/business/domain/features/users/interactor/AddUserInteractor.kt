package com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.Gender
import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import io.reactivex.Single

sealed interface AddUserInteractor {

    operator fun invoke(name: String, email: String, gender: Gender): Single<User>
}