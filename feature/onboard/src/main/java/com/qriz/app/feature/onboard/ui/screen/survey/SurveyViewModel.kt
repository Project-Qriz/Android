package com.qriz.app.feature.onboard.ui.screen.survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.repository.OnBoardingRepository
import com.qriz.app.feature.onboard.SURVEY_ITEMS
import com.qriz.app.feature.onboard.KNOWN_ALL
import com.qriz.app.feature.onboard.KNOWN_NOTHING
import com.qriz.app.feature.onboard.model.SurveyEffect
import com.qriz.app.feature.onboard.model.SurveyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) : ViewModel() {
    private val _state: MutableStateFlow<SurveyState> = MutableStateFlow(
        SurveyState(
            concepts = SURVEY_ITEMS,
            checked = emptyList(),
        )
    )

    val state: StateFlow<SurveyState> = _state.asStateFlow()

    private val _effect: MutableSharedFlow<SurveyEffect> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    val effect = _effect.asSharedFlow()

    fun toggleConcept(concept: String, isChecked: Boolean) {
        when (concept) {
            KNOWN_ALL -> {
                if (isChecked) {
                    val knownAllConceptList =
                        SURVEY_ITEMS.toMutableList().apply { remove(KNOWN_NOTHING) }
                    updateChecked(knownAllConceptList)
                } else {
                    //체크가 되었다가 풀린 상태로 우선 전부 체크 해제
                    updateChecked(emptyList())
                }
            }

            KNOWN_NOTHING -> {
                if (isChecked) {
                    updateChecked(listOf(KNOWN_NOTHING))
                } else {
                    //체크가 되었다가 풀린 상태로 우선 전부 체크 해제
                    updateChecked(emptyList())
                }
            }

            else -> {
                val newChecked = state.value.checked.toMutableList()

                if (isChecked) {
                    val checked = newChecked.apply {
                        remove(KNOWN_NOTHING)
                        add(concept)
                        if (size == SURVEY_ITEMS.size - NOT_USE_COUNT) {
                            add(KNOWN_ALL)
                        }
                    }
                    updateChecked(checked)
                } else {
                    val checked = newChecked.apply {
                        remove(concept)
                        remove(KNOWN_ALL)
                    }
                    updateChecked(checked)
                }
            }
        }
    }

    fun submit() {
        //서버에서 의미없는 값 삭제
        val checked = state.value.checked.toMutableList().apply {
            remove(KNOWN_NOTHING)
            remove(KNOWN_ALL)
        }

        viewModelScope.launch {
            runCatching {
                onBoardingRepository.submitSurvey(checked)
            }.onSuccess {
                _effect.tryEmit(SurveyEffect.Complete)
            }.onFailure { throwable ->
                _effect.tryEmit(
                    SurveyEffect.Error(
                        throwable.message ?: "알 수 없는 에러가 발생했습니다."
                    )
                )
            }
        }
    }

    private fun updateChecked(list: List<String>) {
        _state.update { it.copy(checked = list) }
    }

    companion object {
        val NOT_USE_COUNT = 2
    }
}
