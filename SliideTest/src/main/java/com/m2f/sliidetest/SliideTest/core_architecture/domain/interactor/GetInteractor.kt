package com.m2f.sliidetest.SliideTest.core_architecture.domain.interactor

import com.m2f.sliidetest.SliideTest.core_architecture.repository.DeleteRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.GetRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.PutRepository
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.DefaultOperation
import com.m2f.sliidetest.SliideTest.core_architecture.repository.operation.Operation
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.Query
import com.m2f.sliidetest.SliideTest.core_architecture.repository.query.VoidQuery
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Marc Moreno
 * @since 9/1/2018.
 */

class GetInteractor<M>(private val executionScheduler: Scheduler,
                       private val mainThreadScheduler: Scheduler,
                       private val getRepository: GetRepository<M>) {

    operator fun invoke(query: Query = VoidQuery, operation: Operation = DefaultOperation): Observable<M> =
            getRepository.get(query, operation)
                    .subscribeOn(executionScheduler)
                    .observeOn(mainThreadScheduler)
}

class GetAllInteractor<M>(private val executionScheduler: Scheduler,
                          private val mainThreadScheduler: Scheduler,
                          private val getRepository: GetRepository<M>) {

    operator fun invoke(query: Query = VoidQuery, operation: Operation = DefaultOperation): Observable<List<M>> =
            getRepository.getAll(query, operation)
                    .subscribeOn(executionScheduler)
                    .observeOn(mainThreadScheduler)
}

class PutInteractor<M>(private val executionScheduler: Scheduler,
                       private val mainThreadScheduler: Scheduler,
                       private val putRepository: PutRepository<M>) {

    operator fun invoke(m: M?, query: Query = VoidQuery, operation: Operation = DefaultOperation): Observable<M> =
            putRepository.put(query, m, operation)
                    .subscribeOn(executionScheduler)
                    .observeOn(mainThreadScheduler)
}

class PutAllInteractor<M>(private val executionScheduler: Scheduler,
                          private val mainThreadScheduler: Scheduler, private val putRepository: PutRepository<M>) {

    operator fun invoke(m: List<M>?, query: Query = VoidQuery, operation: Operation = DefaultOperation): Observable<List<M>> =
            putRepository.putAll(query, m, operation)
                    .subscribeOn(executionScheduler)
                    .observeOn(mainThreadScheduler)
}

class DeleteInteractor(private val executionScheduler: Scheduler,
                       private val mainThreadScheduler: Scheduler, private val deleteRepository: DeleteRepository) {

    operator fun invoke(query: Query = VoidQuery, operation: Operation = DefaultOperation): Completable =
            deleteRepository.delete(query, operation)
                    .subscribeOn(executionScheduler)
                    .observeOn(mainThreadScheduler)
}

class DeleteAllInteractor(private val executionScheduler: Scheduler,
                          private val mainThreadScheduler: Scheduler, private val deleteRepository: DeleteRepository) {

    operator fun invoke(query: Query = VoidQuery, operation: Operation = DefaultOperation): Completable =
            deleteRepository.deleteAll(query, operation)
                    .subscribeOn(executionScheduler)
                    .observeOn(mainThreadScheduler)
}

fun <V> GetRepository<V>.toGetInteractor(executionScheduler: Scheduler = Schedulers.io(),
                                         mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()) =
        GetInteractor(executionScheduler, mainThreadScheduler, this)

fun <V> GetRepository<V>.toGetAllInteractor(executionScheduler: Scheduler = Schedulers.io(),
                                            mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()) = GetAllInteractor(executionScheduler, mainThreadScheduler, this)

fun <V> PutRepository<V>.toPutInteractor(executionScheduler: Scheduler = Schedulers.io(),
                                         mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()) = PutInteractor(executionScheduler, mainThreadScheduler, this)

fun <V> PutRepository<V>.toPutAllInteractor(executionScheduler: Scheduler = Schedulers.io(),
                                            mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()) = PutAllInteractor(executionScheduler, mainThreadScheduler, this)

fun DeleteRepository.toDeleteInteractor(executionScheduler: Scheduler = Schedulers.io(),
                                        mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()) = DeleteInteractor(executionScheduler, mainThreadScheduler, this)

fun DeleteRepository.toDeleteAllInteractor(executionScheduler: Scheduler = Schedulers.io(),
                                           mainThreadScheduler: Scheduler = AndroidSchedulers.mainThread()) = DeleteAllInteractor(executionScheduler, mainThreadScheduler, this)