package com.qriz.app.core.network.model.request.user

data class JoinRequest(
    val username: String,
    val password: String,
    val nickname: String,
    val email: String,
)
