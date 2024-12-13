package com.qriz.app.core.data.mapper

import com.qriz.app.core.data.model.user.User
import com.qriz.app.core.network.model.response.user.JoinResponse
import com.qriz.app.core.network.model.response.user.LoginResponse
import java.text.SimpleDateFormat
import java.util.Locale

fun LoginResponse.toDataModel(): User =
    User(
        id = id,
        username = userName,
        createdAt = createdAt,
    )

fun JoinResponse.toDataModel(): User =
    User(
        id = id,
        username = username,
        createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date)
    )
