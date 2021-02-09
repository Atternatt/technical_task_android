package com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.memory

import com.m2f.sliidetest.SliideTest.core_architecture.canEmitt
import com.m2f.sliidetest.SliideTest.core_architecture.error.DataNotFoundException
import com.m2f.sliidetest.SliideTest.core_architecture.error.QueryNotSupportedException
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.DeleteDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.GetDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.PutDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.KeyQuery
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * @author Marc Moreno
 * @since 9/1/2018.
 */
class InMemoryDataSource<V> : GetDataSource<V>, PutDataSource<V>, DeleteDataSource {

    private val objects: MutableMap<String, V> = mutableMapOf()
    private val arrays: MutableMap<String, List<V>> = mutableMapOf()

    override fun get(query: Query): Observable<V> = Observable.create { emitter ->
        emitter.canEmitt {
            when (query) {
                is KeyQuery -> {
                    val result = objects[query.key]
                    if (result != null) {
                        emitter.onNext(result)
                        emitter.onComplete()
                    } else {
                        emitter.onError(DataNotFoundException())
                    }
                }
                else -> emitter.onError(QueryNotSupportedException())
            }
        }
    }

    override fun getAll(query: Query): Observable<List<V>> {
        return Observable.create { emitter ->
            emitter.canEmitt {
                when (query) {
                    is KeyQuery -> {
                        val list = arrays[query.key]
                        if (list != null) {
                            emitter.onNext(list)
                            emitter.onComplete()
                        } else {
                            emitter.onError(DataNotFoundException())
                        }
                    }
                    else -> emitter.onError(QueryNotSupportedException())
                }
            }
        }
    }

    override fun put(query: Query, value: V?): Observable<V> = Observable.create { emitter ->
        emitter.canEmitt {
            when (query) {
                is KeyQuery -> {
                    if (value != null) {
                        objects[query.key] = value
                        emitter.onNext(value)
                        emitter.onComplete()
                    } else {
                        emitter.onError(IllegalArgumentException("InMemoryDataSource: value must be not null"))
                    }
                }
                else -> emitter.onError(QueryNotSupportedException())
            }
        }
    }

    override fun putAll(query: Query, value: List<V>?): Observable<List<V>> =
        Observable.create { emitter ->
            emitter.canEmitt {
                when (query) {
                    is KeyQuery -> {
                        if (value != null) {
                            arrays[query.key] = value
                            emitter.onComplete()
                        } else {
                            emitter.onError(IllegalArgumentException("InMemoryDataSource: values must be not null"))
                        }
                    }
                    else -> emitter.onError(QueryNotSupportedException())
                }
            }
        }

    override fun delete(query: Query): Completable {
        return Completable.create { emitter ->
            if (!emitter.isDisposed) {
                when (query) {
                    is KeyQuery -> {
                        objects.remove(query.key)
                        emitter.onComplete()
                    }
                    else -> emitter.onError(QueryNotSupportedException())
                }
            }
        }
    }

    override fun deleteAll(query: Query): Completable {
        return Completable.create { emitter ->
            if (!emitter.isDisposed) {
                when (query) {
                    is KeyQuery -> {
                        arrays.remove(query.key)
                        emitter.onComplete()
                    }
                    else -> emitter.onError(QueryNotSupportedException())
                }
            }
        }
    }
}