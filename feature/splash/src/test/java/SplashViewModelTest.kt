import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.feature.splash.SplashUiAction
import com.qriz.app.feature.splash.SplashUiEffect
import com.qriz.app.feature.splash.SplashViewModel
import com.qriz.app.feature.splash.SplashViewModel.Companion.IS_TEST_FLAG
import com.quiz.app.core.data.user.user_api.repository.UserRepository
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

    private val fakeUserRepository = mockk<UserRepository>()

    private fun TestScope.splashViewModel() = SplashViewModel(
        savedStateHandle = SavedStateHandle().apply {
            set(IS_TEST_FLAG, true)
        },
        userRepository = fakeUserRepository
    )

    @Test
    fun `Action_OCheckLoginState process - Effect_MoveToMain 발생`() = runTest {
        with(splashViewModel()) {
            // given
            every { fakeUserRepository.flowLogin } returns flowOf(false)
            // when
            process(SplashUiAction.CheckLoginState)
            // then
            effect.test {
                (awaitItem() is SplashUiEffect.MoveToMain) shouldBe true
            }
        }
    }

}
