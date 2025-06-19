package com.qriz.app.core.network.daily_study.di

import com.qriz.app.core.network.daily_study.DailyStudyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DailyStudyModule {
    @Provides
    @Singleton
    fun providesDailyStudyApi(retrofit: Retrofit): DailyStudyApi =
        retrofit.create(DailyStudyApi::class.java)
}
