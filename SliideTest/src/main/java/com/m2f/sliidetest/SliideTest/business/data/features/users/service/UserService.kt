package com.m2f.sliidetest.SliideTest.business.data.features.users.service

import com.m2f.sliidetest.SliideTest.business.data.features.users.model.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

interface UserService {

    @GET("users")
    fun getUsers(@Query("page") page: Int? = null): Observable<UsersResponse>

    @POST("users")
    fun createUser(@Body user: CreateUserEntity): Observable<CreateUserResponse>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: Long): Single<DeleteUserResponse>
}