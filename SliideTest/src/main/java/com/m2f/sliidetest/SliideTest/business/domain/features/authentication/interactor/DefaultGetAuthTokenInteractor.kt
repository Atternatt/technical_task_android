package com.m2f.sliidetest.SliideTest.business.domain.features.authentication.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.authentication.model.AuthToken
import com.m2f.sliidetest.SliideTest.business.domain.features.authentication.query.AuthTokenQuery
import com.m2f.sliidetest.SliideTest.core_architecture.repository.GetRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.MainOperation
import io.reactivex.Single

internal class DefaultGetAuthTokenInteractor constructor(private val getRepository: GetRepository<AuthToken>) : GetAuthTokenInteractor {

    companion object {
        const val EMPTY_TOKEN = ""
    }

    override fun invoke(): Single<AuthToken> = getRepository.get(AuthTokenQuery, MainOperation).single(AuthToken(EMPTY_TOKEN))
}