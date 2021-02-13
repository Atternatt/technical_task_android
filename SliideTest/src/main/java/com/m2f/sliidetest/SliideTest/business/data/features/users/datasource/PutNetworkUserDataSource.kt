package com.m2f.sliidetest.SliideTest.business.data.features.users.datasource

import com.m2f.sliidetest.SliideTest.business.data.features.users.model.CreateUserEntity
import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UserEntity
import com.m2f.sliidetest.SliideTest.business.data.features.users.service.UserService
import com.m2f.sliidetest.SliideTest.business.domain.features.users.queries.CreateUserQuery
import com.m2f.sliidetest.SliideTest.core_architecture.error.PutObjectFailException
import com.m2f.sliidetest.SliideTest.core_architecture.error.QueryNotSupportedException
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.PutDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import io.reactivex.functions.Function
import io.reactivex.internal.functions.ObjectHelper
import io.reactivex.internal.operators.observable.ObservableOnErrorNext
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject


class PutNetworkUserDataSource @Inject constructor(private val apiService: UserService) :
    PutDataSource<UserEntity> {
    override fun put(query: Query, value: UserEntity?): Observable<UserEntity> =
        when (query) {
            is CreateUserQuery -> {
                apiService.createUser(
                    CreateUserEntity(
                        name = query.name,
                        email = query.email,
                        gender = query.gender.value,
                        status = "Active"
                    )
                )
                    .map { it.data }
                    .onError { Observable.error(PutObjectFailException()) }
            }
            else -> Observable.error(QueryNotSupportedException())
        }

    override fun putAll(query: Query, value: List<UserEntity>?): Observable<List<UserEntity>> =
        Observable.error(NotImplementedError())
}


/**
 * Patch to fix function overloading on [Observable#onErrorResumeNext]
 * */
@CheckReturnValue
@SchedulerSupport(SchedulerSupport.NONE)
fun <T> Observable<T>.onError(resumeFunction: Function<in Throwable?, out ObservableSource<out T>?>?): Observable<T> {
    ObjectHelper.requireNonNull(resumeFunction, "resumeFunction is null")
    return RxJavaPlugins.onAssembly(ObservableOnErrorNext(this, resumeFunction, false))
}