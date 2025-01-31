package com.qriz.app.core.datastore.user.model

import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus
import kotlinx.serialization.Serializable

@Serializable
data class UserEntity(
    val id: Long,
    val userId: String,
    val name: String,
    val createdAt: String,
    val previewTestStatus: PreviewTestStatus
)
