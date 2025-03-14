package com.quiz.app.core.data.user.user_api.model

import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus.NOT_STARTED
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus.PREVIEW_COMPLETED
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus.PREVIEW_SKIPPED
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus.SURVEY_COMPLETED

/**
 * 서비스 사용 유저
 * @property id                     유저 식별 ID
 * @property userId                 유저 ID
 * @property name                   유저 성명
 * @property createdAt              가입일
 * @property previewTestStatus      프리뷰 테스트 진행 상태
 */
data class User(
    val id: Long,
    val userId: String,
    val name: String,
    val createdAt: String,
    val previewTestStatus: PreviewTestStatus
) {
    val isSurveyNeeded
        get() = previewTestStatus == NOT_STARTED

    companion object {
        val Default = User(
            id = -1,
            userId = "",
            name = "",
            createdAt = "",
            previewTestStatus = NOT_STARTED
        )
    }
}

/**
 * 유저의 프리뷰 테스트 진행 상태
 * @property NOT_STARTED            회원 가입 후 최초 로그인 상태 (설문 + 프리뷰 테스트 실시 필요)
 * @property PREVIEW_SKIPPED        설문조사에서 아무것도 모른다 선택 상태 (프리뷰 테스트 실시 필요)
 * @property SURVEY_COMPLETED       설문조사에서 선택한 개념이 존재 + 프리뷰 테스트 미실시 상태 (프리뷰 테스트 실시 필요)
 * @property PREVIEW_COMPLETED      프리뷰 완료 상태
 */
enum class PreviewTestStatus {
    NOT_STARTED,
    PREVIEW_SKIPPED,
    SURVEY_COMPLETED,
    PREVIEW_COMPLETED;

    fun isNeedPreviewTest() = this != PREVIEW_COMPLETED
}
