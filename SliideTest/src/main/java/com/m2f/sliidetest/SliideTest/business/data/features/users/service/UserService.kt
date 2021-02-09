package com.m2f.sliidetest.SliideTest.business.data.features.users.service

import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UserEntity
import io.reactivex.Observable
import retrofit2.http.GET

interface UserService {

    @GET("users")
    fun getLastPage(): Observable<UserEntity>
}