package com.qriz.app.feature.incorrect_answers_note

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.qriz.app.core.designsystem.theme.QrizTheme
import com.qriz.app.feature.base.extention.collectSideEffect


@Composable
fun IncorrectAnswersNoteScreen(
    viewModel: IncorrectAnswersNoteViewModel = hiltViewModel(),
    onShowSnackBar: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is IncorrectAnswersNoteUiEffect.ShowSnackBar -> onShowSnackBar(
                it.message ?: context.getString(it.defaultResId)
            )
        }
    }

    IncorrectAnswersNoteContent()
}

@Composable
fun IncorrectAnswersNoteContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "IncorrectAnswersNoteScreen",
            style = QrizTheme.typography.display1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IncorrectAnswersNoteContentPreview() {

}
