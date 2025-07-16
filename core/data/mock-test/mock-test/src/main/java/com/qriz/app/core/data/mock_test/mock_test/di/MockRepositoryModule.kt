package com.qriz.app.core.data.mock_test.mock_test.di

import com.qriz.app.core.data.mock_test.mock_test.repository.MockTestRepositoryImpl
import com.qriz.app.core.data.mock_test.mock_test_api.repository.MockTestRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface MockRepositoryModule {
    @Binds
    @Singleton
    fun bindsMockRepository(
        mockRepositoryImpl: MockTestRepositoryImpl
    ): MockTestRepository
}
