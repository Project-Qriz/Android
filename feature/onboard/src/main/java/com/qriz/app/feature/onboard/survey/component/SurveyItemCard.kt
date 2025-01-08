package com.qriz.app.feature.onboard.survey.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.data.onboard.onboard_api.model.PreCheckConcept
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.feature.onboard.R
import com.qriz.app.feature.onboard.survey.model.SurveyListItem

@Composable
fun SurveyItemCard(
    surveyItem: SurveyListItem,
    modifier: Modifier = Modifier,
    onChecked: (Boolean) -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onChecked(surveyItem.isChecked.not()) },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = 24.dp,
            ),
        ) {
            val cardText = when(surveyItem){
                is SurveyListItem.KnowsAll -> stringResource(R.string.know_all_about_concepts)
                is SurveyListItem.KnowsNothing -> stringResource(R.string.know_nothing_about_concepts)
                is SurveyListItem.SurveyItem -> surveyItem.concept.title
            }
            Text(
                text = cardText,
                style = MaterialTheme.typography.bodyMedium,
            )
            Checkbox(
                checked = surveyItem.isChecked,
                colors = CheckboxDefaults.colors().copy(
                    uncheckedBoxColor = Gray200,
                    uncheckedBorderColor = Gray200,
                    uncheckedCheckmarkColor = White,
                ),
                onCheckedChange = { onChecked(surveyItem.isChecked.not()) }
            )
        }
    }
}

@Preview
@Composable
private fun ConceptCheckOptionCardPreview() {
    QrizTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SurveyItemCard(
                surveyItem = SurveyListItem.KnowsAll(false),
                onChecked = {}
            )
            SurveyItemCard(
                surveyItem = SurveyListItem.KnowsNothing(false),
                onChecked = {}
            )
            SurveyItemCard(
                surveyItem = SurveyListItem.SurveyItem(
                    concept = PreCheckConcept.JOIN,
                    isChecked = true
                ),
                onChecked = {}
            )
            SurveyItemCard(
                surveyItem = SurveyListItem.SurveyItem(
                    concept = PreCheckConcept.UNDERSTANDING_NULL_PROPERTIES,
                    isChecked = true
                ),
                onChecked = {}
            )
        }
    }
}
