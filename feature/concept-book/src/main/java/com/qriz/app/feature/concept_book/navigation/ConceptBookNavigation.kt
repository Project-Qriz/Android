package com.qriz.app.feature.concept_book.navigation

import androidx.compose.foundation.layout.PaddingValues
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
    padding: PaddingValues,
    popBackStack: () -> Unit,
    onShowSnackBar: (String) -> Unit,
) {
    composable<MainTabRoute.ConceptBook> {
        ConceptBookScreen(
//            padding = padding,
//            popBackStack = popBackStack,
            onShowSnackBar = onShowSnackBar,
        )
    }
}
