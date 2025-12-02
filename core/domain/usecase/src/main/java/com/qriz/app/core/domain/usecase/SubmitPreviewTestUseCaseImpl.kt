package com.qriz.app.core.domain.usecase

import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.domain.usecase_api.onboard.SubmitPreviewTestUseCase
import com.qriz.app.core.model.ApiResult
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class SubmitPreviewTestUseCaseImpl @Inject constructor(
    private val onboardRepository: OnBoardRepository,
    private val userRepository: UserRepository,
) : SubmitPreviewTestUseCase {
    override suspend fun invoke(answer: Map<Long, Option>): ApiResult<Unit> {
        val result = onboardRepository.submitPreviewTest(answer)
        if (result is ApiResult.Success) {
            fetchUserWithRetry()
        }

        return result
    }

    private fun fetchUserWithRetry(maxRetries: Int = 3) = CoroutineScope(Job() + Dispatchers.IO).launch{
        repeat(maxRetries) { attempt ->
            val result = userRepository.fetchUser()
            if (result is ApiResult.Success || attempt == maxRetries - 1) {
                return@launch
            }
        }
    }
}
