package signin

import app.cash.turbine.test
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signin.SignInUiAction
import com.qriz.app.feature.sign.signin.SignInUiEffect
import com.qriz.app.feature.sign.signin.SignInViewModel
import com.quiz.app.core.data.user.user_api.model.User
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class SignInViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val fakeUserRepository = mockk<UserRepository>()

    private fun TestScope.signInViewModel() = SignInViewModel(
        userRepository = fakeUserRepository
    )

    @Test
    fun `Action_ChangeUserId process - id, 에러메세지 업데이트`() = runTest {
        with(signInViewModel()) {
            // given
            val userId = "chaeunwoo123"
            // when
            process(SignInUiAction.ChangeUserId(userId))
            // then
            uiState.test {
                with(awaitItem()) {
                    id shouldBe userId
                    loginErrorMessageResId shouldBe R.string.empty
                }
            }
        }
    }

    @Test
    fun `Action_ChangeUserPw process - pw, 에러메세지 업데이트`() = runTest {
        with(signInViewModel()) {
            // given
            val userPw = "asfasf12314"
            // when
            process(SignInUiAction.ChangeUserPw(userPw))
            // then
            uiState.test {
                with(awaitItem()) {
                    pw shouldBe userPw
                    loginErrorMessageResId shouldBe R.string.empty
                }
            }
        }
    }

    @Test
    fun `Action_ClickPwVisibility process - isVisiblePw 업데이트`() = runTest {
        with(signInViewModel()) {
            // given
            val isVisiblePw = true
            // when
            process(SignInUiAction.ClickPwVisibility(isVisiblePw))
            // then
            uiState.test {
                awaitItem().isVisiblePw shouldBe isVisiblePw
            }
        }
    }

    @Test
    fun `Action_ClickLogin process 성공 - Effect_MoveToHome 발생`() = runTest {
        with(signInViewModel()) {
            // given
            val userId = "test123"
            val userPw = "tasfasfasf"
            process(SignInUiAction.ChangeUserId(userId))
            process(SignInUiAction.ChangeUserPw(userPw))

            coEvery { fakeUserRepository.login(userId, userPw) } returns ApiResult.Success(User.Default)
            // when
            process(SignInUiAction.ClickLogin)
            // then
            uiState.test { awaitItem().isLoading shouldBe false }
            effect.test { awaitItem() shouldBe SignInUiEffect.MoveToHome }
        }
    }

    @Test
    fun `Action_ClickLogin process Failure - loginErrorMessageResId 업데이트`() = runTest {
        with(signInViewModel()) {
            // given
            val userId = "test123"
            val userPw = "tasfasfasf"
            process(SignInUiAction.ChangeUserId(userId))
            process(SignInUiAction.ChangeUserPw(userPw))
            coEvery { fakeUserRepository.login(userId, userPw) } returns ApiResult.Failure(code = -1, message = "일치하는 회원 정보가 없습니다")
            // when
            process(SignInUiAction.ClickLogin)
            // then
            uiState.test {
                with(awaitItem()) {
                    loginErrorMessageResId shouldBe R.string.user_not_registered
                    isLoading shouldBe false
                }
            }
        }
    }

    @Test
    fun `Action_ClickLogin process NetworkError - loginErrorMessageResId 업데이트`() = runTest {
        with(signInViewModel()) {
            // given
            val userId = "test123"
            val userPw = "tasfasfasf"
            process(SignInUiAction.ChangeUserId(userId))
            process(SignInUiAction.ChangeUserPw(userPw))
            coEvery { fakeUserRepository.login(userId, userPw) } returns ApiResult.NetworkError(IOException())
            // when
            process(SignInUiAction.ClickLogin)
            // then
            uiState.test {
                with(awaitItem()) {
                    loginErrorMessageResId shouldBe com.qriz.app.core.ui.common.R.string.check_network_and_try_again
                    isLoading shouldBe false
                }
            }
        }
    }

    @Test
    fun `Action_ClickLogin process UnknownError - loginErrorMessageResId 업데이트`() = runTest {
        with(signInViewModel()) {
            // given
            val userId = "test123"
            val userPw = "tasfasfasf"
            process(SignInUiAction.ChangeUserId(userId))
            process(SignInUiAction.ChangeUserPw(userPw))
            coEvery { fakeUserRepository.login(userId, userPw) } returns ApiResult.UnknownError(throwable = null)
            // when
            process(SignInUiAction.ClickLogin)
            // then
            uiState.test {
                with(awaitItem()) {
                    loginErrorMessageResId shouldBe com.qriz.app.core.ui.common.R.string.unknown_error_occurs
                    isLoading shouldBe false
                }
            }
        }
    }

    @Test
    fun `Action_ClickSignUp process - Effect_MoveToSignUp 발생`() = runTest {
        with(signInViewModel()) {
            // given
            process(SignInUiAction.ClickSignUp)
            // when & then
            effect.test { awaitItem() shouldBe SignInUiEffect.MoveToSignUp }
        }
    }

    @Test
    fun `Action_ClickFindId process - Effect_MoveToFindId 발생`() = runTest {
        with(signInViewModel()) {
            // given
            process(SignInUiAction.ClickFindId)
            // when & then
            effect.test { awaitItem() shouldBe SignInUiEffect.MoveToFindId }
        }
    }

    @Test
    fun `Action_ClickFindPw process - Effect_MoveToFindPw 발생`() = runTest {
        with(signInViewModel()) {
            // given
            process(SignInUiAction.ClickFindPw)
            // when & then
            effect.test { awaitItem() shouldBe SignInUiEffect.MoveToFindPw }
        }
    }
}
