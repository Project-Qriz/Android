package com.qriz.app.feature.onboard.previewresult.component

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray200
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
import kotlin.math.min

@Composable
fun FrequentMistakeConceptCard(
    modifier: Modifier = Modifier,
    userName: String,
    questionsCount: Int,
    frequentMistakeConcepts: ImmutableList<WeakAreaItem>,
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
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.total_number_of_question),
                color = Gray600,
                style = QrizTheme.typography.headline1.copy(
                    fontWeight = FontWeight.Normal
                )
            )

            Text(
                text = stringResource(R.string.counting, questionsCount),
                color = Blue500,
                style = QrizTheme.typography.title2
            )
        }

        if (frequentMistakeConcepts.size > 1) {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(82.dp)
                    .padding(bottom = 12.dp),
                frequentMistakeConcepts = frequentMistakeConcepts,
            )
        }

        //TODO : 동점인 경우 아래 추가되는 방식으로 표현해야함 (상하 패딩 수정 필요)
        frequentMistakeConcepts.forEachIndexed { index, weakAreaItem ->
            val isHeaderRankingItem =
                if (index == 0) true
                else frequentMistakeConcepts[index - 1].ranking != weakAreaItem.ranking

            val rowTopPadding = if (isHeaderRankingItem) 8.dp else 6.dp
            val rowBottomPadding = if (isHeaderRankingItem) 8.dp else 6.dp

            WrongQuestionRankingItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = rowTopPadding,
                        bottom = rowBottomPadding
                    ),
                ranking = weakAreaItem.ranking,
                isHeaderRankingItem = isHeaderRankingItem,
                sqldConcept = weakAreaItem.topic,
                incorrectCount = weakAreaItem.incorrectCount
            )
        }
        if (frequentMistakeConcepts.size > 3) {
            VerticalDotsIcon(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun BarChart(
    modifier: Modifier = Modifier,
    frequentMistakeConcepts: ImmutableList<WeakAreaItem>,
    background: Color = White,
    lineColor: Color = Blue100
) {
    val upperValue =
        remember { (frequentMistakeConcepts.maxOfOrNull { it.incorrectCount }?.plus(1)) ?: 0 }

    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = with(density) { 12.sp.toPx() }
        }
    }

    Canvas(
        modifier
            .background(background)
    ) {
        //TODO : remember 필요 여부 확인
        //TODO : 동점인 경우 높이가 낮은채로 같은 현상
        //TODO : 애니메이션 추가 필요
        //TODO : 순위별 색상이 달라야함
        //TODO : 그래프 상단 색상 굵기 설정
        val canvasHeight = size.height
        val canvasWidth = size.width
        val barGraphWidth = with(density) { 28.dp.toPx() }
        val horizontalLineWidth = with(density) { 1.dp.toPx() }
        val spaceBetweenTextAndGraph = with(density) { 10.dp.toPx() }

        val maxVisibleGraphsCount = (canvasWidth / barGraphWidth).toInt()
        val visibleGraphsCount = min(frequentMistakeConcepts.size, maxVisibleGraphsCount)

        val canvasWidthExceptBarWidth = (canvasWidth - visibleGraphsCount * barGraphWidth)
        val spacingFromLeft = when (visibleGraphsCount % 2) {
            0 -> canvasWidthExceptBarWidth / (visibleGraphsCount + 1)
            else -> canvasWidthExceptBarWidth / ((visibleGraphsCount + 1) / 2) * (5f / 17)
        }

        val spacePerGraph = when (visibleGraphsCount % 2) {
            0 -> canvasWidthExceptBarWidth / (visibleGraphsCount + 1)
            else -> canvasWidthExceptBarWidth / ((visibleGraphsCount + 1) / 2) * (12f / 17)
        }

        //HorizontalLine
        drawLine(
            start = Offset(
                x = 0F,
                y = canvasHeight
            ),
            end = Offset(
                x = canvasWidth,
                y = canvasHeight
            ),
            color = lineColor,
            strokeWidth = horizontalLineWidth
        )
        val textHeight = with(density) { 12.sp.toPx() } //TODO : 다시 수정 필요
        val barMaxHeight = canvasHeight - (textHeight + spaceBetweenTextAndGraph)

        //BarGraph
        frequentMistakeConcepts.take(visibleGraphsCount)
            .forEachIndexed { index, frequentMistakeConcept ->
                val barStartX = spacingFromLeft + (barGraphWidth * index) + (spacePerGraph * index)
                val barStartY =
                    textHeight + spaceBetweenTextAndGraph + ((upperValue - frequentMistakeConcept.incorrectCount).toFloat() / upperValue) * barMaxHeight

                drawRoundRect(
                    color = Blue500,
                    topLeft = Offset(
                        x = barStartX,
                        y = barStartY
                    ),
                    size = Size(
                        width = barGraphWidth,
                        height = (frequentMistakeConcept.incorrectCount.toFloat() / upperValue) * barMaxHeight
                        //((value / upperValue) * canvasHeight) - spacingFromBottom
                    ),
                    cornerRadius = CornerRadius(10f, 10f)
                )

                drawContext.canvas.nativeCanvas.apply {
                    var displayText = frequentMistakeConcept.topic.title
                    if (displayText.length > 3) {
                        displayText = displayText.take(3) + "..."
                    }

                    drawText(
                        displayText,
                        barStartX + (barGraphWidth / 2),
                        barStartY - spaceBetweenTextAndGraph,
                        textPaint
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
        Ranking.THIRD -> ""
    }

    val rankingColor = when (ranking) {
        Ranking.FIRST -> Blue500
        Ranking.SECOND,
        Ranking.THIRD -> Gray500
    }

    val conceptColor = when (ranking) {
        Ranking.FIRST -> Gray800
        Ranking.SECOND -> Gray600
        Ranking.THIRD -> Gray500
    }

    val conceptWeight = when (ranking) {
        Ranking.FIRST,
        Ranking.SECOND -> FontWeight.Bold

        Ranking.THIRD -> FontWeight.Normal
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
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Text(
            text = sqldConcept.title,
            style = QrizTheme.typography.headline2.copy(
                fontWeight = conceptWeight
            ),
            color = conceptColor
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Text(
            text = stringResource(R.string.question_count, incorrectCount),
            style = QrizTheme.typography.body2,
            color = Gray800
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
        FrequentMistakeConceptCard(
            userName = "Qriz",
            questionsCount = 20,
            frequentMistakeConcepts = persistentListOf(
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
                    incorrectCount = 1
                ),
//                WeakAreaItem(
//                    ranking = Ranking.THIRD,
//                    topic = SQLDConcept.UNDERSTANDING_TRANSACTIONS,
//                    incorrectCount = 1
//                ),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FrequentMistakeConceptCardSameCountPreview() {
    QrizTheme {
        FrequentMistakeConceptCard(
            userName = "Qriz",
            questionsCount = 20,
            frequentMistakeConcepts = persistentListOf(
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
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FrequentMistakeConceptCard2Preview() {
    QrizTheme {
        FrequentMistakeConceptCard(
            userName = "Qriz",
            questionsCount = 20,
            frequentMistakeConcepts = persistentListOf(
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
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FrequentMistakeConceptCard3Preview() {
    QrizTheme {
        FrequentMistakeConceptCard(
            userName = "Qriz",
            questionsCount = 20,
            frequentMistakeConcepts = persistentListOf(
                WeakAreaItem(
                    ranking = Ranking.FIRST,
                    topic = SQLDConcept.GROUP_BY_AND_HAVING,
                    incorrectCount = 3
                ),
            )
        )
    }
}
