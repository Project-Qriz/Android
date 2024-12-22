package com.qriz.app.feature.onboard.ui.screen.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.repository.OnBoardRepository
import com.qriz.app.feature.onboard.model.PreviewEffect
import com.qriz.app.feature.onboard.model.PreviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val onBoardRepository: OnBoardRepository
) : ViewModel() {
    private val _state: MutableStateFlow<PreviewState> = MutableStateFlow(PreviewState())
    val state: StateFlow<PreviewState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<PreviewEffect> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val effect: SharedFlow<PreviewEffect> = _effect.asSharedFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            runCatching {
                onBoardRepository.getPreviewTest()
            }.onSuccess { test ->
                _state.update {
                    it.copy(
                        questions = test.questions,
                        remainTime = test.totalTimeLimit.times(1000).toLong(),
                        totalTimeLimit = test.totalTimeLimit.times(1000).toLong(),
                    )
                }
                startTimer()
            }.onFailure {
                //Error Handling
            }
        }
    }

    fun nextPage() {
        val currentPage = state.value.currentPage

        _state.update { it.copy(currentPage = currentPage + 1) }
    }

    fun previousPage() {
        val currentPage = state.value.currentPage

        _state.update { it.copy(currentPage = currentPage - 1) }
    }

    fun saveAnswer(id: Long, answer: String) {
        val answers = state.value.myAnswer.toMutableMap()
        answers[id] = answer

        _state.update { it.copy(myAnswer = answers) }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            var currentTime = state.value.remainTime
            val interval = 1000L
            while (isActive) {
                if(currentTime == 0L) {
                    break
                }

                delay(interval)
                currentTime -= interval
                _state.update { it.copy(remainTime = currentTime) }
            }
        }
    }
}
