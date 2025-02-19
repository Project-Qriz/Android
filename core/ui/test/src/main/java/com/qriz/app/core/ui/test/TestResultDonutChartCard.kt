package com.qriz.app.core.ui.test

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue300
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Blue800
import com.qriz.app.core.designsystem.theme.Gray100
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

private val TEST_RESULT_COLORS = listOf(
    Blue800, Blue500, Blue300, Blue100, Gray300
)

fun getTestResultColor(index: Int): Color =
    if (index > TEST_RESULT_COLORS.lastIndex) TEST_RESULT_COLORS.last()
    else TEST_RESULT_COLORS[index]

@Composable
fun TestResultDonutChartCard(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    chartTitle: String? = null,
    isLessScore: Boolean = false,
    totalScore: Int,
    estimatedScore: Float,
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
                                    .copy(fontWeight = SemiBold),
                                color = Red700,
                            )
                        }
                    }
                }
            }

            AnimatedDonutChart(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                totalScore = totalScore,
                testResultItems = testResultItems
            )

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.expected_score, estimatedScore.toInt()),
                    modifier = Modifier
                        .padding(end = 4.dp),
                )
                IconButton(
                    modifier = Modifier
                        .size(13.dp),
                    onClick = {}, //TODO: 다이얼로그 노출 디자인 추가 예정
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.exclamation_mark_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(13.dp),
                        tint = Gray300
                    )
                }
            }

            BottomScoreRow(
                modifier = Modifier
                    .padding(top = 32.dp),
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
    val arcs = remember(testResultItems) {
        testResultItems.mapIndexed { index, testResultItem ->
            currentSum += testResultItem.score
            ArcData(
                animation = Animatable(0f),
                targetSweepAngle = -(currentSum / total),
                color = getTestResultColor(index)
            )
        }
    }

    LaunchedEffect(key1 = arcs) {
        arcs.map {
            launch {
                it.animation.animateTo(
                    targetValue = it.targetSweepAngle,
                    animationSpec = tween(
                        durationMillis = animationDurationMillis,
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.total_score),
                style = QrizTheme.typography.body2,
                color = Gray800
            )
            Text(
                text = stringResource(R.string.test_result_score, totalScore),
                style = QrizTheme.typography.heading2,
                color = Gray800
            )
        }

    }
}

@Composable
fun BottomScoreRow(
    modifier: Modifier,
    testResultItems: List<TestResultItem>
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        testResultItems.forEachIndexed { index, testResult ->
            BottomScoreRowItem(
                scoreName = testResult.scoreName,
                scoreColor = getTestResultColor(index),
                score = testResult.score
            )

            if (index != testResultItems.lastIndex) {
                HorizontalDivider(
                    color = Gray100,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
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
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .size(14.dp)
                .clip(CircleShape)
                .background(scoreColor),
        )
        Text(
            text = scoreName,
            style = QrizTheme.typography.body2,
            color = Black
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.test_result_score, score),
            style = QrizTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Gray700
        )
    }

}
