package com.qriz.app.core.network.user.di

import com.qriz.app.core.network.user.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object UserApiModule {

    @Provides
    @Singleton
    fun bindsUserApi(
        retrofit: Retrofit
    ): UserApi = retrofit.create(UserApi::class.java)

}
