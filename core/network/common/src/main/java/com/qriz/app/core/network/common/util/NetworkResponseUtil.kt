package com.qriz.app.core.network.common.util

import com.qriz.app.core.network.common.NetworkResponse
import com.qriz.app.core.network.common.exception.QrizNetworkException

fun <T> NetworkResponse<T>.verifyResponseCode(): NetworkResponse<T> =
    if (code == 1) this
    else throw QrizNetworkException(code = code, message = message)
