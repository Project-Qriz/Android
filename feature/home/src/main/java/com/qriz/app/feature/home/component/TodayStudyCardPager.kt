package com.qriz.app.feature.home.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.model.PlannedSkill
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.home.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.time.LocalDate

@Composable
fun TodayStudyCardPager(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 18.dp,
    selectedPlanDay: Int,
    dailyStudyPlans: ImmutableList<DailyStudyPlan>,
    isNeedPreviewTest: Boolean,
    onClickInit: () -> Unit,
    onChangeTodayStudyCard: (Int) -> Unit,
) {
    val pagerState = rememberPagerState { dailyStudyPlans.size }

    LaunchedEffect(selectedPlanDay) {
        pagerState.animateScrollToPage(selectedPlanDay - 1)
    }

    LaunchedEffect(pagerState.isScrollInProgress) {
        val showingDay = pagerState.currentPage + 1
        if (pagerState.isScrollInProgress.not()) {
            onChangeTodayStudyCard(showingDay)
        }
    }

    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = horizontalPadding),
        ) {
            if (isNeedPreviewTest) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.preview_test_open),
                        color = Gray500,
                        style = QrizTheme.typography.label,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    HorizontalDivider(
                        color = Blue100,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isNeedPreviewTest) {
                    Box(
                        modifier = Modifier.size(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.lock_icon),
                            contentDescription = null,
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.today_s_study),
                    color = Gray800,
                    style = QrizTheme.typography.headline1,
                    modifier = Modifier.weight(1f)
                )

                if (isNeedPreviewTest.not()) {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = onClickInit,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.renew_icon),
                            contentDescription = null,
                            tint = Gray500
                        )
                    }
                }
            }

            if (isNeedPreviewTest.not()) {
                Row(
                    modifier = Modifier.padding(top = 19.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val startDay = if (selectedPlanDay == 1) selectedPlanDay else selectedPlanDay - 1
                    for (day in startDay..startDay + 2) {
                        DateCard(
                            day = day,
                            isSelected = if (selectedPlanDay == 1) day == 1 else day == selectedPlanDay,
                            modifier = Modifier.weight(1f),
                            onClickDayCard = { onChangeTodayStudyCard(day) },
                        )
                    }
                }
            }
        }

        HorizontalPager(
            modifier = Modifier
                .blur(if (isNeedPreviewTest) 4.dp else 0.dp)
                .padding(
                    top = 12.dp,
                    bottom = if (isNeedPreviewTest) 32.dp else 16.dp
                ),
            state = pagerState,
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            pageSpacing = 8.dp,
            userScrollEnabled = isNeedPreviewTest.not(),
        ) { page ->
            TodayStudyCard(
                modifier = Modifier.fillMaxWidth(),
                plan = dailyStudyPlans[page],
            )
        }

        if (isNeedPreviewTest.not()) {
            QrizButton(
                text = stringResource(R.string.go_to_study),
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding)
                    .padding(
                        bottom = if (isNeedPreviewTest) 0.dp else 32.dp
                    )
            )
        }
    }
}

@Composable
private fun TodayStudyCard(
    plan: DailyStudyPlan,
    modifier: Modifier = Modifier,
) {
    QrizCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 24.dp,
                horizontal = 26.dp
            )
        ) {
            if (plan.comprehensiveReviewDay) Text(
                text = stringResource(R.string.comprehensive_review),
                style = QrizTheme.typography.headline1.copy(color = Gray800),
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        QrizTheme.typography.headline1.toSpanStyle().copy(
                            color = Gray800,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append(
                            if (plan.reviewDay) stringResource(R.string.review_concept)
                            else stringResource(R.string.study_concept)
                        )
                    }
                    withStyle(
                        QrizTheme.typography.headline1.toSpanStyle().copy(color = Gray800)
                    ) {
                        append("${plan.plannedSkills.size}가지")
                    }
                },
                modifier = Modifier.padding(bottom = 1.5.dp)
            )

            DashedDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 17.5.dp)
            )

            if (plan.comprehensiveReviewDay) {
                Text(
                    text = stringResource(R.string.comprehensive_review_description),
                    style = QrizTheme.typography.body2.copy(color = Gray500)
                )
            }

            plan.plannedSkills.forEach {
                ConceptCard(
                    type = it.type,
                    keyConcept = it.keyConcept,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            if (plan.plannedSkills.size > 2) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_ellipsis),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    tint = Gray200,
                )
            }
        }
    }
}

@Composable
private fun ConceptCard(
    type: String,
    keyConcept: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = Blue50,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = Blue100,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = type,
                style = QrizTheme.typography.body2.copy(color = Gray400)
            )
            Text(
                text = keyConcept,
                style = QrizTheme.typography.headline2.copy(color = Gray800)
            )
        }
    }
}

@Composable
private fun DashedDivider(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        val pathEffect = PathEffect.dashPathEffect(
            intervals = floatArrayOf(
                20f,
                10f
            ),
            phase = 0f
        )

        drawLine(
            color = Blue100,
            start = Offset(
                0f,
                size.height / 2
            ),
            end = Offset(
                size.width,
                size.height / 2
            ),
            strokeWidth = 1.dp.toPx(),
            pathEffect = pathEffect,
        )
    }
}

@Composable
fun DateCard(
    day: Int,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClickDayCard: (Int) -> Unit,
) {
    QrizCard(
        modifier = modifier
            .aspectRatio(if (isSelected) 105f / 70f else 105f / 64f)
            .clickable { onClickDayCard(day) },
        color = if (isSelected) White else Gray100,
        elevation = if (isSelected) 3.dp else 0.dp,
        border = null
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Day",
                color = if (isSelected) Gray800 else Gray400,
                style = QrizTheme.typography.body2,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = if (isSelected) Black else Gray400,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.toString(),
                    color = White,
                    style = QrizTheme.typography.label2
                )
            }
        }
    }
}

fun String.asBulletListText(
    bullet: String = "•  ",
    restLine: TextUnit = 12.sp,
) = buildAnnotatedString {
    split("\n").forEach {
        val txt = it.trim()
        if (txt.isBlank()) return@forEach

        withStyle(
            style = ParagraphStyle(
                textIndent = TextIndent(
                    restLine = restLine
                )
            )
        ) {
            append(bullet + txt)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TodayStudyCardPreview() {
    QrizTheme {
        TodayStudyCardPager(
            isNeedPreviewTest = false,
            selectedPlanDay = 0,
            dailyStudyPlans = persistentListOf(
                DailyStudyPlan(
                    id = 1,
                    comprehensiveReviewDay = true,
                    reviewDay = true,
                    completionDate = null,
                    completed = true,
                    plannedSkills = listOf(
                        PlannedSkill(
                            id = 1,
                            keyConcept = "",
                            type = "",
                            description = "",
                        )
                    ),
                    planDate = LocalDate.now(),
                )
            ),
            onClickInit = {},
            onChangeTodayStudyCard = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TodayStudyCardPreview2() {
    QrizTheme {
        TodayStudyCardPager(
            isNeedPreviewTest = true,
            selectedPlanDay = 0,
            dailyStudyPlans = persistentListOf(
                DailyStudyPlan(
                    id = 1,
                    comprehensiveReviewDay = true,
                    reviewDay = true,
                    completionDate = null,
                    completed = true,
                    plannedSkills = listOf(
                        PlannedSkill(
                            id = 1,
                            keyConcept = "",
                            type = "",
                            description = "",
                        )
                    ),
                    planDate = LocalDate.now(),
                )
            ),
            onClickInit = {},
            onChangeTodayStudyCard = {},
        )
    }
}
