package com.qriz.app.feature.onboard.survey.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue500
import com.qriz.app.core.designsystem.theme.Gray800
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
            modifier = Modifier
                .background(White)
                .padding(
                    horizontal = 24.dp,
                ),
        ) {
            val cardText = when (surveyItem) {
                is SurveyListItem.KnowsAll -> stringResource(R.string.know_all_about_concepts)
                is SurveyListItem.KnowsNothing -> stringResource(R.string.know_nothing_about_concepts)
                is SurveyListItem.SurveyItem -> surveyItem.concept.title
            }
            Text(
                text = cardText,
                style = QrizTheme.typography.headline3,
                color = Gray800,
            )
            //TODO : 체크되지 않았을때도 흰색 체크 모양이 있어야함
            Checkbox(
                checked = surveyItem.isChecked,
                colors = CheckboxDefaults.colors().copy(
                    uncheckedBoxColor = Blue100,
                    uncheckedBorderColor = Blue100,
                    uncheckedCheckmarkColor = White,
                    checkedBoxColor = Blue500,
                    checkedBorderColor = Blue500,
                    checkedCheckmarkColor = White,
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
                    concept = SQLDConcept.JOIN,
                    isChecked = true
                ),
                onChecked = {}
            )
            SurveyItemCard(
                surveyItem = SurveyListItem.SurveyItem(
                    concept = SQLDConcept.UNDERSTANDING_NULL_PROPERTIES,
                    isChecked = true
                ),
                onChecked = {}
            )
        }
    }
}
