package com.qriz.app.core.network.user.mapper

import com.qriz.app.core.network.user.model.response.JoinResponse
import com.qriz.app.core.network.user.model.response.LoginResponse
import com.quiz.app.core.data.user.user_api.model.User
import java.text.SimpleDateFormat
import java.util.Locale

fun LoginResponse.toDataModel(): User =
    User(
        id = id,
        username = userName,
        createdAt = createdAt,
    )

//TODO : SimpleDateFormat부분 Util class로 뽑기
fun JoinResponse.toDataModel(): User =
    User(
        id = id,
        username = username,
        createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date)
    )
