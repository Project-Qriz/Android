package com.qriz.app.core.ui.test

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Black
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray300
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.Red700
import com.qriz.app.core.designsystem.theme.Red700Opacity14
import com.qriz.app.core.ui.test.model.TestResultItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

@Composable
fun TestResultDonutChartCard(
    modifier: Modifier = Modifier,
    totalScore: Int,
    testResultItems: ImmutableList<TestResultItem>,
    getTestResultColor: (index: Int) -> Color,
    title: @Composable () -> Unit,
    chartTitle: String? = null,
    isLessScore: Boolean = false,
    estimatedScore: Float? = null,
    showEstimatedScoreTooltip: Boolean = false,
    onClickDetail: (() -> Unit)? = null,
    onEstimatedScoreTooltipClick: (() -> Unit)? = null,
    onEstimatedScoreTooltipDismissRequest: (() -> Unit)? = null,
) {
    TestResultBaseCard(
        modifier = modifier,
        title = title,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
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
                            modifier = Modifier.padding(start = 12.dp),
                            elevation = 0.dp,
                            border = null,
                            cornerRadius = 4.dp,
                            color = Red700Opacity14
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    ),
                                text = stringResource(R.string.low_score),
                                style = QrizTheme.typography.caption.copy(fontWeight = SemiBold),
                                color = Red700,
                            )
                        }
                    }
                }
            }

            AnimatedDonutChart(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                totalScore = totalScore,
                testResultItems = testResultItems,
                getTestResultColor = getTestResultColor,
            )

            if (estimatedScore != null) {
                var expectedScoreOffset by remember { mutableStateOf(IntOffset.Zero) }
                var expectedScoreSize by remember { mutableStateOf(IntSize.Zero) }

                if (showEstimatedScoreTooltip) {
                    ExpectedScoreTooltip(
                        targetOffset = expectedScoreOffset,
                        targetSize = expectedScoreSize,
                        onDismissRequest = onEstimatedScoreTooltipDismissRequest!!,
                    )
                }
                Row(
                    modifier = Modifier
                        .onGloballyPositioned {
                            expectedScoreOffset = it.positionInParent().round()
                            expectedScoreSize = it.size
                        }
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(
                            R.string.expected_score,
                            estimatedScore.toInt()
                        ),
                        modifier = Modifier.padding(end = 4.dp),
                    )
                    IconButton(
                        modifier = Modifier.size(13.dp),
                        onClick = onEstimatedScoreTooltipClick!!,
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.exclamation_mark_icon),
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = Gray300
                        )
                    }
                }
            }

            BottomScoreRow(
                modifier = Modifier.padding(top = 32.dp),
                testResultItems = testResultItems,
                getTestResultColor = getTestResultColor
            )

            if (onClickDetail != null) {
                Box(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .clickable(onClick = onClickDetail)
                        .border(
                            width = 1.dp,
                            color = Gray200,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(
                            vertical = 13.dp
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.show_score_detail),
                        style = QrizTheme.typography.body2.copy(Gray800),
                    )
                }
            }
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
    chartBackgroundColor: Color = Gray300,
    getTestResultColor: (index: Int) -> Color,
) {
    var animation by rememberSaveable { mutableStateOf(true) }

    val totalAnimationAngle = (totalScore / 100F) * 360
    val total = if (totalAnimationAngle <= 0) 0f else totalScore.div(totalAnimationAngle)

    var currentSum = 0
    val arcs = remember(testResultItems) {
        testResultItems.mapIndexed { index, testResultItem ->
            currentSum += testResultItem.score
            val initialValue = if (animation.not()) {
                -((currentSum / 100F) * 360)
            } else {
                0f
            }
            ArcData(
                animation = Animatable(initialValue),
                targetSweepAngle = -(currentSum / total),
                color = getTestResultColor(index)
            )
        }
    }

    LaunchedEffect(key1 = arcs) {
        if (total == 0f) return@LaunchedEffect

        val animationJobs = mutableListOf<Deferred<*>>()
        if (animation) {
            arcs.map {
                val job = async {
                    it.animation.animateTo(
                        targetValue = it.targetSweepAngle,
                        animationSpec = tween(
                            durationMillis = animationDurationMillis,
                        )
                    )
                }
                animationJobs.add(job)
            }
            animationJobs.awaitAll()
            animation = false
        }
    }

    Box(
        modifier = modifier.size(164.dp),
        contentAlignment = Alignment.Center
    ) {
        val strokeWidth = with(LocalDensity.current) { strokeWidthDp.dp.toPx() }
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val stroke = Stroke(width = strokeWidth)
            val arcSize = size.minDimension - strokeWidth

            drawArc(
                startAngle = 0f,
                sweepAngle = 360f,
                color = chartBackgroundColor,
                useCenter = false,
                style = stroke,
                size = Size(
                    arcSize,
                    arcSize
                ),
                topLeft = Offset(
                    strokeWidth / 2,
                    strokeWidth / 2
                )
            )

            arcs.reversed().map {
                drawArc(
                    startAngle = -90f,
                    sweepAngle = it.animation.value,
                    color = it.color,
                    useCenter = false,
                    style = stroke,
                    size = Size(
                        arcSize,
                        arcSize
                    ),
                    topLeft = Offset(
                        strokeWidth / 2,
                        strokeWidth / 2
                    )
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
                text = stringResource(
                    R.string.test_result_score,
                    totalScore
                ),
                style = QrizTheme.typography.heading2,
                color = Gray800
            )
        }

    }
}

@Composable
fun BottomScoreRow(
    modifier: Modifier,
    testResultItems: List<TestResultItem>,
    getTestResultColor: (index: Int) -> Color
) {
    Column(
        modifier = modifier.fillMaxWidth(),
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
    modifier: Modifier = Modifier,
    scoreName: String,
    scoreColor: Color,
    score: Int,
) {
    Row(
        modifier = modifier,
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
            text = stringResource(
                R.string.test_result_score,
                score
            ),
            style = QrizTheme.typography.body2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Gray700
        )
    }

}
