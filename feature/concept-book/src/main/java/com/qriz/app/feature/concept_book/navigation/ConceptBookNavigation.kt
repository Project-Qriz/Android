package com.qriz.app.feature.concept_book.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.feature.concept_book.ConceptBookScreen

fun NavController.navigateToConceptBook(navOptions: NavOptions) {
    navigate(MainTabRoute.ConceptBook, navOptions)
}

fun NavGraphBuilder.conceptBookNavGraph(
    onShowSnackbar: (String) -> Unit,
) {
    composable<MainTabRoute.ConceptBook> {
        ConceptBookScreen(
            onShowSnackBar = onShowSnackbar,
        )
    }
}
