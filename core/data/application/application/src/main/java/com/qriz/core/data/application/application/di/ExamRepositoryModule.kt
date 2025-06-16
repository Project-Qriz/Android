package com.qriz.core.data.application.application.di

import com.qriz.app.core.data.application.application_api.repository.ExamRepository
import com.qriz.core.data.application.application.ExamRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface ExamRepositoryModule {
    @Binds
    @Singleton
    fun bindsExamRepository(examRepositoryImpl: ExamRepositoryImpl): ExamRepository
}
