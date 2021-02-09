package com.m2f.sliidetest.SliideTest.core_architecture.repository

import com.m2f.sliidetest.SliideTest.core_architecture.error.DataNotFoundException
import com.m2f.sliidetest.SliideTest.core_architecture.error.ObjectNotValidException
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.DeleteDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.GetDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.PutDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.*
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * @author Marc Moreno
 * @since 9/1/2018.
 */
class CacheRepository<V>(
        private val getCache: GetDataSource<V>,
        private val putCache: PutDataSource<V>,
        private val deleteCache: DeleteDataSource,
        private val getMain: GetDataSource<V>,
        private val putMain: PutDataSource<V>,
        private val deleteMain: DeleteDataSource
) : GetRepository<V>, PutRepository<V>, DeleteRepository {

    override fun get(
            query: Query,
            operation: Operation
    ): Observable<V> = when (operation) {
        is DefaultOperation -> get(query, CacheSyncOperation)
        is MainOperation -> getMain.get(query)
        is CacheOperation -> getCache.get(query)
        is MainSyncOperation -> getMain.get(query).flatMap { putCache.put(query, it) }
        is CacheSyncOperation ->
            getCache.get(query).onErrorResumeNext { t: Throwable ->
                when (t) {
                    is ObjectNotValidException, is DataNotFoundException -> get(query, MainSyncOperation)
                    else -> Observable.error(t)
                }
            }
    }

    override fun getAll(
            query: Query,
            operation: Operation
    ): Observable<List<V>> = when (operation) {
        is DefaultOperation -> getAll(query, CacheSyncOperation)
        is MainOperation -> getMain.getAll(query)
        is CacheOperation -> getCache.getAll(query)
        is MainSyncOperation -> getMain.getAll(query).flatMap { putCache.putAll(query, it) }
        is CacheSyncOperation -> {
            getCache.getAll(query)
                    .onErrorResumeNext { t: Throwable ->
                        when (t) {
                            is ObjectNotValidException, is DataNotFoundException -> getAll(query, MainSyncOperation)
                            else -> Observable.error(t)
                        }
                    }
        }
    }

    override fun put(
            query: Query,
            value: V?,
            operation: Operation
    ): Observable<V> = when (operation) {
        is DefaultOperation -> put(query, value, MainSyncOperation)
        is MainOperation -> putMain.put(query, value)
        is CacheOperation -> putCache.put(query, value)
        is MainSyncOperation -> putMain.put(query, value).flatMap { putCache.put(query, it) }
        is CacheSyncOperation -> putCache.put(query, value).flatMap { putMain.put(query, it) }
    }

    override fun putAll(
            query: Query,
            value: List<V>?,
            operation: Operation
    ): Observable<List<V>> = when (operation) {
        is DefaultOperation -> putAll(query, value, MainSyncOperation)
        is MainOperation -> putMain.putAll(query, value)
        is CacheOperation -> putCache.putAll(query, value)
        is MainSyncOperation -> putMain.putAll(query, value).flatMap { putCache.putAll(query, it) }
        is CacheSyncOperation -> putCache.putAll(query, value).flatMap { putMain.putAll(query, it) }
    }

    override fun delete(
            query: Query,
            operation: Operation
    ): Completable = when (operation) {
        is DefaultOperation -> delete(query, MainSyncOperation)
        is MainOperation -> deleteMain.delete(query)
        is CacheOperation -> deleteCache.delete(query)
        is MainSyncOperation -> deleteMain.delete(query).andThen { deleteCache.delete(query) }
        is CacheSyncOperation -> deleteCache.delete(query).andThen { deleteMain.delete(query) }
    }

    override fun deleteAll(
            query: Query,
            operation: Operation
    ): Completable = when (operation) {
        is DefaultOperation -> deleteAll(query, MainSyncOperation)
        is MainOperation -> deleteMain.deleteAll(query)
        is CacheOperation -> deleteCache.deleteAll(query)
        is MainSyncOperation -> deleteMain.deleteAll(query).andThen { deleteCache.deleteAll(query) }
        is CacheSyncOperation -> deleteCache.deleteAll(query).andThen { deleteMain.deleteAll(query) }
    }
}