package com.qriz.app.feature.onboard.previewresult.component

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.test.TestResultBaseCard
import com.qriz.app.feature.onboard.R
import com.qriz.app.feature.onboard.previewresult.model.Ranking
import com.qriz.app.feature.onboard.previewresult.model.WeakAreaItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import kotlin.math.min

private val BAR_CHART_COLORS = listOf(
    Blue500, Gray600, Gray400
)

fun getTestResultColor(index: Int): Color =
    if (index > BAR_CHART_COLORS.lastIndex) BAR_CHART_COLORS.last()
    else BAR_CHART_COLORS[index]

@Composable
fun FrequentMistakeConceptCard(
    modifier: Modifier = Modifier,
    userName: String,
    totalQuestionsCount: Int,
    frequentMistakeConcepts: ImmutableList<WeakAreaItem>,
    isExistOthersRanking: Boolean,
) {
    TestResultBaseCard(
        modifier = modifier,
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        QrizTheme.typography.heading2.toSpanStyle()
                            .copy(fontWeight = FontWeight.Normal)
                    ) {
                        append(
                            stringResource(
                                R.string.mistake_concept_card_user_name,
                                userName
                            ) + " "
                        )
                    }
                    withStyle(
                        QrizTheme.typography.heading2.toSpanStyle()
                    ) {
                        append(stringResource(R.string.wrong_question))
                    }
                    withStyle(
                        QrizTheme.typography.heading2.toSpanStyle()
                            .copy(fontWeight = FontWeight.Normal)
                    ) {
                        append(stringResource(R.string.on) + "\n")
                    }
                    withStyle(
                        QrizTheme.typography.heading2.toSpanStyle()
                    ) {
                        append(stringResource(R.string.concepts_that_appear_frequently))
                    }
                },
                color = Gray800
            )
        },
    ) {
        Text(
            text = stringResource(R.string.total_number_of_question, totalQuestionsCount),
            color = Gray500,
            style = QrizTheme.typography.label2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
        )

        val isAllFirst = remember {
            frequentMistakeConcepts.all { it.ranking == Ranking.FIRST }
        }

        if (frequentMistakeConcepts.size > 1 && isAllFirst.not()) {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                frequentMistakeConcepts = frequentMistakeConcepts,
            )
        }

        for ((index, weakAreaItem) in frequentMistakeConcepts.withIndex()) {
            if (weakAreaItem.ranking == Ranking.OTHERS) break
            val isHeaderRankingItem =
                if (index == 0) true
                else frequentMistakeConcepts[index - 1].ranking != weakAreaItem.ranking

            val nextIsHeaderRankingItem =
                if (index == frequentMistakeConcepts.lastIndex) false
                else frequentMistakeConcepts[index + 1].ranking != weakAreaItem.ranking
            val rowBottomPadding = if (nextIsHeaderRankingItem) 16.dp else 6.dp

            WrongQuestionRankingItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = rowBottomPadding),
                ranking = weakAreaItem.ranking,
                isHeaderRankingItem = isHeaderRankingItem,
                sqldConcept = weakAreaItem.topic,
                incorrectCount = weakAreaItem.incorrectCount
            )
        }

        if (isExistOthersRanking) {
            VerticalDotsIcon(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }

    }
}

data class BarData(
    val animation: Animatable<Float, AnimationVector1D>,
    val targetHeight: Float,
    val color: Color,
    val title: String,
    val value: Int,
)

@Composable
private fun BarChart(
    modifier: Modifier = Modifier,
    frequentMistakeConcepts: ImmutableList<WeakAreaItem>,
    height: Dp = 82.dp,
    animationDurationMillis: Int = 1500,
    background: Color = White,
    axisColor: Color = Blue100,
    axisWidth: Dp = 1.dp,
    barGraphWidth: Dp = 28.dp,
    barGraphCornerRadius: Dp = 8.dp,
    spaceBetweenTextAndGraph: Dp = 10.dp,
    barTextSize: TextUnit = 12.sp,
) {
    val density = LocalDensity.current
    val upperValue = remember { (frequentMistakeConcepts.maxOfOrNull { it.incorrectCount }) ?: 0 }
    val textHeight = remember { with(density) { barTextSize.toDp() } }
    val barMaxHeight = remember {
        with(density) { (height - (textHeight + spaceBetweenTextAndGraph)).toPx() }
    }
    val bars = remember(frequentMistakeConcepts) {
        frequentMistakeConcepts
            .groupBy { it.ranking }
            .map { it.value.first() to it.value.size - 1 }
            .mapIndexed { index, (weakAreaItem, sameRankingCount) ->
                var textDisplayed = weakAreaItem.topic.title
                if (textDisplayed.length > 3) {
                    textDisplayed = textDisplayed.take(3) + "⋯"
                }
                if (sameRankingCount != 0) textDisplayed += " + ${sameRankingCount}개"

                BarData(
                    animation = Animatable(0f),
                    targetHeight = (weakAreaItem.incorrectCount.toFloat() / upperValue) * barMaxHeight,
                    color = getTestResultColor(index),
                    title = textDisplayed,
                    value = weakAreaItem.incorrectCount,
                )
            }
    }

    LaunchedEffect(bars) {
        bars.forEach {
            launch {
                it.animation.animateTo(
                    targetValue = it.targetHeight,
                    animationSpec = tween(
                        durationMillis = animationDurationMillis,
                    )
                )
            }
        }
    }

    Canvas(
        modifier
            .height(height)
            .background(background)
    ) {
        val canvasHeightPx = size.height
        val canvasWidthPx = size.width
        val barGraphWidthPx = with(density) { barGraphWidth.toPx() }
        val axisWidthPx = with(density) { axisWidth.toPx() }
        val spaceBetweenTextAndGraphPx = with(density) { spaceBetweenTextAndGraph.toPx() }
        val visibleGraphsCount = min(bars.size, 3)

        val canvasWidthExceptBarWidthPx = (canvasWidthPx - visibleGraphsCount * barGraphWidthPx)
        val spacingFromLeft = when (visibleGraphsCount % 2) {
            0 -> canvasWidthExceptBarWidthPx / (visibleGraphsCount + 1)
            else -> canvasWidthExceptBarWidthPx / ((visibleGraphsCount + 1) / 2) * (5f / 17)
        }

        val spacePerGraphPx = when (visibleGraphsCount % 2) {
            0 -> canvasWidthExceptBarWidthPx / (visibleGraphsCount + 1)
            else -> canvasWidthExceptBarWidthPx / ((visibleGraphsCount + 1) / 2) * (12f / 17)
        }

        //HorizontalLine
        drawLine(
            start = Offset(
                x = 0F,
                y = canvasHeightPx
            ),
            end = Offset(
                x = canvasWidthPx,
                y = canvasHeightPx
            ),
            color = axisColor,
            strokeWidth = axisWidthPx
        )

        //BarGraph
        bars.forEachIndexed { index, barData ->
            val barStartX =
                spacingFromLeft + (barGraphWidthPx * index) + (spacePerGraphPx * index)
            val cornerRadiusPx = with(density) { barGraphCornerRadius.toPx() }
            val cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
            val path = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = Rect(
                            offset = Offset(
                                x = barStartX,
                                y = canvasHeightPx
                            ),
                            size = Size(
                                width = barGraphWidthPx,
                                height = -barData.animation.value
                            )
                        ),
                        topLeft = cornerRadius,
                        topRight = cornerRadius,
                    )
                )
            }
            drawPath(path, color = barData.color)

            //BarGraph Top Text
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    barData.title,
                    barStartX + (barGraphWidthPx / 2),
                    canvasHeightPx - barData.animation.value - spaceBetweenTextAndGraphPx,
                    Paint().apply {
                        color = getTestResultColor(index).toArgb()
                        textAlign = Paint.Align.CENTER
                        textSize = with(density) { barTextSize.toPx() }
                        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                    }
                )
            }
        }
    }
}

@Composable
private fun WrongQuestionRankingItem(
    modifier: Modifier,
    ranking: Ranking,
    isHeaderRankingItem: Boolean,
    sqldConcept: SQLDConcept,
    incorrectCount: Int,
) {
    val rankingText = when (ranking) {
        Ranking.FIRST -> stringResource(R.string.first)
        Ranking.SECOND -> stringResource(R.string.second)
        Ranking.THIRD,
        Ranking.OTHERS -> ""
    }

    val rankingColor = when (ranking) {
        Ranking.FIRST -> Blue500
        Ranking.SECOND -> Gray500
        Ranking.THIRD,
        Ranking.OTHERS -> Color.Transparent
    }

    val conceptColor = when (ranking) {
        Ranking.FIRST -> Gray800
        Ranking.SECOND -> Gray600
        Ranking.THIRD -> Gray500
        Ranking.OTHERS -> Color.Transparent
    }

    val conceptWeight = when (ranking) {
        Ranking.FIRST,
        Ranking.SECOND -> FontWeight.Bold

        Ranking.THIRD,
        Ranking.OTHERS -> FontWeight.Normal
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        if (rankingText.isNotBlank()) {
            QrizCard(
                modifier = Modifier
                    .padding(end = 8.dp),
                color = if (isHeaderRankingItem) Blue100 else Color.Transparent,
                elevation = 0.dp,
                border = null,
                cornerRadius = 4.dp
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                    text = rankingText,
                    color = if (isHeaderRankingItem) rankingColor else Color.Transparent,
                    style = QrizTheme.typography.caption.copy(
                        fontWeight = FontWeight.Bold,
                    )
                )
            }
        }

        Text(
            text = sqldConcept.title,
            style = QrizTheme.typography.headline2.copy(
                fontWeight = conceptWeight
            ),
            color = conceptColor,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = stringResource(R.string.question_count, incorrectCount),
            style = QrizTheme.typography.body2,
            color = Gray800,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun VerticalDotsIcon(
    modifier: Modifier = Modifier,
    color: Color = Gray200
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(color, shape = CircleShape)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FrequentMistakeConceptCardPreview() {
    QrizTheme {
        val frequentMistakeConcepts = persistentListOf(
            WeakAreaItem(
                ranking = Ranking.FIRST,
                topic = SQLDConcept.GROUP_BY_AND_HAVING,
                incorrectCount = 5
            ),
            WeakAreaItem(
                ranking = Ranking.SECOND,
                topic = SQLDConcept.RELATIONAL_DATABASE_OVERVIEW,
                incorrectCount = 3
            ),
            WeakAreaItem(
                ranking = Ranking.THIRD,
                topic = SQLDConcept.UNDERSTANDING_TRANSACTIONS,
                incorrectCount = 2
            ),
            WeakAreaItem(
                ranking = Ranking.OTHERS,
                topic = SQLDConcept.UNDERSTANDING_TRANSACTIONS,
                incorrectCount = 1
            ),
        )
        FrequentMistakeConceptCard(
            userName = "Qriz",
            totalQuestionsCount = 20,
            frequentMistakeConcepts = frequentMistakeConcepts,
            isExistOthersRanking = frequentMistakeConcepts.any {
                it.ranking == Ranking.OTHERS
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FrequentMistakeConceptCardSameCountPreview() {
    QrizTheme {
        val frequentMistakeConcepts = persistentListOf(
            WeakAreaItem(
                ranking = Ranking.FIRST,
                topic = SQLDConcept.GROUP_BY_AND_HAVING,
                incorrectCount = 3
            ),
            WeakAreaItem(
                ranking = Ranking.FIRST,
                topic = SQLDConcept.RELATIONAL_DATABASE_OVERVIEW,
                incorrectCount = 3
            ),
            WeakAreaItem(
                ranking = Ranking.FIRST,
                topic = SQLDConcept.DCL,
                incorrectCount = 3
            ),
            WeakAreaItem(
                ranking = Ranking.SECOND,
                topic = SQLDConcept.TOP_N_QUERIES,
                incorrectCount = 2
            ),
        )
        FrequentMistakeConceptCard(
            userName = "Qriz",
            totalQuestionsCount = 20,
            frequentMistakeConcepts = frequentMistakeConcepts,
            isExistOthersRanking = frequentMistakeConcepts.any {
                it.ranking == Ranking.OTHERS
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FrequentMistakeConceptCard2Preview() {
    QrizTheme {
        val frequentMistakeConcepts = persistentListOf(
            WeakAreaItem(
                ranking = Ranking.FIRST,
                topic = SQLDConcept.GROUP_BY_AND_HAVING,
                incorrectCount = 3
            ),
            WeakAreaItem(
                ranking = Ranking.FIRST,
                topic = SQLDConcept.RELATIONAL_DATABASE_OVERVIEW,
                incorrectCount = 3
            ),
        )
        FrequentMistakeConceptCard(
            userName = "Qriz",
            totalQuestionsCount = 20,
            frequentMistakeConcepts = frequentMistakeConcepts,
            isExistOthersRanking = frequentMistakeConcepts.any {
                it.ranking == Ranking.OTHERS
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FrequentMistakeConceptCard3Preview() {
    QrizTheme {
        val frequentMistakeConcepts = persistentListOf(
            WeakAreaItem(
                ranking = Ranking.FIRST,
                topic = SQLDConcept.GROUP_BY_AND_HAVING,
                incorrectCount = 3
            ),
        )
        FrequentMistakeConceptCard(
            userName = "Qriz",
            totalQuestionsCount = 20,
            frequentMistakeConcepts = frequentMistakeConcepts,
            isExistOthersRanking = frequentMistakeConcepts.any {
                it.ranking == Ranking.OTHERS
            }
        )
    }
}
