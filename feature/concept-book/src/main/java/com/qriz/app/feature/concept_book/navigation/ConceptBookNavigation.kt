package com.qriz.app.feature.concept_book.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qriz.app.core.navigation.route.ConceptBookRoute
import com.qriz.app.core.navigation.route.MainTabRoute
import com.qriz.app.feature.concept_book.ConceptBookScreen
import com.qriz.app.feature.concept_book.detail.ConceptBookDetailScreen
import com.qriz.app.feature.concept_book.list.ConceptBookListScreen

fun NavController.navigateToConceptBook(navOptions: NavOptions? = null) {
    navigate(
        MainTabRoute.ConceptBook,
        navOptions
    )
}

fun NavController.navigateToConceptBookList(
    categoryName: String,
    navOptions: NavOptions? = null,
) {
    navigate(
        ConceptBookRoute.ConceptBookList(categoryName),
        navOptions,
    )
}

fun NavController.navigateToConceptBookDetail(
    conceptBookId: Long,
    navOptions: NavOptions? = null,
) {
    navigate(
        ConceptBookRoute.ConceptBookDetail(conceptBookId),
        navOptions,
    )
}

fun NavGraphBuilder.conceptBookNavGraph(
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    moveToConceptBookList: (String) -> Unit,
    moveToConceptBookDetail: (Long) -> Unit,
) {
    composable<MainTabRoute.ConceptBook> {
        ConceptBookScreen(
            onShowSnackBar = onShowSnackbar,
            moveToConceptBookList = moveToConceptBookList,
        )
    }

    composable<ConceptBookRoute.ConceptBookList> {
        val route = it.toRoute<ConceptBookRoute.ConceptBookList>()
        ConceptBookListScreen(
            categoryName = route.categoryName,
            moveToConceptBookDetail = moveToConceptBookDetail,
            moveToBack = onBack,
        )
    }

    composable<ConceptBookRoute.ConceptBookDetail> {
        val route = it.toRoute<ConceptBookRoute.ConceptBookDetail>()
        ConceptBookDetailScreen(
            conceptBookId = route.conceptBookId,
            onBack = onBack,
        )
    }
}
