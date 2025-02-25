import app.cash.turbine.test
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.feature.splash.SplashUiAction
import com.qriz.app.feature.splash.SplashUiEffect
import com.qriz.app.feature.splash.SplashViewModel
import com.qriz.core.data.token.token_api.TokenRepository
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus.NOT_STARTED
import com.quiz.app.core.data.user.user_api.model.User
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val fakeUserRepository = mockk<UserRepository>()
    private val fakeTokenRepository = mockk<TokenRepository>()

    private fun TestScope.splashViewModel() = SplashViewModel(
        tokenRepository = fakeTokenRepository,
        userRepository = fakeUserRepository
    )

    @Test
    fun `Action_StartLogin process 토큰이 존재하지 않음 - Effect_MoveToLogin 발생`() = runTest {
        with(splashViewModel()) {
            // given
            coEvery { fakeTokenRepository.isTokenExist() } returns false
            // when
            process(SplashUiAction.StartLogin)
            // then
            effect.test { awaitItem() shouldBe SplashUiEffect.MoveToLogin }
        }
    }

    @Test
    fun `Action_StartLogin process 유저가 Survey를 완료하지않음 - Effect_MoveToSurvey 발생`() = runTest {
        with(splashViewModel()) {
            // given
            coEvery { fakeTokenRepository.isTokenExist() } returns true
            coEvery { fakeUserRepository.getUser() } returns User.Default.copy(
                previewTestStatus = NOT_STARTED
            )
            // when
            process(SplashUiAction.StartLogin)
            // then
            effect.test { awaitItem() shouldBe SplashUiEffect.MoveToSurvey }
        }
    }

    @Test
    fun `Action_StartLogin process 유저가 Survey를 완료함 - Effect_MoveToMain 발생`() = runTest {
        val states = PreviewTestStatus.entries.filter { it != NOT_STARTED }
        for (previewTestStatus in states) {
            with(splashViewModel()) {
                // given
                coEvery { fakeTokenRepository.isTokenExist() } returns true
                coEvery { fakeUserRepository.getUser() } returns User.Default.copy(
                    previewTestStatus = previewTestStatus
                )
                // when
                process(SplashUiAction.StartLogin)
                // then
                effect.test { awaitItem() shouldBe SplashUiEffect.MoveToMain() }
            }
        }
    }

    @Test
    fun `Action_StartLogin process API 실패 - Effect_MoveToLogin 발생`() = runTest {
        with(splashViewModel()) {
            // given
            coEvery { fakeTokenRepository.isTokenExist() } returns true
            coEvery { fakeUserRepository.getUser() } throws Exception()
            // when
            process(SplashUiAction.StartLogin)
            // then
            effect.test { awaitItem() shouldBe SplashUiEffect.MoveToLogin }
        }
    }
}
