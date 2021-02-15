package com.m2f.sliidetest.SliideTest.di

import com.m2f.sliidetest.SliideTest.core_architecture.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RxModule {

    @Provides
    @Singleton
    fun providesMainScheduler(): SchedulerProvider = object : SchedulerProvider {
        override fun ui(): Scheduler {
            return AndroidSchedulers.mainThread()
        }

        override fun computation(): Scheduler {
            return Schedulers.computation()
        }

        override fun trampoline(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun newThread(): Scheduler {
            return Schedulers.newThread()
        }

        override fun io(): Scheduler {
            return Schedulers.io()
        }
    }
}