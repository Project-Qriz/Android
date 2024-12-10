package com.qriz.app.feature.sign.model

sealed interface SignUpEffect {
    data object SignUpComplete : SignUpEffect
}
