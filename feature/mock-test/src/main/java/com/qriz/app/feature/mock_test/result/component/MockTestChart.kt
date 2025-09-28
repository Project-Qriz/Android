package com.qriz.app.feature.mock_test.result.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisTickComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.continuous
import com.patrykandpatrick.vico.compose.cartesian.layer.point
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shape.toVicoShape
import com.patrykandpatrick.vico.core.cartesian.CartesianMeasuringContext
import com.patrykandpatrick.vico.core.cartesian.axis.Axis
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray100
import com.qriz.app.core.designsystem.theme.Gray400
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray600
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.mock_test.R
import com.qriz.app.feature.mock_test.result.MockTestResultUiState
import com.qriz.app.feature.mock_test.result.model.HistoricalResultItem
import com.qriz.app.feature.mock_test.result.model.HistoricalScoreChartItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MockTestChart(
    modifier: Modifier = Modifier,
    filter: MockTestResultUiState.HistoricalScoreFilter,
    showFilterDropDown: Boolean,
    historicalScores: ImmutableList<ImmutableList<HistoricalScoreChartItem>>,
    onSubjectFilterChange: (MockTestResultUiState.HistoricalScoreFilter) -> Unit,
    onSubjectFilterClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(White)
            .fillMaxWidth()
            .padding(
                horizontal = 18.dp,
                vertical = 24.dp,
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.score_fluctuations),
                style = QrizTheme.typography.heading2.copy(color = Gray800)
            )

            HistoryFilter(
                filters = MockTestResultUiState.HistoricalScoreFilter.entries,
                selected = filter,
                showDropDown = showFilterDropDown,
                onClickFilter = onSubjectFilterClick,
                onSelect = onSubjectFilterChange,
            )
        }

        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(R.string.mock_test_chart_guide_text),
            style = QrizTheme.typography.label2.copy(color = Gray400),
        )

        HistoricalResultChart(
            modifier = Modifier.padding(top = 16.dp),
            historicalResults = historicalScores,
        )

        if (filter == MockTestResultUiState.HistoricalScoreFilter.SUBJECT) {
            ChartLegend(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

private class HorizontalValueFormatter(val dateTimes: List<LocalDateTime>) :
    CartesianValueFormatter {
    override fun format(
        context: CartesianMeasuringContext,
        value: Double,
        verticalAxisPosition: Axis.Position.Vertical?
    ): CharSequence {
        if (dateTimes.size <= value) {
            return "--"
        }

        val epochMillis = dateTimes[value.toInt()].toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()

        val time = Instant.fromEpochMilliseconds(epochMillis)
            .toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()

        return time.format(DateTimeFormatter.ofPattern("MM.dd"))
    }
}

@Composable
private fun HistoricalResultChart(
    modifier: Modifier = Modifier,
    historicalResults: ImmutableList<ImmutableList<HistoricalScoreChartItem>>,
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(historicalResults) {
        modelProducer.runTransaction {
            lineSeries {
                historicalResults.forEach { result ->
                    series(
                        x = result.indices.toList(),
                        y = result.map {
                            it.score
                        },
                    )
                }
            }
        }
    }

    val lineColors = listOf(
        Gray500,
        Blue500,
    )

    CartesianChartHost(
        modifier = modifier.height(240.dp),
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    lineColors.map { color ->
                        LineCartesianLayer.rememberLine(
                            fill = LineCartesianLayer.LineFill.single(fill(color)),
                            stroke = LineCartesianLayer.LineStroke.continuous(
                                thickness = 4.dp
                            ),
                            pointProvider = LineCartesianLayer.PointProvider.single(
                                LineCartesianLayer.point(
                                    component = rememberShapeComponent(
                                        fill(White),
                                        CorneredShape.Pill,
                                        strokeThickness = 2.dp,
                                        strokeFill = fill(color)
                                    ),
                                    size = 8.dp,
                                )
                            ),
                        )
                    }
                ),
                rangeProvider = CartesianLayerRangeProvider.fixed(
                    maxY = 100.0,
                    minX = 0.0,
                    maxX = 4.0,
                ),
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = HorizontalValueFormatter(
                    historicalResults.first().map { it.date }
                ),
                itemPlacer = HorizontalAxis.ItemPlacer.aligned(),
                tick = rememberAxisTickComponent(
                    fill = fill(Gray100),
                    thickness = 2.dp,
                ),
                guideline = rememberAxisGuidelineComponent(
                    fill = fill(Gray100),
                    thickness = 2.dp,
                    shape = RectangleShape.toVicoShape()
                ),
                line = rememberAxisLineComponent(
                    fill = fill(Gray100),
                    thickness = 2.dp
                ),
                label = rememberAxisLabelComponent(
                    color = Gray600,
                    textSize = 12.sp
                ),
            ),
            startAxis = VerticalAxis.rememberStart(
                tick = rememberAxisTickComponent(
                    fill = fill(Gray100),
                    thickness = 2.dp,
                ),
                guideline = rememberAxisGuidelineComponent(
                    fill = fill(Gray100),
                    thickness = 2.dp,
                    shape = RectangleShape.toVicoShape()
                ),
                line = null,
                label = rememberAxisLabelComponent(
                    color = Gray600,
                    textSize = 12.sp
                ),
                itemPlacer = VerticalAxis.ItemPlacer.step({ 20.0 })
            ),
        ),
        modelProducer = modelProducer,
    )
}

@Preview(showBackground = true)
@Composable
fun MockTestChartPreview() {
    QrizTheme {
        MockTestChart(
            filter = MockTestResultUiState.HistoricalScoreFilter.TOTAL,
            showFilterDropDown = true,
            historicalScores = persistentListOf(
                persistentListOf(
                    HistoricalScoreChartItem(
                        score = 10,
                        date = LocalDateTime(
                            year = 2025,
                            monthNumber = 6,
                            dayOfMonth = 24,
                            hour = 12,
                            minute = 30,
                        )
                    ),
                    HistoricalScoreChartItem(
                        score = 20,
                        date = LocalDateTime(
                            year = 2025,
                            monthNumber = 7,
                            dayOfMonth = 24,
                            hour = 12,
                            minute = 30,
                        )
                    )
                )
            ),
            onSubjectFilterChange = {},
            onSubjectFilterClick = {},
        )
    }
}
