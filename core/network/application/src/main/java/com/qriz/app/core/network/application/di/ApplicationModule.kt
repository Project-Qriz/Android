package com.qriz.app.core.network.application.di

import com.qriz.app.core.network.application.api.ApplicationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun providesApplicationApi(
        retrofit: Retrofit
    ) = retrofit.create(ApplicationApi::class.java)
}
