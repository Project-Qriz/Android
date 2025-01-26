package com.qriz.app.core.network.common.exception

class QrizNetworkException(
    val code: Int,
    override val message: String = UNKNOWN_ERROR_MESSAGE,
): Exception(message) {
    companion object {
        const val UNKNOWN_ERROR_MESSAGE = "알 수 없는 오류가 발생하였습니다."
    }
}
