package com.qriz.app.core.network.core.di

import com.qriz.app.core.network.core.BuildConfig
import com.qriz.app.core.network.core.adapter.QrizCallAdapterFactory
import com.qriz.app.core.network.core.interceptor.AuthInterceptor
import com.qriz.app.core.network.core.interceptor.ResponseConvertInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun providesRetrofit(
        json: Json,
        okHttpClient: OkHttpClient,
        callAdapterFactory: QrizCallAdapterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(callAdapterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        responseConvertInterceptor: ResponseConvertInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(logging)
        builder.addInterceptor(authInterceptor)
        builder.addInterceptor(responseConvertInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun providesJson() = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }
}
