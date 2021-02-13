package com.m2f.sliidetest.SliideTest.business.domain.features.users.interactor

import io.reactivex.Completable

interface DeleteUserInteractor {

    operator fun invoke(userId: Long): Completable
}