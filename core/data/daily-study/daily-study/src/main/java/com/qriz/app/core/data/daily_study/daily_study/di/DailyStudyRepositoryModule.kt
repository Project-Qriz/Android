package com.qriz.app.core.data.daily_study.daily_study.di

import com.qriz.app.core.data.daily_study.daily_study.DailyStudyRepositoryImpl
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface DailyStudyRepositoryModule {
    @Binds
    @Singleton
    fun bindsDailyStudyRepository(
        dailyStudyRepositoryImpl: DailyStudyRepositoryImpl
    ): DailyStudyRepository
}
