package com.qriz.app.core.network.mock_test.di

import com.qriz.app.core.network.mock_test.api.MockTestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object MockTestApiModule {

    @Provides
    @Singleton
    fun providesMockTestApi(
        retrofit: Retrofit
    ): MockTestApi = retrofit.create(MockTestApi::class.java)
}
