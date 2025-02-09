package com.qriz.app.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Main : Route

    @Serializable
    data object SignIn : Route

    @Serializable
    data object SignUp : Route

    @Serializable
    data object FindId : Route

    @Serializable
    data object FindPasswordAuth : Route

    @Serializable
    data class Guide(
        val title: String,
        val subTitle: String,
        val image: Int,
        val buttonText: String,
        val route: String,
    ) : Route

    @Serializable
    data object ConceptCheck : Route

    @Serializable
    data object Preview : Route
}
