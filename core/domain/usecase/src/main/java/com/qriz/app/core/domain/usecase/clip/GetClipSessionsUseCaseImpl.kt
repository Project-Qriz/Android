package com.qriz.app.core.domain.usecase.clip

import com.qriz.app.core.data.clip.clip_api.repository.ClipRepository
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.data.mock_test.mock_test_api.repository.MockTestRepository
import com.qriz.app.core.domain.usecase_api.clip.GetClipDaysUseCase
import com.qriz.app.core.domain.usecase_api.clip.GetClipSessionsUseCase
import com.qriz.app.core.model.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class GetClipSessionsUseCaseImpl @Inject constructor(
    private val mockTestRepository: MockTestRepository,
    private val clipRepository: ClipRepository
): GetClipSessionsUseCase {
    override fun invoke(): Flow<ApiResult<List<String>>> = mockTestRepository
        .mockTestSessions
        .map { /* session 변경 이벤트만 받음 */ }
        .onStart { emit(Unit) }
        .flatMapLatest {
            clipRepository.getClipSessions()
        }
}
