package com.qriz.app.core.datastore.user.mapper

import com.qriz.app.core.datastore.user.model.UserEntity
import com.quiz.app.core.data.user.user_api.model.User

fun UserEntity.toUser(): User {
    return User(
        id = id,
        userId = userId,
        name = name,
        createdAt = createdAt,
        previewTestStatus = previewTestStatus
    )
}

fun User.toUserEntity(): UserEntity {
    return UserEntity(
        id = id,
        userId = userId,
        name = name,
        createdAt = createdAt,
        previewTestStatus = previewTestStatus
    )
}
