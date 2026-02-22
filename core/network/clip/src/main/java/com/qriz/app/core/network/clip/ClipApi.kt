package com.qriz.app.core.network.clip

import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.network.clip.model.ClipDaysResponse
import com.qriz.app.core.network.clip.model.ClipDetailResponse
import com.qriz.app.core.network.clip.model.ClipFilterResponse
import com.qriz.app.core.network.clip.model.ClipSessionsResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ClipApi {
    @GET("/api/v1/clips/days")
    suspend fun getClipDays(): ApiResult<ClipDaysResponse>

    @GET("/api/v1/clips/sessions")
    suspend fun getClipSessions(): ApiResult<ClipSessionsResponse>

    @GET("/api/v1/clips")
    suspend fun getClips(
        @Query("category") category: Int,
        @Query("testInfo") testInfo: String? = null,
        @Query("onlyIncorrect") onlyIncorrect: Boolean = false,
        @Query("keyConcepts") keyConcepts: List<String>? = null,
    ): ApiResult<List<ClipFilterResponse>>

    @GET("/api/v1/clips/{clipId}/detail")
    suspend fun getClipDetail(
        @Path("clipId") clipId: Long
    ): ApiResult<ClipDetailResponse>

    @DELETE("/api/v1/clips/{clipId}")
    suspend fun deleteClip(
        @Path("clipId") clipId: Long
    ): ApiResult<Unit>
}
