package com.qriz.app.core.data.onboard.onboard.di

import com.qriz.app.core.data.onboard.onboard.repository.OnBoardRepositoryImpl
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface OnBoardRepositoryModule {
    @Binds
    @Singleton
    fun bindsOnBoardRepository(
        repository: OnBoardRepositoryImpl
    ): OnBoardRepository
}