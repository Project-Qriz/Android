package com.qriz.app.core.domain.usecase.daily_study

import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.PlannedSkill
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.domain.usecase_api.daily_study.GetDailyStudyPlanUseCase
import com.qriz.app.core.model.ApiResult
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

internal class GetDailyStudyPlanUseCaseImpl @Inject constructor(
    private val dailyStudyRepository: DailyStudyRepository,
    private val userRepository: UserRepository
): GetDailyStudyPlanUseCase {
    override fun invoke(): Flow<ApiResult<List<DailyStudyPlan>>> =
        userRepository.getUserFlow()
            .map { it.previewTestStatus.isNeedPreviewTest() }
            .distinctUntilChanged()
            .flatMapLatest { isNeedPreviewTest ->
                if (isNeedPreviewTest) {
                    flowOf(fakeDailyStudyPlan)
                } else {
                    dailyStudyRepository.getDailyStudyPlanFlow()
                }
            }

    companion object {
        private val fakeDailyStudyPlan = ApiResult.Success(
            listOf(
                DailyStudyPlan(
                    id = 0,
                    completed = false,
                    planDate = LocalDate.now(),
                    completionDate = null,
                    plannedSkills = listOf(
                        PlannedSkill(
                            id = 0,
                            type = "SQL 기본",
                            keyConcept = "WHERE 절",
                            description = ""
                        ),
                        PlannedSkill(
                            id = 0,
                            type = "SQL 기본",
                            keyConcept = "WHERE 절",
                            description = ""
                        )
                    ),
                    reviewDay = false,
                    comprehensiveReviewDay = false
                )
            ),
        )
    }

}
