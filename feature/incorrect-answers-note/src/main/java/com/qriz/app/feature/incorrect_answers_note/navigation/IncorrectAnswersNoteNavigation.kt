package com.qriz.app.feature.incorrect_answers_note.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.feature.incorrect_answers_note.IncorrectAnswersNoteScreen

fun NavController.navigateToIncorrectAnswersNote(navOptions: NavOptions) {
    navigate(MainTabRoute.IncorrectAnswersNote, navOptions)
}

fun NavGraphBuilder.incorrectAnswersNoteNavGraph(
    padding: PaddingValues,
    popBackStack: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    composable<MainTabRoute.IncorrectAnswersNote> {
        IncorrectAnswersNoteScreen(
//            padding = padding,
//            popBackStack = popBackStack,
            onShowSnackBar = onShowSnackBar,
        )
    }
}
