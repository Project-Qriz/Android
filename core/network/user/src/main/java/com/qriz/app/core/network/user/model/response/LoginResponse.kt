package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** 서버측 요구 사항에 맞춰진 필드명
 * @property id         유저 식별 ID
 * @property userName   사용자 ID
 * @property nickname   유저 성명
 * @property createdAt  가입일
 * */
@Serializable
data class LoginResponse(
    @SerialName("id") val id: Long,
    @SerialName("username") val userName: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("previewTestStatus") val previewTestStatus: String,
)
