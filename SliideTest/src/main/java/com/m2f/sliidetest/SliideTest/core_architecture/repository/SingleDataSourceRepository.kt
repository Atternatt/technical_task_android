package com.m2f.sliidetest.SliideTest.core_architecture.repository

import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.DeleteDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.GetDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.PutDataSource
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.Operation
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * @author Marc Moreno
 * @since 9/1/2018.
 */
class SingleDataSourceRepository<T> constructor(
        private val getDataSource: GetDataSource<T>,
        private val putDataSource: PutDataSource<T>,
        private val deleteDataSource: DeleteDataSource
) : GetRepository<T>, PutRepository<T>, DeleteRepository {

    override fun get(query: Query, operation: Operation): Observable<T> = getDataSource.get(query)

    override fun getAll(query: Query, operation: Operation): Observable<List<T>> = getDataSource.getAll(query)

    override fun put(query: Query, value: T?, operation: Operation): Observable<T> = putDataSource.put(query, value)

    override fun putAll(query: Query, value: List<T>?, operation: Operation): Observable<List<T>> = putDataSource.putAll(query, value)

    override fun delete(query: Query, operation: Operation): Completable = deleteDataSource.delete(query)

    override fun deleteAll(query: Query, operation: Operation): Completable = deleteDataSource.deleteAll(query)
}

class SingleGetDataSourceRepository<T>(private val getDataSource: GetDataSource<T>) : GetRepository<T> {

    override fun get(query: Query, operation: Operation): Observable<T> = getDataSource.get(query)

    override fun getAll(query: Query, operation: Operation): Observable<List<T>> = getDataSource.getAll(query)
}

class SinglePutDataSourceRepository<T>(private val putDataSource: PutDataSource<T>) : PutRepository<T> {
    override fun put(query: Query, value: T?, operation: Operation): Observable<T> = putDataSource.put(query, value)

    override fun putAll(query: Query, value: List<T>?, operation: Operation): Observable<List<T>> = putDataSource.putAll(query, value)
}


class SingleDeleteDataSourceRepository(private val deleteDataSource: DeleteDataSource) : DeleteRepository {

    override fun delete(query: Query, operation: Operation): Completable = deleteDataSource.delete(query)

    override fun deleteAll(query: Query, operation: Operation): Completable = deleteDataSource.deleteAll(query)
}