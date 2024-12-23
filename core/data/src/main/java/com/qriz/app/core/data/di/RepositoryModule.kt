package com.qriz.app.core.data.di

import com.qriz.app.core.data.repository.OnBoardRepository
import com.qriz.app.core.data.repository.OnBoardRepositoryImpl
import com.qriz.app.core.data.repository.UserRepository
import com.qriz.app.core.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface RepositoryModule {
    @Binds
    @Singleton
    fun bindsUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    fun bindsOnBoardRepository(
       onBoardingRepository: OnBoardRepositoryImpl
    ): OnBoardRepository
}
