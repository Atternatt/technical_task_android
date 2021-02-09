package com.m2f.sliidetest.SliideTest.core_architecture.repository

import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.Operation
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * @author Marc Moreno
 * @since 9/1/2018.
 */

/**
 * This repository uses mappers to map objects and redirects them to the contained repository, acting as a simple "translator".
 *
 * @param getRepository Repository with get operations
 * @param putRepository Repository with put operations
 * @param deleteRepository Repository with delete operations
 * @param toOutMapper Mapper to map data objects to domain objects
 * @param toInMapper Mapper to map domain objects to data objects
 */
class RepositoryMapper<In, Out>(
        private val getRepository: GetRepository<In>,
        private val putRepository: PutRepository<In>,
        private val deleteRepository: DeleteRepository,
        private val toOutMapper: (In) -> Out,
        private val toInMapper: (Out) -> In
) : GetRepository<Out>, PutRepository<Out>, DeleteRepository {

    override fun get(query: Query, operation: Operation): Observable<Out> = getRepository.get(query, operation).map(toOutMapper)

    override fun getAll(query: Query, operation: Operation): Observable<List<Out>> = getRepository.getAll(query, operation).map { it.map(toOutMapper) }

    override fun put(query: Query, value: Out?, operation: Operation): Observable<Out> {
        val mapped = value?.let(toInMapper)
        return putRepository.put(query, mapped, operation).map(toOutMapper)
    }

    override fun putAll(query: Query, value: List<Out>?, operation: Operation): Observable<List<Out>> {
        val mapped = value?.let { it.map(toInMapper) }
        return putRepository.putAll(query, mapped, operation).map { it.map(toOutMapper) }
    }

    override fun delete(query: Query, operation: Operation): Completable = deleteRepository.delete(query, operation)

    override fun deleteAll(query: Query, operation: Operation): Completable = deleteRepository.deleteAll(query, operation)

}

class GetRepositoryMapper<In, Out>(
        private val getRepository: GetRepository<In>,
        private val toOutMapper: (In) -> Out
) : GetRepository<Out> {

    override fun get(query: Query, operation: Operation): Observable<Out> = getRepository.get(query, operation).map(toOutMapper)

    override fun getAll(query: Query, operation: Operation): Observable<List<Out>> = getRepository.getAll(query, operation).map { it.map(toOutMapper) }
}

class PutRepositoryMapper<In, Out>(
        private val putRepository: PutRepository<In>,
        private val toOutMapper: (In) -> Out,
        private val toInMapper: (Out) -> In) : PutRepository<Out> {

    override fun put(query: Query, value: Out?, operation: Operation): Observable<Out> {
        val mapped = value?.let(toInMapper)
        return putRepository.put(query, mapped, operation).map(toOutMapper)
    }

    override fun putAll(query: Query, value: List<Out>?, operation: Operation): Observable<List<Out>> {
        val mapped = value?.let { it.map(toInMapper) }
        return putRepository.putAll(query, mapped, operation).map { it.map(toOutMapper) }
    }
}