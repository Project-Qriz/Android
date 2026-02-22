package com.qriz.app.feature.clip

import androidx.lifecycle.viewModelScope
import com.qriz.app.core.data.clip.clip_api.model.Clip
import com.qriz.app.core.data.clip.clip_api.model.ClipFilterCategory
import com.qriz.app.core.data.clip.clip_api.model.ClipFilterConcept
import com.qriz.app.core.data.clip.clip_api.model.ClipFilterSubject
import com.qriz.app.core.data.clip.clip_api.repository.ClipRepository
import com.qriz.app.core.data.conceptbook.conceptbook_api.model.Subject
import com.qriz.app.core.data.conceptbook.conceptbook_api.repository.ConceptBookRepository
import com.qriz.app.core.data.daily_study.daily_study_api.model.DailyStudyPlan
import com.qriz.app.core.data.daily_study.daily_study_api.repository.DailyStudyRepository
import com.qriz.app.core.domain.usecase_api.clip.GetClipDaysUseCase
import com.qriz.app.core.domain.usecase_api.clip.GetClipSessionsUseCase
import com.qriz.app.core.domain.usecase_api.daily_study.GetDailyStudyPlanUseCase
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.base.BaseViewModel
import com.qriz.app.feature.clip.model.ClipDataUiState
import com.qriz.app.feature.clip.model.toUiModel
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ClipViewModel @Inject constructor(
    private val clipRepository: ClipRepository,
    private val conceptBookRepository: ConceptBookRepository,
    private val getDailyStudyPlanUseCase: GetDailyStudyPlanUseCase,
    private val userRepository: UserRepository,
    clipDaysUseCase: GetClipDaysUseCase,
    clipSessionsUseCase: GetClipSessionsUseCase,
) : BaseViewModel<ClipUiState, ClipUiEffect, ClipUiAction>(
    ClipUiState.Default
) {
    private val dailyStudyClipRetry = MutableSharedFlow<Unit>()
    private val mockTestClipRetry = MutableSharedFlow<Unit>()

    private val dailyStudyFilter = MutableStateFlow<List<String>>(emptyList())
    private val mockTestFilter = MutableStateFlow<List<String>>(emptyList())

    private val clipDays = clipDaysUseCase().shareIn(
        viewModelScope,
        SharingStarted.Lazily,
        replay = 1
    )

    private val clipSessions = clipSessionsUseCase().shareIn(
        viewModelScope,
        SharingStarted.Lazily,
        replay = 1
    )

    private val dailyStudyClipUiState = clipDays.flatMapLatest { info ->
        if (info !is ApiResult.Success) {
            flowOf(info.toFailureUiState())
        } else {
            updateState {
                copy(selectedClipDay = selectedClipDay.ifEmpty { info.data.firstOrNull() ?: "" })
            }
            buildClipDataUiStateFlow(
                info = info.data,
                stateSelector = {
                    Pair(
                        it.selectedClipDay,
                        it.dailyStudyClipOnlyIncorrect
                    )
                },
                filterFlow = dailyStudyFilter,
                fetcher = { testInfo, onlyIncorrect, keyConcepts ->
                    clipRepository.getDailyStudyClips(
                        testInfo = testInfo,
                        onlyIncorrect = onlyIncorrect,
                        keyConcepts = keyConcepts
                    )
                },
            )
        }
    }

    private val mockTestClipUiState = clipSessions.flatMapLatest { info ->
        if (info !is ApiResult.Success) {
            flowOf(info.toFailureUiState())
        } else {
            updateState {
                copy(selectedClipSession = selectedClipSession.ifEmpty {
                    info.data.firstOrNull() ?: ""
                })
            }
            buildClipDataUiStateFlow(
                info = info.data,
                stateSelector = {
                    Pair(
                        it.selectedClipSession,
                        it.dailyMockTestOnlyIncorrect
                    )
                },
                filterFlow = mockTestFilter,
                fetcher = { testInfo, onlyIncorrect, keyConcepts ->
                    clipRepository.getMockTestClips(
                        testInfo = testInfo,
                        onlyIncorrect = onlyIncorrect,
                        keyConcepts = keyConcepts
                    )
                },
            )
        }
    }

    val state: StateFlow<ClipUiState> = combine(
        uiState,
        clipDays,
        clipSessions,
        dailyStudyClipUiState,
        mockTestClipUiState,
    ) { uiState, clipDays, clipSessions, dailyStudyClipUiState, mockTestClipUiState ->
        if (clipDays !is ApiResult.Success || clipSessions !is ApiResult.Success) {
            uiState
        } else {
            val days = clipDays.data
            val sessions = clipSessions.data
            uiState.copy(
                clipDays = days.toImmutableList(),
                clipSessions = sessions.toImmutableList(),
                dailyStudyClipUiState = dailyStudyClipUiState,
                mockTestClipUiState = mockTestClipUiState,
            )
        }
    }.stateIn(
        scope = viewModelScope,
        initialValue = ClipUiState.Default,
        started = SharingStarted.Eagerly,
    )

    private fun <T> ApiResult<T>.toFailureUiState(): ClipDataUiState = when (this) {
        is ApiResult.Failure -> ClipDataUiState.Failure(message)
        is ApiResult.NetworkError -> ClipDataUiState.Failure(NETWORK_IS_UNSTABLE)
        is ApiResult.UnknownError -> ClipDataUiState.Failure(UNKNOWN_ERROR)
        is ApiResult.Success -> error("unreachable")
    }

    private fun List<Subject>.toClipFilters(clips: List<Clip>): List<ClipFilterSubject> {
        val enabledConcepts = clips.map { it.keyConcepts }.toSet()
        return map { subject ->
            ClipFilterSubject(
                number = subject.number,
                categories = subject.categories.map { category ->
                    ClipFilterCategory(
                        name = category.name,
                        concepts = category.conceptBooks.map { conceptBook ->
                            ClipFilterConcept(
                                name = conceptBook.name,
                                enable = conceptBook.name in enabledConcepts,
                            )
                        },
                    )
                },
            )
        }
    }

    private fun buildClipDataUiStateFlow(
        info: List<String>,
        stateSelector: (ClipUiState) -> Pair<String, Boolean>,
        filterFlow: Flow<List<String>>,
        fetcher: suspend (testInfo: String, onlyIncorrect: Boolean, keyConcepts: List<String>) -> ApiResult<List<Clip>>,
    ): Flow<ClipDataUiState> = combine(
        uiState.map(stateSelector).distinctUntilChanged(),
        filterFlow,
    ) { (testInfo, onlyIncorrect), keyConcepts ->
        Triple(
            testInfo,
            onlyIncorrect,
            keyConcepts
        )
    }.distinctUntilChanged().mapLatest { (testInfo, onlyIncorrect, keyConcepts) ->
            val result = fetcher(
                testInfo,
                onlyIncorrect,
                keyConcepts
            )
            val conceptBook = conceptBookRepository.getData()
            when (result) {
                is ApiResult.Success -> ClipDataUiState.Success(
                    clips = result.data.map { it.toUiModel() }.toImmutableList(),
                    filter = conceptBook.toClipFilters(result.data)
                        .map { it.toUiModel(keyConcepts) }.toImmutableList(),
                    onlyIncorrect = onlyIncorrect,
                    info = info.toImmutableList(),
                )

                is ApiResult.Failure -> ClipDataUiState.Failure(result.message)
                is ApiResult.NetworkError -> ClipDataUiState.Failure(NETWORK_IS_UNSTABLE)
                is ApiResult.UnknownError -> ClipDataUiState.Failure(UNKNOWN_ERROR)
            }
        }

    override fun process(action: ClipUiAction): Job = viewModelScope.launch {
        when (action) {
            is ClipUiAction.Init -> {}
            is ClipUiAction.TabDailyStudy -> updateState { copy(page = ClipUiState.DAILY_STUDY) }
            is ClipUiAction.TabMockTest -> updateState { copy(page = ClipUiState.MOCK_TEST) }
            is ClipUiAction.ClickFirstSubjectFilter -> updateState {
                if (action.tab == ClipUiState.DAILY_STUDY) {
                    copy(
                        dailyStudyShowFilterBottomSheet = true,
                        dailyStudyFilterInitialPage = 0
                    )
                } else {
                    copy(
                        mockTestShowFilterBottomSheet = true,
                        mockTestFilterInitialPage = 0
                    )
                }
            }

            is ClipUiAction.ClickSecondSubjectFilter -> updateState {
                if (action.tab == ClipUiState.DAILY_STUDY) {
                    copy(
                        dailyStudyShowFilterBottomSheet = true,
                        dailyStudyFilterInitialPage = 1
                    )
                } else {
                    copy(
                        mockTestShowFilterBottomSheet = true,
                        mockTestFilterInitialPage = 1
                    )
                }
            }

            is ClipUiAction.DismissFilterBottomSheet -> updateState {
                if (action.tab == ClipUiState.DAILY_STUDY) {
                    copy(dailyStudyShowFilterBottomSheet = false)
                } else {
                    copy(mockTestShowFilterBottomSheet = false)
                }
            }

            is ClipUiAction.SelectClipDay -> updateState { copy(selectedClipDay = action.day) }
            is ClipUiAction.SelectClipSession -> updateState { copy(selectedClipSession = action.session) }
            is ClipUiAction.ApplyDailyStudyFilter -> {
                dailyStudyFilter.value = action.concepts
                updateState { copy(dailyStudyShowFilterBottomSheet = false) }
            }

            is ClipUiAction.ApplyMockTestFilter -> {
                mockTestFilter.value = action.concepts
                updateState { copy(mockTestShowFilterBottomSheet = false) }
            }

            is ClipUiAction.ToggleOnlyIncorrect -> updateState {
                if (action.tab == ClipUiState.DAILY_STUDY) {
                    copy(dailyStudyClipOnlyIncorrect = action.onlyIncorrect)
                } else {
                    copy(dailyMockTestOnlyIncorrect = action.onlyIncorrect)
                }
            }

            is ClipUiAction.MoveToDailyStudy -> moveToDailyStudy()

            is ClipUiAction.MoveToMockTestSessions -> moveToMockTest()
        }
    }

    private suspend fun moveToDailyStudy() {
        if (userRepository.getUserFlow().first().previewTestStatus.isNeedPreviewTest()) {
            ClipUiEffect.ShowSnackBar(defaultResId = R.string.unavailable_daily_study)
            return
        }

        when (val result = getDailyStudyPlanUseCase().first()) {
            is ApiResult.Success<List<DailyStudyPlan>> -> {
                sendEffect(
                    ClipUiEffect.MoveToDailyStudy(
                        dayNumber = 1,
                        isReview = false,
                        isComprehensiveReview = false
                    )
                )
            }

            is ApiResult.Failure -> sendEffect(
                ClipUiEffect.ShowSnackBar(
                    defaultResId = com.qriz.app.core.designsystem.R.string.empty,
                    message = result.message
                )
            )

            is ApiResult.NetworkError -> sendEffect(
                ClipUiEffect.ShowSnackBar(
                    defaultResId = com.qriz.app.core.designsystem.R.string.empty,
                    message = NETWORK_IS_UNSTABLE
                )
            )

            is ApiResult.UnknownError -> sendEffect(
                ClipUiEffect.ShowSnackBar(
                    defaultResId = com.qriz.app.core.designsystem.R.string.empty,
                    message = UNKNOWN_ERROR
                )
            )
        }
    }

    private suspend fun moveToMockTest() {
        if (userRepository.getUserFlow().first().previewTestStatus.isNeedPreviewTest()) {
            ClipUiEffect.ShowSnackBar(defaultResId = R.string.unavailable_mock_test)
            return
        }

        sendEffect(ClipUiEffect.MoveToMockTestSessions)
    }
}
