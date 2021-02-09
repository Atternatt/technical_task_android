package com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor

import com.m2f.sliidetest.SliideTest.business.domain.features.users.model.User
import io.reactivex.Observable


interface GetAllUsersInteractor {

    operator fun invoke(forceRefresh: Boolean): Observable<List<User>>
}