import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.feature.splash.SplashUiAction
import com.qriz.app.feature.splash.SplashUiEffect
import com.qriz.app.feature.splash.SplashViewModel
import com.qriz.app.feature.splash.SplashViewModel.Companion.IS_TEST_FLAG
import com.qriz.core.data.token.token_api.TokenRepository
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val fakeTokenRepository = mockk<TokenRepository>()

    private fun TestScope.splashViewModel() = SplashViewModel(
        savedStateHandle = SavedStateHandle().apply {
            set(IS_TEST_FLAG, true)
        },
        tokenRepository = fakeTokenRepository
    )

    @Test
    fun `Action_OCheckLoginState process - Effect_MoveToMain 발생`() = runTest {
        with(splashViewModel()) {
            // given
            every { fakeTokenRepository.flowTokenExist } returns flowOf(false)
            // when
            process(SplashUiAction.CheckLoginState)
            // then
            effect.test {
                awaitItem() shouldBe SplashUiEffect.MoveToMain(false)
            }
        }
    }

}
