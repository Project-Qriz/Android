package com.qriz.app.core.network.onboard.di

import com.qriz.app.core.network.onboard.api.OnBoardApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object OnBoardApiModule {

    @Provides
    @Singleton
    fun providesOnBoardApi(
        retrofit: Retrofit
    ): OnBoardApi = retrofit.create(OnBoardApi::class.java)
}
