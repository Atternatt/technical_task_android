package com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import io.reactivex.Single

interface RemoveUserInteractor {

    operator fun invoke(user: User): Single<Unit>
}