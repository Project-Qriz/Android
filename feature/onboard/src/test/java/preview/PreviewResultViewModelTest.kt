package preview

import app.cash.turbine.test
import com.qriz.app.core.data.onboard.onboard_api.model.PreviewTestResult
import com.qriz.app.core.data.onboard.onboard_api.repository.OnBoardRepository
import com.qriz.app.core.data.test.test_api.model.SQLDConcept
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.feature.onboard.previewresult.PreviewResultUiAction
import com.qriz.app.feature.onboard.previewresult.PreviewResultUiEffect
import com.qriz.app.feature.onboard.previewresult.PreviewResultUiState
import com.qriz.app.feature.onboard.previewresult.PreviewResultUiState.State
import com.qriz.app.feature.onboard.previewresult.PreviewResultViewModel
import com.qriz.app.feature.onboard.previewresult.mapper.toPreviewTestResultItem
import com.quiz.app.core.data.user.user_api.model.User
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class PreviewResultViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val fakeUserRepository = mockk<UserRepository>()
    private val fakeOnBoardRepository = mockk<OnBoardRepository>()

    private fun TestScope.previewResultViewModel() = PreviewResultViewModel(
        userRepository = fakeUserRepository,
        onBoardRepository = fakeOnBoardRepository,
    )

    @Test
    fun `Action_ClickClose process - Effect_MoveToWelcomeGuide 발생`() = runTest {
        with(previewResultViewModel()) {
            // given
            process(PreviewResultUiAction.ClickClose)

            // then
            uiState.test { (awaitItem() == PreviewResultUiState.Default) shouldBe true }
            effect.test { (awaitItem() is PreviewResultUiEffect.MoveToWelcomeGuide) shouldBe true }
        }
    }

    @Test
    fun `Action_LoadPreviewResult process 성공 - 상태 업데이트`() = runTest {
        with(previewResultViewModel()) {
            // given
            coEvery { fakeOnBoardRepository.getPreviewTestResult() } returns fakeTestResult

            // when
            process(PreviewResultUiAction.LoadPreviewResult)

            // then
            uiState.test {
                with(awaitItem()) {
                    previewTestResultItem shouldBe fakeTestResult.toPreviewTestResultItem()
                    state shouldBe State.SUCCESS
                }
            }
        }
    }

    @Test
    fun `Action_LoadPreviewResult process 실패 - 상태 업데이트`() = runTest {
        with(previewResultViewModel()) {
            // given
            coEvery { fakeOnBoardRepository.getPreviewTestResult() } throws Exception()

            // when
            process(PreviewResultUiAction.LoadPreviewResult)

            // then
            uiState.test { awaitItem().state shouldBe State.FAILURE }
        }
    }

    @Test
    fun `Action_ObserveClient process - UserName 업데이트`() = runTest {
        // given
        val fakeUserName = "TestUser"
        val fakeUser = User.Default.copy(name = fakeUserName)
        coEvery { fakeUserRepository.getClientFlow() } returns flowOf(fakeUser)

        with(previewResultViewModel()) {
            // when
            process(PreviewResultUiAction.ObserveClient)

            // then
            uiState.test {
                with(awaitItem()) { userName shouldBe fakeUserName }
            }
        }
    }

    companion object {
        val fakeTestResult = PreviewTestResult(
            estimatedScore = 83.0F,
            totalScore = 100,
            part1Score = 48,
            part2Score = 52,
            totalQuestions = 21,
            weakAreas = listOf(),
            topConceptsToImprove = listOf(SQLDConcept.JOIN, SQLDConcept.SELECT),
        )
    }
}
