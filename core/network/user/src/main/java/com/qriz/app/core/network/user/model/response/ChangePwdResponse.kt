package com.qriz.app.core.network.user.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//TODO : 굳이 넘겨받지 않아도 되는 정보임으로 회의 건의
@Serializable
data class ChangePwdResponse(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
)