package com.qriz.core.data.token.token.di

import com.qriz.core.data.token.token.repository.TokenRepositoryImpl
import com.qriz.core.data.token.token_api.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface TokenRepositoryModule {

    @Binds
    @Singleton
    fun bindsTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl
    ): TokenRepository
}
