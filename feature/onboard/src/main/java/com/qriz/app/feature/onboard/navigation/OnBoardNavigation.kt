package com.qriz.app.feature.onboard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qriz.app.core.navigation.route.Route
import com.qriz.app.feature.onboard.ConceptCheckScreen
import com.qriz.app.feature.onboard.GuideScreen
import com.qriz.app.feature.onboard.R

fun NavHostController.navigateCheckGuide() {
    navigate(
        Route.Guide(
            title = """SQLD를 어느정도
                |알고 계시나요?""".trimMargin(),
            subTitle = """선택하신 체크사항을 기반으로
                |맞춤 프리뷰 테스트를 제공해 드려요!""".trimMargin(),
            image = R.drawable.img_onboard_check,
            route = GuideType.CONCEPT_CHECK.route,
            buttonText = "시작하기",
        )
    ) { popUpTo<Route.SignIn>() }
}

fun NavGraphBuilder.onboardNavGraph(
    onNavigateFromGuide: (Route) -> Unit,
) {
    composable<Route.Guide> {
        val route = it.toRoute<Route.Guide>()
        GuideScreen(
            title = route.title,
            subTitle = route.subTitle,
            image = route.image,
            buttonText = route.buttonText,
            onNext = {
                val guideType = GuideType
                    .entries
                    .find { guideType ->  guideType.route == route.route }

                when(guideType) {
                    GuideType.CONCEPT_CHECK -> { onNavigateFromGuide(Route.ConceptCheck) }
                    GuideType.PREVIEW -> {}
                    GuideType.WELCOME -> {}
                    null -> {
                        //TODO: Snackbar 띄우기
                    }
                }
            }
        )
    }

    composable<Route.ConceptCheck> {
        ConceptCheckScreen(
            onComplete = {
                
            }
        )
    }
}

enum class GuideType(val route: String) {
    CONCEPT_CHECK("concept_check"),
    PREVIEW("preview"),
    WELCOME("welcome");
}
