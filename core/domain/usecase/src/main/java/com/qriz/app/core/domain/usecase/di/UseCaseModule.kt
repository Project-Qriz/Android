package com.qriz.app.core.domain.usecase.di

import com.qriz.app.core.domain.usecase.SubmitPreviewTestUseCaseImpl
import com.qriz.app.core.domain.usecase_api.onboard.SubmitPreviewTestUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface UseCaseModule {
    @Binds
    fun bindsSubmitPreviewTestUseCase(
        impl: SubmitPreviewTestUseCaseImpl
    ): SubmitPreviewTestUseCase
}
