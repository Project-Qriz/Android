package preview

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.data.test.test_api.model.Option
import com.qriz.app.core.data.test.test_api.model.Question
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.feature.onboard.preview.PreviewUiAction
import com.qriz.app.feature.onboard.preview.PreviewUiEffect
import com.qriz.app.feature.onboard.preview.PreviewUiState
import com.qriz.app.feature.onboard.preview.PreviewViewModel
import com.qriz.app.feature.onboard.preview.PreviewViewModel.Companion.IS_TEST_FLAG
import com.qriz.app.feature.onboard.preview.PreviewViewModel.Companion.toMilliSecond
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import com.qriz.app.core.data.test.test_api.model.Test as TestModel

class PreviewViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val fakeOnBoardRepository = mockk<OnBoardRepository>()

    private fun TestScope.previewViewModel() = PreviewViewModel(
        savedStateHandle = SavedStateHandle().apply {
            set(IS_TEST_FLAG, true)
        },
        onBoardRepository = fakeOnBoardRepository
    )

    @Test
    fun `Action_LoadPreviewTest process 성공 - 상태 업데이트, 타이머 시작`() = runTest {
        with(previewViewModel()) {
            // given
            coEvery { fakeOnBoardRepository.getPreviewTest() } returns fakeTest

            // when
            process(PreviewUiAction.LoadPreviewTest)

            // then
            uiState.test {
                with(awaitItem()) {
                    questions shouldBe fakeTest.questions.toImmutableList()
                    remainTimeMs shouldBe fakeTest.totalTimeLimit.toMilliSecond()
                    totalTimeLimitMs shouldBe fakeTest.totalTimeLimit.toMilliSecond()
                    isLoading shouldBe false
                }
                timerJob.shouldNotBeNull()
            }
        }
    }

    @Test
    fun `Action_LoadPreviewTest process 실패 - Effect_ShowSnackBar 발생`() = runTest {
        with(previewViewModel()) {
            // given
            coEvery { fakeOnBoardRepository.getPreviewTest() } throws Exception()
            // when
            process(PreviewUiAction.LoadPreviewTest)
            //then
            uiState.test { awaitItem().isLoading shouldBe false }
            effect.test { (awaitItem() is PreviewUiEffect.ShowSnackBar) shouldBe true }
        }
    }

    @Test
    fun `Action_LoadPreviewTest process 로딩 상태 - API호출되지 않음`() = runTest {
        // given
        coEvery { fakeOnBoardRepository.getPreviewTest() } returns fakeTest
        val previewViewModel = object : PreviewViewModel(
            savedStateHandle = SavedStateHandle().apply { set(IS_TEST_FLAG, true) },
            onBoardRepository = fakeOnBoardRepository
        ) {
            init {
                updateState { copy(isLoading = true) }
            }
        }
        with(previewViewModel) {
            // when
            process(PreviewUiAction.LoadPreviewTest)
            //then
            coVerify(exactly = 0) { fakeOnBoardRepository.getPreviewTest() }
        }
    }

    @Test
    fun `Action_SelectOption process - 해당 질문의 선택된 옵션이 업데이트 됨`() = runTest {
        with(previewViewModel()) {
            // given
            val selectedQuestion = fakeTest.questions.first()
            val selectedOption = selectedQuestion.options.first()
            process(
                PreviewUiAction.SelectOption(
                    questionID = selectedQuestion.id,
                    option = selectedOption
                )
            )

            // when & then
            uiState.test {
                awaitItem().selectedOptions[selectedQuestion.id] shouldBe selectedOption
            }
        }
    }

    @Test
    fun `Action_ClickNextPage process - 페이지 인덱스가 1씩 증가함`() = runTest {
        with(previewViewModel()) {
            // given
            process(PreviewUiAction.ClickNextPage)

            // when & then
            uiState.test {
                awaitItem().currentIndex shouldBe PreviewUiState.Default.currentIndex + 1
            }
        }
    }

    @Test
    fun `Action_ClickPreviousPage process - 페이지 인덱스가 1씩 감소함`() = runTest {
        with(previewViewModel()) {
            // given
            process(PreviewUiAction.ClickPreviousPage)

            // when & then
            uiState.test {
                awaitItem().currentIndex shouldBe PreviewUiState.Default.currentIndex - 1
            }
        }
    }

    @Test
    fun `Action_ClickSubmit process 성공 - 로딩 해제, Effect_MoveToGuide 발생`() = runTest {
        with(previewViewModel()) {
            // given
            coEvery { fakeOnBoardRepository.submitPreviewTest(any()) } returns Unit

            // when
            process(PreviewUiAction.ClickSubmit)

            // then
            uiState.test { awaitItem().isLoading shouldBe false }
            effect.test { (awaitItem() is PreviewUiEffect.MoveToGuide) shouldBe true }
        }
    }

    @Test
    fun `Action_ClickSubmit process 실패 - 로딩 해제, Effect_ShowSnackBar 발생`() = runTest {
        with(previewViewModel()) {
            // given
            coEvery { fakeOnBoardRepository.submitPreviewTest(any()) } throws Exception()

            // when
            process(PreviewUiAction.ClickSubmit)

            // then
            uiState.test { awaitItem().isLoading shouldBe false }
            effect.test { (awaitItem() is PreviewUiEffect.ShowSnackBar) shouldBe true }
        }
    }

    @Test
    fun `Action_ClickSubmit process 로딩 상태 - API호출되지 않음`() = runTest {
        // given
        val previewViewModel = object : PreviewViewModel(
            savedStateHandle = SavedStateHandle().apply { set(IS_TEST_FLAG, true) },
            onBoardRepository = fakeOnBoardRepository
        ) {
            init {
                updateState { copy(isLoading = true) }
            }
        }
        with(previewViewModel) {
            // when
            process(PreviewUiAction.ClickSubmit)
            //then
            coVerify(exactly = 0) { fakeOnBoardRepository.submitPreviewTest(any()) }
        }
    }

    @Test
    fun `Action_ClickSubmit process 풀리지 않은 문제가 있음 - API호출되지 않음, Effect_ShowSnackBar 발생`() =
        runTest {
            with(previewViewModel()) {
                // given
                coEvery { fakeOnBoardRepository.getPreviewTest() } returns fakeTest
                process(PreviewUiAction.LoadPreviewTest)

                fakeTest.questions.dropLast(1).forEach { selectedQuestion ->
                    process(
                        PreviewUiAction.SelectOption(
                            questionID = selectedQuestion.id,
                            option = selectedQuestion.options.first()
                        )
                    )
                }

                coEvery { fakeOnBoardRepository.submitPreviewTest(any()) } returns Unit

                // when
                process(PreviewUiAction.ClickSubmit)
                //then
                coVerify(exactly = 0) { fakeOnBoardRepository.submitPreviewTest(any()) }
                effect.test { (awaitItem() is PreviewUiEffect.ShowSnackBar) shouldBe true }
            }
        }

    @Test
    fun `Action_ClickCancel process - Effect_MoveToBack 발생`() = runTest {
        with(previewViewModel()) {
            // given
            process(PreviewUiAction.ClickCancel)
            // when & then
            effect.test { (awaitItem() is PreviewUiEffect.MoveToBack) shouldBe true }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `타이머가 시작 후 - remainTimeMs는 1초씩 감소`() = runTest {
        with(previewViewModel()) {
            // given
            coEvery { fakeOnBoardRepository.getPreviewTest() } returns fakeTest
            process(PreviewUiAction.LoadPreviewTest)
            (1..5).forEach { seconds ->
                // when
                advanceTimeBy(1010)
                // then
                uiState.test {
                    with(awaitItem()) {
                        remainTimeMs shouldBe totalTimeLimitMs - 1000 * seconds
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `타이머 만료 - 체크하지 않은 문제는 공백 처리 후, submitPreviewTest 호출`() = runTest {
        with(previewViewModel()) {
            // given
            coEvery { fakeOnBoardRepository.getPreviewTest() } returns fakeTest
            process(PreviewUiAction.LoadPreviewTest)

            fakeTest.questions.dropLast(1).forEach { selectedQuestion ->
                process(
                    PreviewUiAction.SelectOption(
                        questionID = selectedQuestion.id,
                        option = selectedQuestion.options.first()
                    )
                )
            }
            val expected = fakeTest.questions.associate {
                it.id to it.options.first()
            } + mapOf(fakeTest.questions.last().id to Option(""))

            // when
            advanceTimeBy(fakeTest.totalTimeLimit.toMilliSecond() + 100)

            // then
            uiState.test { awaitItem().remainTimeMs shouldBe 0 }
            coVerify { fakeOnBoardRepository.submitPreviewTest(expected) }
        }
    }

    companion object {
        val fakeTest = TestModel(
            questions = listOf(
                Question(
                    id = 1,
                    question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?",
                    options = listOf(
                        Option("트랜잭션을 더 작은 단위로 분할"),
                        Option("트랜잭션의 타임아웃 시간을 늘림"),
                        Option("모든 데이터를 메모리에 로드"),
                        Option("트랜잭션의 격리 수준을 낮춤"),
                    ),
                    timeLimit = 60,
                ),
                Question(
                    id = 2,
                    question = "다음 중 트랜잭션 모델링에서 '긴 트랜잭션(Long Transaction)'을 처리하는 방법으로 가장 적절한 것은?@2",
                    options = listOf(
                        Option("트랜잭션을 더 작은 단위로 분할#2"),
                        Option("트랜잭션의 타임아웃 시간을 늘림#2"),
                        Option("모든 데이터를 메모리에 로드#2"),
                        Option("트랜잭션의 격리 수준을 낮춤#2"),
                    ),
                    timeLimit = 60,
                ),
            ),
            totalTimeLimit = 120 //2분
        )
    }
}
