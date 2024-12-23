package com.qriz.app.core.network.model.response.user

import java.time.LocalDateTime

data class JoinResponse(
    val id: Long,
    val username: String,
    val name: String,
    val date: LocalDateTime
)
