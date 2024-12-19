package com.qriz.app.feature.onboard.ui.screen.survey

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.onboard.SURVEY_ITEMS
import com.qriz.app.feature.onboard.model.SurveyEffect
import com.qriz.app.feature.onboard.ui.component.SurveyItemCard

@Composable
fun ConceptCheckScreen(
    onBack: () -> Unit,
    onComplete: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    viewModel: SurveyViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            when(it) {
                is SurveyEffect.Complete -> onComplete()
                is SurveyEffect.Error -> { onShowSnackbar(it.message) }
            }
        }
    }

    ConceptCheckContent(
        concepts = state.concepts,
        checked = state.checked,
        onCheckedConcept = viewModel::toggleConcept,
        onSubmit = viewModel::submit,
        onCancel = onBack,
    )
}

@Composable
private fun ConceptCheckContent(
    concepts: List<String>,
    checked: List<String>,
    onCheckedConcept: (String, Boolean) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .clickable { onCancel() },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "취소",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 18.dp)
            )
        }

        Text(
            text = """아는 개념을 체크해주세요!""",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.W600
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 8.dp,
                start = 24.dp,
            ),
        )

        Text(
            text = """체크하신 결과를 토대로
                |추후 진행할 테스트의 레벨이 조정됩니다!""".trimMargin(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.padding(
                start = 24.dp,
                bottom = 32.dp,
            ),
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                start = 18.dp,
                end = 18.dp,
                bottom = 18.dp
            )
        ) {
            items(concepts) { concept ->
                SurveyItemCard(
                    content = concept,
                    isChecked = checked.contains(concept),
                ) { isChecked ->
                    onCheckedConcept(
                        concept,
                        isChecked
                    )
                }
            }
        }

        QrizButton(
            enable = checked.isNotEmpty(),
            text = "선택완료",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .padding(horizontal = 18.dp),
            onClick = onSubmit,
        )
    }
}

@Preview(
    backgroundColor = 0xFFF8F9FD,
    showBackground = true
)
@Composable
fun ConceptCheckContentPreview() {
    QrizTheme {
        ConceptCheckContent(
            concepts = SURVEY_ITEMS,
            checked = listOf(
                "선택지 3번",
                "선택지 4번",
            ),
            onCheckedConcept = { _, _ -> /* none */ },
            onSubmit = {},
            onCancel = {},
        )
    }
}
