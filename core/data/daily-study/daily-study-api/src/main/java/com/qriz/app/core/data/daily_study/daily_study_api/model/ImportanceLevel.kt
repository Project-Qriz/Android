package com.qriz.app.core.data.daily_study.daily_study_api.model

enum class ImportanceLevel(val displayName: String) {
    HIGH("상"),
    MEDIUM("중"),
    LOW("하");

    companion object {
        fun fromDisplayName(displayName: String): ImportanceLevel {
            return entries.find { it.displayName == displayName }
                ?: throw IllegalArgumentException("Unknown ImportanceLevel: $displayName")
        }
    }
}
