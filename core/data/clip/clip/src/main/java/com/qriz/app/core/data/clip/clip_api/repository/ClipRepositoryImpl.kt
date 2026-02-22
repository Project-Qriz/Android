package com.qriz.app.core.data.clip.clip_api.repository

import com.qriz.app.core.data.clip.clip_api.mapper.toDomain
import com.qriz.app.core.data.clip.clip_api.model.Clip
import com.qriz.app.core.data.clip.clip_api.model.ClipDetail
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.map
import com.qriz.app.core.network.clip.ClipApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class ClipRepositoryImpl @Inject constructor(
    private val clipApi: ClipApi
) : ClipRepository {

    private val clipDetailCache = mutableMapOf<Long, ClipDetail>()

    override suspend fun getClipDays(): Flow<ApiResult<List<String>>> = flow {
        emit(clipApi.getClipDays().map { it.days })
    }

    override suspend fun getClipSessions(): Flow<ApiResult<List<String>>> = flow {
        emit(clipApi.getClipSessions().map { it.sessions })
    }

    override suspend fun getDailyStudyClips(
        testInfo: String?,
        onlyIncorrect: Boolean,
        keyConcepts: List<String>?
    ): ApiResult<List<Clip>> {
        return clipApi.getClips(
            category = 2,
            testInfo = testInfo,
            onlyIncorrect = onlyIncorrect,
            keyConcepts = keyConcepts
        ).map { responses ->
            responses.map { it.toDomain() }
        }
    }

    override suspend fun getMockTestClips(
        testInfo: String?,
        onlyIncorrect: Boolean,
        keyConcepts: List<String>?
    ): ApiResult<List<Clip>> {
        return clipApi.getClips(
            category = 3,
            testInfo = testInfo,
            onlyIncorrect = onlyIncorrect,
            keyConcepts = keyConcepts
        ).map { responses ->
            responses.map { it.toDomain() }
        }
    }

    override suspend fun getClipDetail(clipId: Long): ApiResult<ClipDetail> {
        if (clipDetailCache[clipId] != null) {
            return ApiResult.Success(clipDetailCache[clipId]!!)
        }

        return clipApi.getClipDetail(clipId).map {
            val result = it.toDomain()
            clipDetailCache[clipId] = result
            result
        }
    }

    override suspend fun deleteClip(clipId: Long): ApiResult<Unit> {
        return clipApi.deleteClip(clipId)
    }
}
