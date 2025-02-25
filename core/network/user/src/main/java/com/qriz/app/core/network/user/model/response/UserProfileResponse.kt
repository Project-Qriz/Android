package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//TODO : 서버 스펙 정해지면 수정 예정
@Serializable
data class UserProfileResponse(
    @SerialName("id") val id: Long,
    @SerialName("username") val username: String,
    @SerialName("name") val name: String,
    @SerialName("date") val date: String,
)
