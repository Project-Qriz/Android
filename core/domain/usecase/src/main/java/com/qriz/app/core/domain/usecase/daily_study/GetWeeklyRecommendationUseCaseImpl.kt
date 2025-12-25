package com.qriz.app.core.domain.usecase.daily_study

import com.qriz.app.core.data.daily_study.daily_study_api.model.ImportanceLevel
import com.qriz.app.core.data.daily_study.daily_study_api.model.WeeklyRecommendation
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.domain.usecase_api.daily_study.GetWeeklyRecommendationUseCase
import com.qriz.app.core.model.ApiResult
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetWeeklyRecommendationUseCaseImpl @Inject constructor(
    private val dailyStudyRepository: DailyStudyRepository,
    private val userRepository: UserRepository
): GetWeeklyRecommendationUseCase {
    override fun invoke(): Flow<ApiResult<List<WeeklyRecommendation>>> =
        userRepository.getUserFlow()
            .map { it.previewTestStatus.isNeedPreviewTest() }
            .distinctUntilChanged()
            .flatMapLatest { isNeedPreviewTest ->
                if (isNeedPreviewTest) {
                    flowOf(fakeWeeklyRecommendation)
                } else {
                    dailyStudyRepository.getWeeklyRecommendation()
                }
            }

    companion object {
        private val fakeWeeklyRecommendation = ApiResult.Success(
            listOf(
                WeeklyRecommendation(
                    skillId = 1,
                    keyConcepts = "데이터 모델의 이해",
                    description = "",
                    frequency = 1,
                    incorrectRate = null,
                    importanceLevel = ImportanceLevel.HIGH,
                ),
                WeeklyRecommendation(
                    skillId = 1,
                    keyConcepts = "SELECT 문",
                    description = "",
                    frequency = 1,
                    incorrectRate = null,
                    importanceLevel = ImportanceLevel.LOW,
                )
            )
        )
    }
}
