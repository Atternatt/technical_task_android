package com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.storage

import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.DeleteDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.GetDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.PutDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * @author Marc Moreno
 * @since 9/1/2018.
 */
class DeviceStorageObjectAssemblerDataSource<T>(
    private val toStringMapper: (T) -> String,
    private val toModelMapper: (String) -> T,
    private val toStringFromListMapper: (List<T>) -> String,
    private val toModelFromListString: (String) -> List<T>,
    private val deviceStorageDataSource: DeviceStorageDataSource<String>
) : GetDataSource<T>, PutDataSource<T>, DeleteDataSource {

    override fun get(query: Query): Observable<T> =
        deviceStorageDataSource.get(query).map(toModelMapper)

    override fun getAll(query: Query): Observable<List<T>> =
        deviceStorageDataSource.get(query).map(toModelFromListString)

    override fun put(query: Query, value: T?): Observable<T> {
        return if (value != null) {
            val mappedValue = value.let(toStringMapper)
            deviceStorageDataSource.put(query, mappedValue).map { value }
        } else {
            Observable.error(IllegalArgumentException("value must not be null"))
        }
    }

    override fun putAll(query: Query, value: List<T>?): Observable<List<T>> {
        return if (value != null) {
            val mappedValue = value.let(toStringFromListMapper)
            deviceStorageDataSource.put(query, mappedValue).map { value }
        } else {
            Observable.error(IllegalArgumentException("Value must be null"))
        }
    }

    override fun delete(query: Query): Completable = deviceStorageDataSource.delete(query)

    override fun deleteAll(query: Query): Completable = deviceStorageDataSource.deleteAll(query)

}
