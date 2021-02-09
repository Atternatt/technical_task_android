package com.m2f.sliidetest.SliideTest.business.data.features.users.datasource

import com.m2f.sliidetest.SliideTest.business.data.features.users.model.UserEntity
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.GetDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Observable
import javax.inject.Inject


class GetUsersNetworkDatasource @Inject constructor() :
    GetDataSource<UserEntity> {

    override fun get(query: Query): Observable<UserEntity> = Observable.error(NotImplementedError())

    override fun getAll(query: Query): Observable<List<UserEntity>> = Observable.just(
        listOf(
            UserEntity("a@a", 1, "a", "aaa"),
            UserEntity("b@b", 2, "b", "bbb"),
            UserEntity("c@c", 3, "c", "ccc"),
            UserEntity("e@e", 4, "e", "eee"),
            UserEntity("f@f", 5, "f", "fff"),
            UserEntity("g@g", 6, "g", "ggg"),
            UserEntity("h@h", 7, "h", "hhh"),
            UserEntity("i@i", 8, "i", "iii"),
            UserEntity("j@j", 9, "j", "jjj"),
            UserEntity("k@k", 10, "k", "kkk"),
            UserEntity("l@l", 11, "l", "lll"),
            UserEntity("m@m", 12, "m", "mmm")
        )
    )


}