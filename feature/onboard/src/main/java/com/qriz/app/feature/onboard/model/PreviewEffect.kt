package com.qriz.app.feature.onboard.model

sealed interface PreviewEffect {
    data object Submit : PreviewEffect
}
