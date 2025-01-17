package com.qriz.app.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface Route

sealed interface SignRoute : Route {
    @Serializable
    data object SignIn : SignRoute

    @Serializable
    data object SignUp : SignRoute

    @Serializable
    data object FindId : SignRoute

    @Serializable
    data object FindPasswordAuth : SignRoute

    @Serializable
    data object ResetPassword : SignRoute
}

sealed interface OnBoardRoute : Route {
    @Serializable
    data object ConceptCheckGuide : OnBoardRoute

    @Serializable
    data object ConceptCheck : OnBoardRoute

    @Serializable
    data object PreviewGuide : OnBoardRoute

    @Serializable
    data object Preview : OnBoardRoute

    @Serializable
    data object PreviewResult : OnBoardRoute

    @Serializable
    data object WelcomeGuide : OnBoardRoute
}

sealed interface MainTabRoute : Route {
    @Serializable
    data object Home : MainTabRoute

    @Serializable
    data object ConceptBook : MainTabRoute

    @Serializable
    data object WrongAnswersNote : MainTabRoute

    @Serializable
    data object MyPage : MainTabRoute
}
