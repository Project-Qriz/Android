package com.qriz.app.core.domain.usecase.clip

import com.qriz.app.core.data.clip.clip_api.repository.ClipRepository
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.domain.usecase_api.clip.GetClipDaysUseCase
import com.qriz.app.core.model.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class GetClipDaysUseCaseImpl @Inject constructor(
    private val dailyStudyRepository: DailyStudyRepository,
    private val clipRepository: ClipRepository
): GetClipDaysUseCase {
    override fun invoke(): Flow<ApiResult<List<String>>> = dailyStudyRepository.planUpdateFlow
        .onStart { emit(Unit) }
        .flatMapLatest {
            clipRepository.getClipDays()
        }
}
