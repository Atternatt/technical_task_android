package com.m2f.sliidetest.SliideTest.business.data

import okhttp3.Interceptor
import okhttp3.Response

object AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.request().newBuilder()
            .header(
                    "Authorization",
                    "Bearer 0b220f4a5d71275bcd252a9ea5a11b0b2931e9f145ffc6de27e2155ceb90e3bb"
            )
            .build()
            .let { chain.proceed(it) }
}