package com.qriz.app.core.domain.usecase_api.clip

import com.qriz.app.core.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface GetClipSessionsUseCase {
    operator fun invoke(): Flow<ApiResult<List<String>>>
}
