package com.qriz.app.core.data.application.application_api.model

enum class DdayType {
    BEFORE,
    AFTER;

    companion object {
        fun fromStatus(status: String): DdayType {
            return when (status) {
                "BEFORE" -> BEFORE
                "AFTER" -> AFTER
                else -> error("Unknown dday status: $status")
            }
        }
    }
}
