package com.example.to_do_app.di

import android.content.Context
import com.example.to_do_app.data.alarms.AlarmScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object AlarmSchedulerModule {
    @Provides
    fun provideAlarmScheduler(@ApplicationContext context: Context): AlarmScheduler {return AlarmScheduler(context)
    }
}