package com.qriz.app.feature.onboard.preview.model

sealed interface PreviewEffect {
    data object Submit : PreviewEffect
}
