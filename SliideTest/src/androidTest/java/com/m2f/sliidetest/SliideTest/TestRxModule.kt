package com.m2f.sliidetest.SliideTest

import com.m2f.sliidetest.SliideTest.core_architecture.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TestRxModule {

    @Provides
    @Singleton
    fun providesMainScheduler(): SchedulerProvider = object : SchedulerProvider {
        override fun ui(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun computation(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun trampoline(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun newThread(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun io(): Scheduler {
            return Schedulers.trampoline()
        }
    }
}