package com.qriz.app.feature.clip.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.qriz.app.core.data.clip.clip_api.repository.ClipRepository
import com.qriz.app.core.data.conceptbook.conceptbook_api.repository.ConceptBookRepository
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.navigation.route.ClipRoute
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.clip.model.toDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClipDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val clipRepository: ClipRepository,
    private val conceptBookRepository: ConceptBookRepository,
) : BaseViewModel<ClipDetailUiState, ClipDetailUiEffect, ClipDetailUiAction>(
    ClipDetailUiState.Loading
) {
    private val id = savedStateHandle.toRoute<ClipRoute.ClipDetail>().id

    val state = flow {
        val subjects = conceptBookRepository.getData()
        emit(Pair(clipRepository.getClipDetail(id), subjects))
    }.map { (clipDetailResult, subjects) ->
        when (clipDetailResult) {
            is ApiResult.Success -> ClipDetailUiState.Success(
                clipDetail = clipDetailResult.data.toDetailUiModel(subjects)
            )
            is ApiResult.Failure -> ClipDetailUiState.Failure(
                message = clipDetailResult.message
            )
            is ApiResult.NetworkError -> ClipDetailUiState.Failure(
                message = NETWORK_IS_UNSTABLE
            )
            is ApiResult.UnknownError -> {
                ClipDetailUiState.Failure(
                    message = UNKNOWN_ERROR
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ClipDetailUiState.Loading,
    )

    override fun process(action: ClipDetailUiAction): Job = viewModelScope.launch {
        when (action) {
            is ClipDetailUiAction.ClickBack -> sendEffect(ClipDetailUiEffect.MoveBack)
            is ClipDetailUiAction.ClickConceptCard -> sendEffect(ClipDetailUiEffect.MoveToConceptBookDetail(action.skillId))
            is ClipDetailUiAction.ClickGoToStudy -> sendEffect(ClipDetailUiEffect.MoveToConceptBook)
        }
    }
}
