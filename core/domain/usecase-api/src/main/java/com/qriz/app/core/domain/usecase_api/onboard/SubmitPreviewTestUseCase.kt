package com.qriz.app.core.domain.usecase_api.onboard

import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.model.ApiResult

interface SubmitPreviewTestUseCase {
    suspend operator fun invoke(answer: Map<Long, Option>): ApiResult<Unit>
}
