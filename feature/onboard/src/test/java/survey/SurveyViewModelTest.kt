package survey

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.qriz.app.core.data.onboard.onboard_api.model.PreCheckConcept
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.feature.onboard.survey.SurveyUiAction
import com.qriz.app.feature.onboard.survey.SurveyViewModel
import com.qriz.app.feature.onboard.survey.SurveyViewModel.Companion.IS_TEST_FLAG
import com.qriz.app.feature.onboard.survey.model.SurveyListItem
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class SurveyViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val fakeOnBoardRepository = mockk<OnBoardRepository>()

    private fun TestScope.surveyViewModel() = SurveyViewModel(
        savedStateHandle = SavedStateHandle().apply {
            set(IS_TEST_FLAG, true)
        },
        onBoardRepository = fakeOnBoardRepository
    )

    @Test
    fun `Action_ObserveSurveyItems process - 아무 것도 체크 되지 않은 채로 Concept 로드`() = runTest {
        with(surveyViewModel()) {
            // given
            process(SurveyUiAction.ObserveSurveyItems)
            // when & then
            uiState.test {
                with(awaitItem()) {
                    surveyItems.isNotEmpty() shouldBe true
                    surveyItems.count { it.isChecked } shouldBe 0
                }
            }
        }
    }

    @Test
    fun `Action_ClickKnowsAll process 체크 - KnowsNothing를 제외한 모든 Concept 체크`() = runTest {
        with(surveyViewModel()) {
            // given
            process(SurveyUiAction.ObserveSurveyItems)
            process(SurveyUiAction.ClickKnowsAll(true))
            // when & then
            uiState.test {
                with(awaitItem()) {
                    surveyItems.isNotEmpty() shouldBe true
                    surveyItems.first { it is SurveyListItem.KnowsAll }.isChecked shouldBe true
                    surveyItems.first { it is SurveyListItem.KnowsNothing }.isChecked shouldBe false
                    surveyItems.count { it.isChecked } shouldBe surveyItems.size - 1
                }
            }
        }
    }

    @Test
    fun `Action_ClickKnowsAll process 체크해제 - 모든 Concept 체크해제`() = runTest {
        with(surveyViewModel()) {
            // given
            process(SurveyUiAction.ObserveSurveyItems)
            process(SurveyUiAction.ClickKnowsAll(false))
            // when & then
            uiState.test {
                with(awaitItem()) {
                    surveyItems.isNotEmpty() shouldBe true
                    surveyItems.count { it.isChecked } shouldBe 0
                }
            }
        }
    }

    @Test
    fun `Action_ClickKnowsNothing process 체크 - KnowsNothing를 제외한 모든 Concept 체크해제`() = runTest {
        with(surveyViewModel()) {
            // given
            process(SurveyUiAction.ObserveSurveyItems)
            process(SurveyUiAction.ClickKnowsNothing(true))
            // when & then
            uiState.test {
                with(awaitItem()) {
                    surveyItems.isNotEmpty() shouldBe true
                    surveyItems.first { it is SurveyListItem.KnowsNothing }.isChecked shouldBe true
                    surveyItems.count { it.isChecked } shouldBe 1
                }
            }
        }
    }

    @Test
    fun `Action_ClickKnowsNothing process 체크해제 - 모든 Concept 체크해제`() = runTest {
        with(surveyViewModel()) {
            // given
            process(SurveyUiAction.ObserveSurveyItems)
            process(SurveyUiAction.ClickKnowsNothing(false))
            // when & then
            uiState.test {
                with(awaitItem()) {
                    surveyItems.isNotEmpty() shouldBe true
                    surveyItems.count { it.isChecked } shouldBe 0
                }
            }
        }
    }

    @Test
    fun `Action_ClickConcept process 체크 - KnowsNothing는 해제되고 선택된 Concept 체크`() = runTest {
        with(surveyViewModel()) {
            // given
            process(SurveyUiAction.ObserveSurveyItems)
            for (concept in fakeSelectedConcepts) {
                process(
                    SurveyUiAction.ClickConcept(
                        preCheckConcept = concept,
                        isChecked = true
                    )
                )
            }
            // when & then
            uiState.test {
                with(awaitItem()) {
                    surveyItems.isNotEmpty() shouldBe true
                    surveyItems.first { it is SurveyListItem.KnowsNothing }.isChecked shouldBe false
                    surveyItems.filterIsInstance<SurveyListItem.SurveyItem>()
                        .count { it.isChecked } shouldBe fakeSelectedConcepts.size
                }
            }
        }
    }

    @Test
    fun `Action_ClickConcept process 체크해제 - 선택된 Concept 체크해제`() = runTest {
        with(surveyViewModel()) {
            // given
            process(SurveyUiAction.ObserveSurveyItems)
            for (concept in fakeSelectedConcepts) {
                process(
                    SurveyUiAction.ClickConcept(
                        preCheckConcept = concept,
                        isChecked = true
                    )
                )
            }
            process(
                SurveyUiAction.ClickConcept(
                    preCheckConcept = fakeSelectedConcepts.first(),
                    isChecked = false
                )
            )
            // when & then
            uiState.test {
                with(awaitItem()) {
                    surveyItems.isNotEmpty() shouldBe true
                    surveyItems.first { it is SurveyListItem.KnowsNothing }.isChecked shouldBe false
                    surveyItems.filterIsInstance<SurveyListItem.SurveyItem>()
                        .count { it.isChecked } shouldBe fakeSelectedConcepts.size - 1
                }
            }
        }
    }

    @Test
    fun `Action_ClickConcept process KnowsAll와 KnowsNothing를 제외한 모든 체크 - KnowsNothing를 제외한 모든 Concept 체크`() =
        runTest {
            with(surveyViewModel()) {
                // given
                process(SurveyUiAction.ObserveSurveyItems)
                for (concept in PreCheckConcept.entries) {
                    process(
                        SurveyUiAction.ClickConcept(
                            preCheckConcept = concept,
                            isChecked = true
                        )
                    )
                }
                // when & then
                uiState.test {
                    with(awaitItem()) {
                        surveyItems.isNotEmpty() shouldBe true
                        surveyItems.first { it is SurveyListItem.KnowsAll }.isChecked shouldBe true
                        surveyItems.first { it is SurveyListItem.KnowsNothing }.isChecked shouldBe false
                        surveyItems.count { it.isChecked } shouldBe surveyItems.size - 1
                    }
                }
            }
        }

    @Test
    fun `Action_ClickSubmit process 아무것도 선택하지 않음 - submitSurvey 호출되지 않음`() = runTest {
        with(surveyViewModel()) {
            // given
            process(SurveyUiAction.ObserveSurveyItems)
            process(SurveyUiAction.ClickSubmit)
            // when & then
            coVerify(exactly = 0) { fakeOnBoardRepository.submitSurvey(emptyList()) }
        }
    }

    @Test
    fun `Action_ClickSubmit가 process 전혀 모름 선택 - submitSurvey 호출`() = runTest {
        with(surveyViewModel()) {
            // given
            process(SurveyUiAction.ObserveSurveyItems)
            process(SurveyUiAction.ClickKnowsNothing(true))
            process(SurveyUiAction.ClickSubmit)
            // when & then
            coVerify { fakeOnBoardRepository.submitSurvey(emptyList()) }
        }
    }

    @Test
    fun `Action_ClickSubmit가 process 선택된 개념이 1개 이상 - submitSurvey 호출`() = runTest {
        with(surveyViewModel()) {
            // given
            process(SurveyUiAction.ObserveSurveyItems)
            for (concept in fakeSelectedConcepts) {
                process(
                    SurveyUiAction.ClickConcept(
                        preCheckConcept = concept,
                        isChecked = true
                    )
                )
            }
            process(SurveyUiAction.ClickSubmit)
            // when & then
            coVerify { fakeOnBoardRepository.submitSurvey(fakeSelectedConcepts) }
        }
    }

    @Test
    fun `Action_ClickSubmit가 process 전부 알고있음 선택 - submitSurvey 호출`() = runTest {
        with(surveyViewModel()) {
            // given
            process(SurveyUiAction.ObserveSurveyItems)
            process(SurveyUiAction.ClickKnowsAll(true))
            process(SurveyUiAction.ClickSubmit)
            // when & then
            coVerify { fakeOnBoardRepository.submitSurvey(PreCheckConcept.entries.toList()) }
        }
    }

    companion object {
        val fakeSelectedConcepts = listOf(
            PreCheckConcept.ATTRIBUTION,
            PreCheckConcept.ENTITY
        )
    }
}
