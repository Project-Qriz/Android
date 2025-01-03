package com.qriz.app.feature.onboard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.qriz.app.core.navigation.route.Route
import com.qriz.app.feature.onboard.survey.ConceptCheckScreen
import com.qriz.app.feature.onboard.guide.GuideScreen
import com.qriz.app.feature.onboard.R
import com.qriz.app.feature.onboard.preview.PreviewScreen

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
    onBack: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    onNavigate: (Route) -> Unit,
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
                    GuideType.CONCEPT_CHECK -> { onNavigate(Route.ConceptCheck) }
                    GuideType.PREVIEW -> { onNavigate(Route.Preview) }
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
            moveToGuide = {
                onNavigate(
                    Route.Guide(
                        title = """테스트를
                            |진행해볼까요?""".trimMargin(),
                        subTitle = """간단한 프리뷰 테스트로 실력을 점검하고
                            |이후 맞춤형 개념과 데일리 테스트를 경험해 보세요!""".trimMargin(),
                        image = R.drawable.img_onboard_test,
                        route = GuideType.PREVIEW.route,
                        buttonText = "간단한 테스트 시작",
                    )
                )
            },
            moveToBack = onBack,
            onShowSnackBar = onShowSnackbar,
        )
    }

    composable<Route.Preview> {
        PreviewScreen(
            onBack = onBack,
            onSubmit = {
                onNavigate(
                    //환영 페이지 데이터 변경하기
                    Route.Guide(
                        title = """테스트를
                            |진행해볼까요?""".trimMargin(),
                        subTitle = """간단한 프리뷰 테스트로 실력을 점검하고
                            |이후 맞춤형 개념과 데일리 테스트를 경험해 보세요!""".trimMargin(),
                        image = R.drawable.img_onboard_test,
                        route = GuideType.WELCOME.route,
                        buttonText = "간단한 테스트 시작",
                    )
                )
            },
        )
    }
}

enum class GuideType(val route: String) {
    CONCEPT_CHECK("concept_check"),
    PREVIEW("preview"),
    WELCOME("welcome");
}
