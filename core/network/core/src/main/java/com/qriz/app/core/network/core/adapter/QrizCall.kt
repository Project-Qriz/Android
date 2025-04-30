package com.qriz.app.core.network.core.adapter

import com.qriz.app.core.model.ApiResult
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type

@Suppress("UNCHECKED_CAST")
class QrizCall<T>(
    private val call: Call<T>,
    private val successType: Type,
) : Call<ApiResult<T>> {

    val json = Json { ignoreUnknownKeys = true }

    override fun clone(): Call<ApiResult<T>> = QrizCall(
        call.clone(),
        successType
    )

    override fun execute(): Response<ApiResult<T>> {
        throw UnsupportedOperationException("QrizCall doesn't support execute")
    }

    override fun isExecuted(): Boolean = call.isExecuted

    override fun cancel() = call.cancel()

    override fun isCanceled(): Boolean = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                callback.onResponse(
                    this@QrizCall,
                    Response.success(response.toApiResult())
                )
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                callback.onResponse(
                    this@QrizCall,
                    Response.success(throwable.toApiResult())
                )
            }

            private fun Throwable?.toApiResult(): ApiResult<Nothing> = when (this) {
                is IOException -> {
                    ApiResult.NetworkError(exception = this)
                }

                else -> ApiResult.UnknownError(throwable = this)
            }

            private fun Response<T>.toApiResult(): ApiResult<T> {
                return if (isSuccessful) {
                    val body = body()
                    if (successType == Unit::class.java) {
                        ApiResult.Success(Unit as T)
                    } else if (body != null) {
                        ApiResult.Success(body)
                    } else {
                        ApiResult.UnknownError(IllegalStateException(INVALID_RESPONSE))
                    }
                } else {
                    val errorBody = errorBody()?.string()
                    if (code() == 400 && errorBody != null) {
                        val errorResponse =
                            json.decodeFromString<InternalFailureResponse>(errorBody)
                        ApiResult.Failure(
                            code = errorResponse.code,
                            message = errorResponse.msg
                        )
                    } else {
                        ApiResult.UnknownError(null)
                    }
                }
            }
        })
    }

    @Serializable
    private data class InternalFailureResponse(val code: Int, val msg: String)

    companion object {
        const val INVALID_RESPONSE = "유효하지 않은 응답입니다. API 선언 부분을 확인해주세요."
    }
}
