package com.qriz.app.core.data.user.user.mapper

import com.qriz.app.core.network.user.model.response.UserProfileResponse
import com.quiz.app.core.data.user.user_api.model.LoginType
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus
import com.quiz.app.core.data.user.user_api.model.User

fun UserProfileResponse.toDataModel(loginType: LoginType): User =
    User(
        userId = userId,
        name = name,
        email = email,
        previewTestStatus = PreviewTestStatus.valueOf(previewStatus),
        loginType = loginType
    )
