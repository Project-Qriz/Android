package com.qriz.app.core.ui.test

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue200
import com.qriz.app.core.designsystem.theme.Blue400
import com.qriz.app.core.designsystem.theme.Blue700
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.theme.Red700Opacity14
import com.qriz.app.core.ui.test.model.TestResultItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

//TODO : 디자인 색상 추가 예정 (수정 대기중)
private val TEST_RESULT_COLORS = listOf(
    Blue700, Blue400, Blue200
)

fun getTestResultColor(index: Int): Color =
    if (index > TEST_RESULT_COLORS.lastIndex) TEST_RESULT_COLORS.last()
    else TEST_RESULT_COLORS[index]

@Composable
fun TestResultDonutChartCard(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subTitle: String,
    chartTitle: String? = null,
    isLessScore: Boolean = false,
    expectedScore: Int,
    testResultItems: ImmutableList<TestResultItem>
) {
    TestResultBaseCard(
        modifier = modifier,
        title = title,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = subTitle,
                style = QrizTheme.typography.subhead,
                color = Gray600,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )

            if (chartTitle != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chartTitle,
                        style = QrizTheme.typography.headline2,
                        color = Gray600,
                    )
                    if (isLessScore) {
                        QrizCard(
                            modifier = Modifier
                                .padding(start = 12.dp),
                            elevation = 0.dp,
                            border = null,
                            cornerRadius = 4.dp,
                            color = Red700Opacity14
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    ),
                                text = stringResource(R.string.low_score),
                                style = QrizTheme.typography.caption
                                    .copy(
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        ),
                                        fontWeight = SemiBold,
                                    ),
                                color = Red700,
                            )
                        }
                    }
                }
            }

            AnimatedDonutChart(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                totalScore = expectedScore,
                testResultItems = testResultItems
            )

            BottomScoreRow(
                modifier = Modifier
                    .padding(top = 12.dp),
                testResultItems = testResultItems
            )
        }
    }
}

data class ArcData(
    val animation: Animatable<Float, AnimationVector1D>,
    val targetSweepAngle: Float,
    val color: Color
)

@Composable
fun AnimatedDonutChart(
    modifier: Modifier,
    totalScore: Int,
    testResultItems: ImmutableList<TestResultItem>,
    animationDurationMillis: Int = 2000,
    strokeWidthDp: Int = 37,
    chartBackgroundColor: Color = Gray300
) {
    val totalAnimationAngle = (totalScore / 100F) * 360
    val total = testResultItems
        .fold(0f) { acc, result -> acc + result.score }
        .div(totalAnimationAngle)

    var currentSum = 0
    val arcs = testResultItems.mapIndexed { index, testResultItem ->
        currentSum += testResultItem.score
        ArcData(
            animation = Animatable(0f),
            targetSweepAngle = -(currentSum / total),
            color = getTestResultColor(index)
        )
    }

    LaunchedEffect(key1 = arcs) {
        arcs.map {
            launch {
                it.animation.animateTo(
                    targetValue = it.targetSweepAngle,
                    animationSpec = tween(
                        durationMillis = animationDurationMillis,
                        easing = FastOutSlowInEasing,
                    )
                )
            }
        }
    }

    Box(
        modifier = modifier
            .size(164.dp),
        contentAlignment = Alignment.Center
    ) {
        val strokeWidth = with(LocalDensity.current) { strokeWidthDp.dp.toPx() }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val stroke = Stroke(width = strokeWidth)
            val arcSize = size.minDimension - strokeWidth

            drawArc(
                startAngle = 0f,
                sweepAngle = 360f,
                color = chartBackgroundColor,
                useCenter = false,
                style = stroke,
                size = Size(arcSize, arcSize),
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            )

            arcs.reversed().map {
                drawArc(
                    startAngle = -90f,
                    sweepAngle = it.animation.value,
                    color = it.color,
                    useCenter = false,
                    style = stroke,
                    size = Size(arcSize, arcSize),
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
                )
            }
        }

        Text(
            text = stringResource(R.string.test_result_score, totalScore),
            style = QrizTheme.typography.heading2,
            color = Gray800
        )
    }
}

@Composable
fun BottomScoreRow(
    modifier: Modifier,
    testResultItems: List<TestResultItem>
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
        ) {
            testResultItems.forEachIndexed { index, testResult ->
                BottomScoreRowItem(
                    scoreName = testResult.scoreName,
                    scoreColor = getTestResultColor(index),
                    score = testResult.score
                )

                if (index != testResultItems.lastIndex) {
                    VerticalDivider(
                        color = Blue100,
                        thickness = 1.dp,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BottomScoreRowItem(
    scoreName: String,
    scoreColor: Color,
    score: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(scoreColor),
            )
            Text(
                text = scoreName,
                style = QrizTheme.typography.label2,
                color = Gray600
            )
        }
        Text(
            text = stringResource(R.string.test_result_score, score),
            style = QrizTheme.typography.subhead,
            color = Gray700
        )
    }
}
