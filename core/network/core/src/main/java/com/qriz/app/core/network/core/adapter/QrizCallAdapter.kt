package com.qriz.app.core.network.core.adapter

import com.qriz.app.core.model.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class QrizCallAdapter<R>(private val responseType: Type) : CallAdapter<R, Call<ApiResult<R>>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<R>): Call<ApiResult<R>> = QrizCall(call, responseType)
}
