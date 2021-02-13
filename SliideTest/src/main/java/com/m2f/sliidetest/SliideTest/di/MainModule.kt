package com.m2f.sliidetest.SliideTest.di

import android.content.Context
import android.content.SharedPreferences
import com.m2f.sliidetest.SliideTest.core_architecture.SchedulerProvider
import com.m2f.sliidetest.SliideTest.core_architecture.repository.datasource.storage.DeviceStorageDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun providesSharedPreferences(@ApplicationContext context: Context): SharedPreferences = context.getSharedPreferences("MAIN", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providesDeviceStorageDatasource(sharedPreferences: SharedPreferences): DeviceStorageDataSource<String> =
        DeviceStorageDataSource(sharedPreferences)

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
