package com.qriz.app.di

import android.content.Context
import com.qriz.app.datastore.UserDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

    @Provides
    @Singleton
    fun providesUserDataStore(
        @ApplicationContext context: Context
    ): UserDataStore = UserDataStore(context)
}
