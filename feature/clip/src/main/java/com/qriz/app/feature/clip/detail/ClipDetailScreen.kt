package com.qriz.app.feature.clip.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qriz.app.feature.clip.model.ClipDetailUiModel
import com.qriz.app.core.designsystem.component.NavigationType
import com.qriz.app.core.designsystem.component.QrizButton
import com.qriz.app.core.designsystem.component.QrizCard
import com.qriz.app.core.designsystem.component.QrizLoading
import com.qriz.app.core.designsystem.component.QrizTopBar
import com.qriz.app.core.designsystem.theme.Blue100
import com.qriz.app.core.designsystem.theme.Blue50
import com.qriz.app.core.designsystem.theme.Gray200
import com.qriz.app.core.designsystem.theme.Gray500
import com.qriz.app.core.designsystem.theme.Gray700
import com.qriz.app.core.designsystem.theme.Gray800
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.core.designsystem.theme.White
import com.qriz.app.core.ui.common.const.ErrorScreen
import com.qriz.app.core.ui.test.QuestionOptionCard
import com.qriz.app.core.ui.test.TestOptionState
import com.qriz.app.feature.base.extention.collectSideEffect
import com.qriz.app.feature.clip.R
import com.qriz.app.feature.clip.component.QuestionResultCard

@Composable
fun ClipDetailScreen(
    viewModel: ClipDetailViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    onMoveToConceptBookDetail: (Long) -> Unit,
    onMoveToConceptBook: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is ClipDetailUiEffect.MoveBack -> onBack()
            is ClipDetailUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )
            is ClipDetailUiEffect.MoveToConceptBookDetail -> onMoveToConceptBookDetail(it.skillId)
            is ClipDetailUiEffect.MoveToConceptBook -> onMoveToConceptBook()
        }
    }

    ClipDetailContent(
        uiState = uiState,
        onAction = viewModel::process,
    )
}

@Composable
private fun ClipDetailContent(
    uiState: ClipDetailUiState,
    onAction: (ClipDetailUiAction) -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().navigationBarsPadding()
    ) {
        QrizTopBar(
            title = stringResource(R.string.clip_note),
            navigationType = NavigationType.BACK,
            onNavigationClick = { onAction(ClipDetailUiAction.ClickBack) },
        )

        when (uiState) {
            is ClipDetailUiState.Failure -> {
                ErrorScreen(
                    title = stringResource(com.qriz.app.core.ui.common.R.string.error_occurs),
                    description = uiState.message
                ) { }
            }

            is ClipDetailUiState.Loading -> {
                QrizLoading(modifier = Modifier.fillMaxSize())
            }

            is ClipDetailUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Blue50)
                        .padding(
                            horizontal = 18.dp,
                        )
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(32.dp))

                    ResultCard(uiState.clipDetail)

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = stringResource(R.string.question),
                        style = QrizTheme.typography.heading2.copy(color = Gray800)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    QuestionDetail(uiState.clipDetail)

                    Spacer(modifier = Modifier.height(8.dp))

                    MyCheckOptionCard(uiState.clipDetail)

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = stringResource(R.string.explanation),
                        style = QrizTheme.typography.heading2.copy(color = Gray800)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ExplanationCard(uiState.clipDetail)

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = stringResource(R.string.key_concepts),
                        style = QrizTheme.typography.heading2.copy(color = Gray800)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ConceptCard(
                        clipDetail = uiState.clipDetail,
                        onClick = { onAction(ClipDetailUiAction.ClickConceptCard(uiState.clipDetail.skillId)) },
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    QrizButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.go_to_study),
                        onClick = { onAction(ClipDetailUiAction.ClickGoToStudy) },
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun ConceptCard(clipDetail: ClipDetailUiModel, onClick: () -> Unit) {
    QrizCard(
        modifier = Modifier.clickable(
            onClick = onClick,
        ),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 12.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = clipDetail.keyConcepts,
                    style = QrizTheme.typography.subhead,
                    color = Gray700,
                    modifier = Modifier.padding(bottom = 2.dp)
                )

                Text(
                    stringResource(R.string.subject_number, clipDetail.subjectNumber),
                    style = QrizTheme.typography.label2.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Gray500,
                )
            }

            Icon(
                imageVector = ImageVector.vectorResource(id = com.qriz.app.core.designsystem.R.drawable.ic_keyboard_arrow_right),
                contentDescription = null,
                tint = Gray500
            )
        }
    }
}

@Composable
private fun QuestionDetail(
    clipDetail: ClipDetailUiModel
) {
    Column(
        modifier = Modifier
            .background(White)
            .border(
                width = 1.dp,
                color = Gray200,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            clipDetail.questionNum.toString().padStart(
                2,
                '0'
            ) + ".",
            modifier = Modifier.padding(horizontal = 18.dp),
            style = QrizTheme.typography.body1.copy(color = Gray800)
        )

        Text(
            clipDetail.questionText,
            modifier = Modifier.padding(horizontal = 18.dp),
            style = QrizTheme.typography.headline2.copy(color = Gray800)
        )

        Spacer(modifier = Modifier.height(16.dp))

        QuestionOptionCard(
            enableBorder = false,
            state = getTestOptionState(
                clipDetail.checked,
                clipDetail.answer,
                1
            ),
            number = 1,
            optionDescription = clipDetail.option1,
            shape = RectangleShape
        )


        Spacer(modifier = Modifier.height(4.dp))

        QuestionOptionCard(
            enableBorder = false,
            state = getTestOptionState(
                clipDetail.checked,
                clipDetail.answer,
                2
            ),
            number = 2,
            optionDescription = clipDetail.option2,
            shape = RectangleShape
        )

        Spacer(modifier = Modifier.height(4.dp))

        QuestionOptionCard(
            enableBorder = false,
            state = getTestOptionState(
                clipDetail.checked,
                clipDetail.answer,
                3
            ),
            number = 3,
            optionDescription = clipDetail.option3,
            shape = RectangleShape
        )

        Spacer(modifier = Modifier.height(4.dp))

        QuestionOptionCard(
            enableBorder = false,
            state = getTestOptionState(
                clipDetail.checked,
                clipDetail.answer,
                4
            ),
            number = 4,
            optionDescription = clipDetail.option4,
            shape = RectangleShape
        )
    }
}

@Composable
private fun MyCheckOptionCard(
    clipDetail: ClipDetailUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Gray200,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            stringResource(
                R.string.answer_number,
                clipDetail.answer
            ),
            style = QrizTheme.typography.subhead.copy(color = Gray800)
        )

        Text(
            stringResource(
                R.string.my_checked_option,
                clipDetail.checked
            ),
            style = QrizTheme.typography.subhead.copy(color = Gray500)
        )
    }
}

@Composable
private fun ExplanationCard(clipDetail: ClipDetailUiModel) {
    Column(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Gray200,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = White,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            stringResource(R.string.solution),
            style = QrizTheme.typography.subhead.copy(color = Gray700)
        )

        HorizontalDivider(color = Blue100)

        Text(
            clipDetail.solution,
            style = QrizTheme.typography.subhead.copy(color = Gray500)
        )
    }
}

@Composable
private fun ResultCard(clipDetail: ClipDetailUiModel) {
    QuestionResultCard(
        isCorrect = clipDetail.correction,
        title = clipDetail.testInfo,
        question = clipDetail.title,
        tag = listOf(clipDetail.keyConcepts)
    )
}

private fun getTestOptionState(checked: Int, answer: Int, value: Int): TestOptionState {
    return if (value == answer) {
        TestOptionState.SelectedOrCorrect
    } else if (checked != answer && value == checked) {
        TestOptionState.SelectedAndIncorrect
    } else {
        TestOptionState.None
    }
}

@Preview(showBackground = true)
@Composable
private fun ClipDetailContentPreview() {
    QrizTheme {
        ClipDetailContent(
            uiState = ClipDetailUiState.Success(
                clipDetail = ClipDetailUiModel(
                    skillName = "데이터베이스 설계",
                    questionText = "관계형 데이터베이스에서 기본키(Primary Key)에 대한 설명으로 옳지 않은 것은?",
                    questionNum = 3,
                    description = "",
                    option1 = "기본키는 NULL 값을 가질 수 없다.",
                    option2 = "기본키는 중복된 값을 가질 수 없다.",
                    option3 = "기본키는 반드시 단일 컬럼으로만 구성되어야 한다.",
                    option4 = "기본키는 테이블 내에서 각 행을 고유하게 식별한다.",
                    answer = 3,
                    solution = "기본키는 복합 키(Composite Key)로 구성될 수 있습니다. 즉, 여러 컬럼을 조합하여 기본키를 정의할 수 있습니다.",
                    checked = 2,
                    correction = false,
                    testInfo = "2024년 1회",
                    skillId = 1L,
                    title = "데이터베이스",
                    keyConcepts = "기본키(Primary Key)",
                    subjectNumber = 1,
                )
            ),
        )
    }
}
