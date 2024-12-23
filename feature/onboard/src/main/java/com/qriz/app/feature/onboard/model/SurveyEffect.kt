package com.qriz.app.feature.onboard.model

sealed interface SurveyEffect {
    data object Complete : SurveyEffect

    data class Error(val message: String) : SurveyEffect
}
