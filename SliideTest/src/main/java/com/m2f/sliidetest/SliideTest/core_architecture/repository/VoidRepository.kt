package com.m2f.sliidetest.SliideTest.core_architecture.repository

import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.Operation
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * @author Marc Moreno
 * @since 9/1/2018.
 */
class VoidRepository<V> : GetRepository<V>, PutRepository<V>, DeleteRepository {

    override fun get(query: Query, operation: Operation): Observable<V> = Observable.error { notSupportedOperation() }

    override fun getAll(query: Query, operation: Operation): Observable<List<V>> = Observable.error { notSupportedOperation() }

    override fun put(query: Query, value: V?, operation: Operation): Observable<V> = Observable.error { notSupportedOperation() }

    override fun putAll(query: Query, value: List<V>?, operation: Operation): Observable<List<V>> = Observable.error { notSupportedOperation() }

    override fun delete(query: Query, operation: Operation): Completable = Completable.error { notSupportedOperation() }

    override fun deleteAll(query: Query, operation: Operation): Completable = Completable.error { notSupportedOperation() }
}

class VoidGetRepository<V> : GetRepository<V> {

    override fun get(query: Query, operation: Operation): Observable<V> = Observable.error { notSupportedOperation() }

    override fun getAll(query: Query, operation: Operation): Observable<List<V>> = Observable.error { notSupportedOperation() }
}

class VoidPutRepository<V> : PutRepository<V> {
    override fun put(query: Query, value: V?, operation: Operation): Observable<V> = Observable.error { notSupportedOperation() }

    override fun putAll(query: Query, value: List<V>?, operation: Operation): Observable<List<V>> = Observable.error { notSupportedOperation() }
}

class VoidDeleteRepository : DeleteRepository {
    override fun delete(query: Query, operation: Operation): Completable = Completable.error { notSupportedOperation() }

    override fun deleteAll(query: Query, operation: Operation): Completable = Completable.error { notSupportedOperation() }
}