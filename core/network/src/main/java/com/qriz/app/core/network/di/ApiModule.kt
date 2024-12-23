package com.qriz.app.core.network.di

import com.qriz.app.core.network.api.OnBoardApi
import com.qriz.app.core.network.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

    @Provides
    @Singleton
    fun bindsUserApi(
        retrofit: Retrofit
    ): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun providesOnBoardApi(
        retrofit: Retrofit
    ): OnBoardApi = retrofit.create(OnBoardApi::class.java)
}
