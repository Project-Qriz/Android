package com.qriz.app.core.network.model.response.user

import java.time.LocalDateTime

data class FindIdResponse(
    val username: String,
    val date: LocalDateTime
)
