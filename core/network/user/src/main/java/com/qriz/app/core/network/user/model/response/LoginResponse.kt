package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** 서버측 요구 사항에 맞춰진 필드명
 * @property name 유저 성명
 * */
@Serializable
data class LoginResponse(
    @SerialName("name") val userName: String,
)
