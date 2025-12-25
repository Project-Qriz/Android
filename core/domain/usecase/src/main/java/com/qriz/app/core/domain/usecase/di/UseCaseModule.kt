package com.qriz.app.core.domain.usecase.di

import com.qriz.app.core.domain.usecase.daily_study.GetDailyStudyPlanUseCaseImpl
import com.qriz.app.core.domain.usecase.daily_study.GetWeeklyRecommendationUseCaseImpl
import com.qriz.app.core.domain.usecase.onboard.SubmitPreviewTestUseCaseImpl
import com.qriz.app.core.domain.usecase_api.daily_study.GetDailyStudyPlanUseCase
import com.qriz.app.core.domain.usecase_api.daily_study.GetWeeklyRecommendationUseCase
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

    @Binds
    fun bindsGetDailyStudyPlanUseCase(
        impl: GetDailyStudyPlanUseCaseImpl
    ): GetDailyStudyPlanUseCase

    @Binds
    fun bindsGetWeeklyRecommendationUseCase(
        impl: GetWeeklyRecommendationUseCaseImpl
    ): GetWeeklyRecommendationUseCase
}
