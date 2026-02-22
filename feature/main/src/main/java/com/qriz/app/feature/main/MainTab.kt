package com.qriz.app.feature.main

import androidx.compose.runtime.Composable
import com.qriz.app.core.navigation.route.MainTabRoute

enum class MainTab(
    val labelResId: Int,
    val contentDescriptionResId: Int,
    val iconResId: Int,
    val selectedIconResId: Int,
    val route: MainTabRoute,
) {
    HOME(
        labelResId = R.string.home,
        contentDescriptionResId = R.string.home,
        iconResId = R.drawable.home_icon,
        selectedIconResId = R.drawable.home_selected_icon,
        route = MainTabRoute.Home,
    ),

    CONCEPT_BOOK(
        labelResId = R.string.concept_book,
        contentDescriptionResId = R.string.concept_book,
        iconResId = R.drawable.concept_book_icon,
        selectedIconResId = R.drawable.concept_book_selected_icon,
        route = MainTabRoute.ConceptBook,
    ),

    INCORRECT_ANSWERS_NOTE(
        labelResId = R.string.incorrect_answer_note,
        contentDescriptionResId = R.string.incorrect_answer_note,
        iconResId = R.drawable.incorrect_answer_note_icon,
        selectedIconResId = R.drawable.incorrect_answer_note_selected_icon,
        route = MainTabRoute.Clip,
    ),

    MY_PAGE(
        labelResId = R.string.my_page,
        contentDescriptionResId = R.string.my_page,
        iconResId = R.drawable.my_page_icon,
        selectedIconResId = R.drawable.my_page_selected_icon,
        route = MainTabRoute.MyPage,
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainTab? {
            return entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (MainTabRoute) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }
    }
}
