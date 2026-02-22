package com.qriz.app.core.data.clip.clip_api.repository

import com.qriz.app.core.data.clip.clip_api.model.Clip
import com.qriz.app.core.data.clip.clip_api.model.ClipDetail
import com.qriz.app.core.model.ApiResult
import kotlinx.coroutines.flow.Flow

interface ClipRepository {
    /**
     * 오답노트가 있는 날짜 목록 조회
     */
    suspend fun getClipDays(): Flow<ApiResult<List<String>>>

    /**
     * 오답노트 세션 목록 조회
     */
    suspend fun getClipSessions(): Flow<ApiResult<List<String>>>

    /**
     * Daily Study 오답노트 목록 조회
     *
     * @param testInfo 시험 정보 필터
     * @param onlyIncorrect 오답만 조회 여부
     * @param keyConcepts 핵심 개념 필터 목록
     */
    suspend fun getDailyStudyClips(
        testInfo: String? = null,
        onlyIncorrect: Boolean = false,
        keyConcepts: List<String>? = null
    ): ApiResult<List<Clip>>

    /**
     * Mock Test 오답노트 목록 조회
     *
     * @param testInfo 시험 정보 필터
     * @param onlyIncorrect 오답만 조회 여부
     * @param keyConcepts 핵심 개념 필터 목록
     */
    suspend fun getMockTestClips(
        testInfo: String? = null,
        onlyIncorrect: Boolean = false,
        keyConcepts: List<String>? = null
    ): ApiResult<List<Clip>>

    /**
     * 오답노트 문제 상세 조회
     *
     * @param clipId 클립 ID
     */
    suspend fun getClipDetail(clipId: Long): ApiResult<ClipDetail>

    /**
     * 오답노트 삭제
     *
     * @param clipId 삭제할 클립 ID
     */
    suspend fun deleteClip(clipId: Long): ApiResult<Unit>
}
