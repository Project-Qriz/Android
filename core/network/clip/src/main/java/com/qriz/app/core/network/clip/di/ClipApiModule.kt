package com.qriz.app.core.network.clip.di

import com.qriz.app.core.network.clip.ClipApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ClipApiModule {

    @Provides
    @Singleton
    fun bindsClipApi(
        retrofit: Retrofit
    ): ClipApi = retrofit.create(ClipApi::class.java)

}