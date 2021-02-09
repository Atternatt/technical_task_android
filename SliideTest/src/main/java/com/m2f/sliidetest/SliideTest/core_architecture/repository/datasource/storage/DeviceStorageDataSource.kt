package com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.storage

import android.content.SharedPreferences
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
class DeviceStorageDataSource<T>(
    private val sharedPreferences: SharedPreferences,
    private val prefix: String = ""
) : GetDataSource<T>, PutDataSource<T>, DeleteDataSource {

    override fun get(query: Query): Observable<T> = Observable.defer {
        Observable.create<T> { emitter ->
            emitter.canEmitt {
                when (query) {
                    is KeyQuery -> {
                        val key = addPrefixTo(query.key)
                        if (!sharedPreferences.contains(key)) {
                            emitter.onError(DataNotFoundException())
                        }
                        val value = sharedPreferences.all[key] as? T

                        if (value != null) {
                            emitter.onNext(value)
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

    override fun getAll(query: Query): Observable<List<T>> =
        Observable.error(UnsupportedOperationException("getAll not supported. Use get instead"))

    override fun put(query: Query, value: T?): Observable<T> = Observable.defer {
        Observable.create<T> { emitter ->
            emitter.canEmitt {
                when (query) {
                    is KeyQuery -> {
                        if (value != null) {
                            val key = addPrefixTo(query.key)
                            val editor = sharedPreferences.edit()
                            when (value) {
                                is String -> editor.putString(key, value).apply()
                                    .also { emitter.onNext(value) }
                                is Boolean -> editor.putBoolean(key, value).apply()
                                    .also { emitter.onNext(value) }
                                is Float -> editor.putFloat(key, value).apply()
                                    .also { emitter.onNext(value) }
                                is Int -> editor.putInt(key, value).apply()
                                    .also { emitter.onNext(value) }
                                is Long -> editor.putLong(key, value).apply()
                                    .also { emitter.onNext(value) }
                                else -> {
                                    emitter.onError(UnsupportedOperationException("value type is not supported"))
                                    return@canEmitt
                                }
                            }
                            emitter.onComplete()
                        } else {
                            emitter.onError(IllegalArgumentException("${DeviceStorageDataSource::class.java.simpleName}: value must be not null"))
                        }
                    }
                    else -> emitter.onError(QueryNotSupportedException())
                }
            }
        }
    }

    override fun putAll(query: Query, value: List<T>?): Observable<List<T>> =
        Observable.error(UnsupportedOperationException("putAll not supported. Use put instead"))

    override fun delete(query: Query): Completable = Completable.defer {
        Completable.create { emitter ->
            if (!emitter.isDisposed) {
                when (query) {
                    is KeyQuery -> {
                        sharedPreferences.edit()
                            .remove(addPrefixTo(query.key))
                            .apply()
                            .also { emitter.onComplete() }
                    }
                    else -> emitter.onError(QueryNotSupportedException())
                }
            }
        }
    }

    override fun deleteAll(query: Query): Completable = Completable.defer {
        Completable.create { emitter ->
            if (!emitter.isDisposed) {
                with(sharedPreferences.edit()) {
                    if (prefix.isNotEmpty()) {
                        sharedPreferences.all.keys.filter { it.contains(prefix) }
                            .forEach { remove(it) }
                    } else {
                        clear()
                    }
                    apply()
                }
            }
        }

    }

    private fun addPrefixTo(key: String) = if (prefix.isEmpty()) key else "$prefix.$key"
}