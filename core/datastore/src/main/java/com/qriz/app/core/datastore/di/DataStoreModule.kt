package com.qriz.app.core.datastore.di

import android.content.Context
import com.qriz.app.core.datastore.TokenDataStore
import com.qriz.app.core.datastore.crypto.CryptographyUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataStoreModule {

}
