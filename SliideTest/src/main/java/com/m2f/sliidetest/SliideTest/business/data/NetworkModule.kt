package com.m2f.sliidetest.SliideTest.business.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.m2f.sliidetest.SliideTest.BuildConfig
import com.m2f.sliidetest.SliideTest.business.data.features.users.Auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Auth
    fun providesAuthInterceptor(): Interceptor = AuthInterceptor

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }


    @Provides
    @Singleton
    fun provideGsonConverter(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideLoggingCapableHttpClient(loggingInterceptor: HttpLoggingInterceptor,
            @Auth interceptor: Interceptor): OkHttpClient
    {
        return OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(interceptor)
                .hostnameVerifier { _, _ -> true }
                .build()
    }

    @Provides
    @Singleton
    fun provideRxJavaCallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
            okHttpClient: OkHttpClient,
            gson: Gson,
            rxJavaCallAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit {

        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .client(okHttpClient)
                .baseUrl(BuildConfig.API_URL)
                .build()
    }
}