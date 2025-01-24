package com.qriz.app.core.network.user.mapper

import com.qriz.app.core.network.user.model.response.JoinResponse
import com.qriz.app.core.network.user.model.response.LoginResponse
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus
import com.quiz.app.core.data.user.user_api.model.User

fun LoginResponse.toDataModel(): User =
    User(
        id = id,
        userId = userName,
        name = nickname,
        createdAt = createdAt,
        previewTestStatus = PreviewTestStatus.valueOf(previewTestStatus)
    )

fun JoinResponse.toDataModel(): User =
    User(
        id = id,
        userId = username,
        name = name,
        createdAt = date,
        previewTestStatus = PreviewTestStatus.NOT_STARTED
    )
