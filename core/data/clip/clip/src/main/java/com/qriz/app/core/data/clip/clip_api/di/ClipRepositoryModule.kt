package com.qriz.app.core.data.clip.clip_api.di

import com.qriz.app.core.data.clip.clip_api.repository.ClipRepository
import com.qriz.app.core.data.clip.clip_api.repository.ClipRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface ClipRepositoryModule {
    @Binds
    @Singleton
    fun bindsClipRepository(
        clipRepository: ClipRepositoryImpl
    ): ClipRepository
}
