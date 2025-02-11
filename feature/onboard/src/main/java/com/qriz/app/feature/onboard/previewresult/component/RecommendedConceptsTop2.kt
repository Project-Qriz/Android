package com.qriz.app.feature.onboard.previewresult.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.onboard.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RecommendedConceptsTop2(
    modifier: Modifier = Modifier,
    topConceptsToImprove: ImmutableList<SQLDConcept>,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.recommended_concepts_top2),
            style = QrizTheme.typography.body1,
            color = Gray700,
            modifier = Modifier
                .padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            topConceptsToImprove.forEachIndexed { index, concept ->
                QrizCard(
                    modifier = Modifier
                        .weight(1f),
                    cornerRadius = 12.dp,
                    elevation = 0.dp,
                    border = null
                ) {
                    Text(
                        text = concept.title,
                        style = QrizTheme.typography.heading2,
                        color = Gray700,
                        modifier = Modifier
                            .padding(
                                start = 24.dp,
                                top = 24.dp,
                                bottom = 24.dp
                            )
                    )
                }
                if (index == 0) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun RecommendedConceptsTop2Preview() {
    QrizTheme {
        RecommendedConceptsTop2(
            topConceptsToImprove = persistentListOf(
                SQLDConcept.GROUP_FUNCTION,
                SQLDConcept.SELECT
            )
        )
    }
}
