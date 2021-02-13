package com.m2f.sliidetest.SliideTest.business.data.features.authentication.datasource

import com.m2f.sliidetest.SliideTest.business.domain.features.authentication.model.AuthToken
import com.m2f.sliidetest.SliideTest.business.domain.features.authentication.query.AuthTokenQuery
import com.m2f.sliidetest.SliideTest.core_architecture.error.QueryNotSupportedException
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.GetDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Observable

//todo @Marc: Replace this local storage for an encripted local storage or save this token into a file not included in the repository.
object LocalAuthTokenDataSource : GetDataSource<AuthToken> {
    override fun get(query: Query): Observable<AuthToken> = when(query) {
        AuthTokenQuery -> Observable.just(
            AuthToken("0b220f4a5d71275bcd252a9ea5a11b0b2931e9f145ffc6de27e2155ceb90e3bb")
        )
        else -> Observable.error(QueryNotSupportedException())

    }
    override fun getAll(query: Query): Observable<List<AuthToken>> =
        Observable.error(NotImplementedError())

}