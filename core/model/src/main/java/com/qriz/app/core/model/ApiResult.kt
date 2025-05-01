package com.qriz.app.core.model

import java.io.IOException

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T): ApiResult<T>

    data class Failure(val code: Int, val message: String): ApiResult<Nothing>

    data class NetworkError(val exception: IOException): ApiResult<Nothing>

    data class UnknownError(val throwable: Throwable?): ApiResult<Nothing>
}

inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> = when (this) {
    is ApiResult.Success -> ApiResult.Success(transform(data))
    is ApiResult.Failure -> this
    is ApiResult.NetworkError -> this
    is ApiResult.UnknownError -> this
}
