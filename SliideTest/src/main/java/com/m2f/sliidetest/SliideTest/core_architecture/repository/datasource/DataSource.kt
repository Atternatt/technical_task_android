package com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource

import com.m2f.sliidetest.SliideTest.core_architecture.error.QueryNotSupportedException
import com.m2f.sliidetest.SliideTest.core_architecture.repository.*
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * @author Marc Moreno
 * @since 9/1/2018.
 */
interface DataSource {
    fun notSupportedQuery(): Nothing = throw QueryNotSupportedException("Query not supported")
}

// DataSources
interface GetDataSource<V> : DataSource {
    fun get(query: Query): Observable<V>

    fun getAll(query: Query): Observable<List<V>>
}

interface PutDataSource<V> : DataSource {
    fun put(query: Query, value: V?): Observable<V>

    fun putAll(query: Query, value: List<V>? = emptyList()): Observable<List<V>>
}

interface DeleteDataSource : DataSource {
    fun delete(query: Query): Completable

    fun deleteAll(query: Query): Completable
}

// Extensions to create
fun <V> GetDataSource<V>.toGetRepository() = SingleGetDataSourceRepository(this)

fun <K, V> GetDataSource<K>.toGetRepository(mapper: (K) -> V): GetRepository<V> = toGetRepository().withMapping(mapper)

fun <V> PutDataSource<V>.toPutRepository() = SinglePutDataSourceRepository(this)

fun <K, V> PutDataSource<K>.toPutRepository(toMapper: (K) -> V, fromMapper: (V) -> K): PutRepository<V> = toPutRepository().withMapping(toMapper, fromMapper)

fun DeleteDataSource.toDeleteRepository() = SingleDeleteDataSourceRepository(this)