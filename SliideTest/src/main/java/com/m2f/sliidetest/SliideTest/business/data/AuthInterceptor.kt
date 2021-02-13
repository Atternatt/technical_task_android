package com.m2f.sliidetest.SliideTest.business.data

import com.m2f.sliidetest.SliideTest.business.domain.features.authentication.interactor.GetAuthTokenInteractor
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val getAuthTokenInteractor: GetAuthTokenInteractor) :
    Interceptor {

    private val mutex = Mutex()

    override fun intercept(chain: Interceptor.Chain): Response {
        return runBlocking {
            val builder = chain.request().newBuilder()
            mutex.withLock(getAuthTokenInteractor) {
                val token = getAuthTokenInteractor().blockingGet()
                builder.header(
                    "Authorization",
                    "Bearer ${token.value}"
                )
            }

            builder.build()
        }.let { chain.proceed(it) }
    }
}