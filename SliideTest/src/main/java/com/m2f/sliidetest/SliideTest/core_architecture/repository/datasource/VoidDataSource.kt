package com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource

import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * @author Marc Moreno
 * @since 9/1/2018.
 */
class VoidDataSource<V> : GetDataSource<V>, PutDataSource<V>, DeleteDataSource {
    override fun get(query: Query): Observable<V> = Observable.error(UnsupportedOperationException())

    override fun getAll(query: Query): Observable<List<V>> = Observable.error(UnsupportedOperationException())

    override fun put(query: Query, value: V?): Observable<V> = Observable.error(UnsupportedOperationException())

    override fun putAll(query: Query, value: List<V>?): Observable<List<V>> = Observable.error(UnsupportedOperationException())

    override fun delete(query: Query): Completable = Completable.error(UnsupportedOperationException())

    override fun deleteAll(query: Query): Completable = Completable.error(UnsupportedOperationException())
}

class VoidGetDataSource<V> : GetDataSource<V> {
    override fun get(query: Query): Observable<V> = Observable.error(UnsupportedOperationException())

    override fun getAll(query: Query): Observable<List<V>> = Observable.error(UnsupportedOperationException())
}

class VoidPutDataSource<V> : PutDataSource<V> {
    override fun put(query: Query, value: V?): Observable<V> = Observable.error(UnsupportedOperationException())

    override fun putAll(query: Query, value: List<V>?): Observable<List<V>> = Observable.error(UnsupportedOperationException())
}

class VoidDeleteDataSource : DeleteDataSource {
    override fun delete(query: Query): Completable = Completable.error(UnsupportedOperationException())

    override fun deleteAll(query: Query): Completable = Completable.error(UnsupportedOperationException())
}