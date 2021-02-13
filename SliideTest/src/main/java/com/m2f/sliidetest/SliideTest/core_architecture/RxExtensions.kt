package com.m2f.sliidetest.SliideTest.core_architecture

import android.os.Looper
import androidx.annotation.CheckResult
import io.reactivex.FlowableEmitter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

/**
 * @author Marc Moreno
 * @since 9/1/2019.
 */
infix fun <E : FlowableEmitter<*>> E.canEmitt(block: E.() -> Unit) {
    if (this.isCancelled.not()) {
        block()
    }
}

infix fun <T : Any, E : FlowableEmitter<T>> E.next(block: () -> T) {
    if (this.isCancelled.not()) {
        onNext(block())
    }
}

infix fun <E : ObservableEmitter<*>> E.canEmitt(block: E.() -> Unit) {
    if (this.isDisposed.not()) {
        block()
    }
}

@CheckResult
fun <T> FlowableEmitter<T>.checkMainThread(): Boolean {
    if (Looper.myLooper() != Looper.getMainLooper()) {
        this canEmitt {
            this.onError(
                IllegalStateException(
                    "Expected to be called on the main thread but was " + Thread.currentThread().name
                )
            )
        }
        return false
    }
    return true
}

fun <Type> Observable<Type>.addThreadPolicy(schedulerProvider: SchedulerProvider) =
    this.subscribeOn(schedulerProvider.io()).observeOn(
        schedulerProvider.ui()
    )