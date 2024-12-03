package com.qriz.app.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Main : Route

    @Serializable
    data object SignIn : Route

    @Serializable
    data object SignUp : Route
}
