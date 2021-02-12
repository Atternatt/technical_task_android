package com.m2f.sliidetest.SliideTest.business.domain.features.authentication.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.authentication.model.AuthToken
import io.reactivex.Single

interface GetAuthTokenInteractor {

    operator fun invoke(): Single<AuthToken>
}