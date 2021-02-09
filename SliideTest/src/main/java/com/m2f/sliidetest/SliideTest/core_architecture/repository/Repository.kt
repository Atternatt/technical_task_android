package com.m2f.sliidetest.SliideTest.core_architecture.repository

import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.DefaultOperation
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.Operation
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * @author Marc Moreno
 * @since 9/1/2018.
 */
interface Repository {

    fun notSupportedQuery(): Nothing = throw UnsupportedOperationException("Query not supported")

    fun notSupportedOperation(): Nothing = throw UnsupportedOperationException("Operation not defined")
}

// Repositories
interface GetRepository<V> : Repository {
    fun get(query: Query, operation: Operation = DefaultOperation): Observable<V>
    fun getAll(query: Query, operation: Operation = DefaultOperation): Observable<List<V>>
}

interface PutRepository<V> : Repository {
    fun put(query: Query, value: V?, operation: Operation = DefaultOperation): Observable<V>
    fun putAll(query: Query, value: List<V>? = emptyList(), operation: Operation = DefaultOperation): Observable<List<V>>
}

interface DeleteRepository : Repository {
    fun delete(query: Query, operation: Operation = DefaultOperation): Completable
    fun deleteAll(query: Query, operation: Operation = DefaultOperation): Completable
}

fun <K, V> GetRepository<K>.withMapping(mapper: (K) -> V): GetRepository<V> = GetRepositoryMapper(this, mapper)

fun <K, V> PutRepository<K>.withMapping(toMapper: (K) -> V, fromMapper: (V) -> K): PutRepository<V> = PutRepositoryMapper(this, toMapper, fromMapper)