package com.qriz.app.core.network.core.adapter

import com.qriz.app.core.model.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

class QrizCallAdapterFactory @Inject constructor() : CallAdapter.Factory() {
    override fun get(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        if (getRawType(type) != Call::class.java) {
            return null
        }

        check(type is ParameterizedType) {
            "return type must be parameterized as Call<Result<Foo>> or Call<Result<out Foo>>"
        }

        val responseType = getParameterUpperBound(
            0,
            type
        )

        if (getRawType(responseType) != ApiResult::class.java) {
            return null
        }

        check(responseType is ParameterizedType) {
            "Response must be parameterized as ApiResult<Foo> or ApiResult<out Foo>"
        }

        val successBodyType = getParameterUpperBound(
            0,
            responseType
        )

        return QrizCallAdapter<Any>(successBodyType)
    }

}
