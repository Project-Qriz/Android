package com.qriz.app.core.data.user.user.di

import com.qriz.app.core.data.user.user.repository.UserRepositoryImpl
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface UserRepositoryModule {
    @Binds
    @Singleton
    fun bindsUserRepository(
        userRepository: UserRepositoryImpl
    ): UserRepository

}
