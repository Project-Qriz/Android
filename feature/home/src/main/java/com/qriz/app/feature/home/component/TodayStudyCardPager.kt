package com.qriz.app.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
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
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.Mint50
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.home.R

//TODO : API 수정 대기 중
@Composable
fun TodayStudyCardPager(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 18.dp,
    currentDay: Int,
    todayStudyConcepts: List<Int>,
    isNeedPreviewTest: Boolean,
    onClickInit: () -> Unit,
    onChangeTodayStudyCard: (Int) -> Unit,
) {
    val pagerState = rememberPagerState { todayStudyConcepts.size }

    LaunchedEffect(currentDay) {
        pagerState.animateScrollToPage(currentDay - 1)
    }

    LaunchedEffect(pagerState.isScrollInProgress) {
        val showingDay = todayStudyConcepts[pagerState.currentPage]
        if (pagerState.isScrollInProgress.not()) {
            onChangeTodayStudyCard(showingDay)
        }
    }

    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = horizontalPadding),
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
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )
                    HorizontalDivider(
                        color = Blue100,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isNeedPreviewTest) {
                    Box(
                        modifier = Modifier
                            .size(20.dp),
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
                    modifier = Modifier
                        .weight(1f)
                )
                if (isNeedPreviewTest.not()) {
                    IconButton(
                        modifier = Modifier
                            .size(24.dp),
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
                val currentDayIndex = todayStudyConcepts.indexOf(currentDay)
                val neededRangeStartIndex = (currentDayIndex / 3) * 3
                Row(
                    modifier = Modifier
                        .padding(top = 12.dp),
                ) {
                    todayStudyConcepts
                        .slice(neededRangeStartIndex..neededRangeStartIndex + 2)
                        .forEachIndexed { index, day ->
                            DateCard(
                                Modifier.weight(1f),
                                day = day,
                                isSelected = currentDay == day,
                                onClickDayCard = onChangeTodayStudyCard
                            )
                            if (index != 2) {
                                Spacer(
                                    modifier = Modifier.width(8.dp)
                                )
                            }
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
                modifier = Modifier
                    .fillMaxWidth(),
                data = todayStudyConcepts[page],
            )
        }

        if (isNeedPreviewTest.not()) {
            //TODO : 이미 완료한 경우도 똑같은 UI 인가?
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
    modifier: Modifier = Modifier,
    data: Int,
) {
    QrizCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = 24.dp,
                    horizontal = 26.dp
                )
        ) {
            Text(
                text = stringResource(R.string.concepts_to_learn) + data,
                style = QrizTheme.typography.headline1
                    .copy(fontWeight = FontWeight.Medium),
                color = Gray800,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        QrizTheme.typography.body2.toSpanStyle()
                            .copy(color = Gray500)
                    ) {
                        append("SQL 기본의 ")
                    }
                    withStyle(
                        QrizTheme.typography.headline2.toSpanStyle()
                            .copy(color = Gray800)
                    ) {
                        append("WHERE 절")
                    }
                },
                modifier = Modifier
                    .padding(bottom = 1.5.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        QrizTheme.typography.body2.toSpanStyle()
                            .copy(color = Gray500)
                    ) {
                        append("데이터 모델링의 이해의 ")
                    }
                    withStyle(
                        QrizTheme.typography.headline2.toSpanStyle()
                            .copy(color = Gray800)
                    ) {
                        append("속성")
                    }
                },
            )

            HorizontalDivider(
                color = Blue100,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 17.5.dp)
            )

            Text(
                text = "WHERE 절이란?",
                style = QrizTheme.typography.headline1
                    .copy(fontWeight = FontWeight.Medium),
                color = Gray800,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .background(Mint50)
                    .padding(
                        vertical = 4.dp,
                        horizontal = 8.dp
                    )
            )
            Text(
                text =
                "SQL 쿼리에서 조건을 지정하여 원하는 데이터만 필터링 하는 방법"
                    .asBulletListText(),
                style = QrizTheme.typography.label,
                color = Gray500,
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        bottom = 16.dp
                    )
            )

            Text(
                text = "속성이란?",
                style = QrizTheme.typography.headline1
                    .copy(fontWeight = FontWeight.Medium),
                color = Gray800,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .background(Mint50)
                    .padding(
                        vertical = 4.dp,
                        horizontal = 8.dp
                    )
            )
            Text(
                text =
                "업무에서 필요로 하는 인스턴스로 관리하고자 하는 의미상 더 이상 분리되지 않는 최소의 데이터 단위"
                    .asBulletListText(),
                style = QrizTheme.typography.label,
                color = Gray500,
                modifier = Modifier
                    .padding(start = 8.dp)
            )
        }
    }
}

//TODO: 이미 Clear 된 경우는 어떤 UI로 보여야하는가
@Composable
fun DateCard(
    modifier: Modifier = Modifier,
    day: Int,
    isSelected: Boolean = false,
    onClickDayCard: (Int) -> Unit,
) {
    QrizCard(
        modifier = modifier
            .clickable { onClickDayCard(day) },
        color = if (isSelected) White else Gray100,
        elevation = if (isSelected) 3.dp else 0.dp,
        border = null
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Day",
                color = if (isSelected) Gray800 else Gray400,
                style = QrizTheme.typography.body2,
                modifier = Modifier
                    .padding(bottom = 4.dp)
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
            currentDay = 0,
            todayStudyConcepts = listOf(1, 2, 3),
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
            currentDay = 0,
            todayStudyConcepts = listOf(1, 2, 3),
            onClickInit = {},
            onChangeTodayStudyCard = {},
        )
    }
}
