package com.qriz.app.feature.main.component

import androidx.annotation.IdRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import com.qriz.app.feature.main.R

@Composable
fun MainBottomBar(
    onTabClick: (MainTab) -> Unit,
) {

}

@Composable
private fun RowScope.TabItem(
    tab: MainTab,
    selected: Boolean,
) {

}

enum class MainTab(
    val label: String,
    @IdRes val iconResId: Int, //선택 안되었을 때
    @IdRes val selectedIconResId: Int, // 선택 되었을 때,
    val contentDescription: String,
) {
    Home(
        "홈",
        R.drawable.ic_main_bottom_home,
        R.drawable.ic_main_bottom_home_selected,
        "홈",
    ),

    Concept(
        "개념서",
        R.drawable.ic_main_bottom_concept,
        R.drawable.ic_main_bottom_concept_selected,
        "개념서",
    ),

    Clip(
        "오답노트",
        R.drawable.ic_main_bottom_clip,
        R.drawable.ic_main_bottom_clip_selected,
        "오답노트"
    ),

    My(
        "마이",
        R.drawable.ic_main_bottom_my,
        R.drawable.ic_main_bottom_my_selected,
        "마이"
    );
}
