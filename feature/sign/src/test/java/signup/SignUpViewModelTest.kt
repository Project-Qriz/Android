package signup

import app.cash.turbine.test
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.feature.sign.signup.SignUpUiAction
import com.qriz.app.feature.sign.signup.SignUpViewModel
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class SignUpViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val fakeUserRepository = mockk<UserRepository>()

    private fun TestScope.signUpViewModel() = SignUpViewModel(
        userRepository = fakeUserRepository
    )

    @Test
    fun `Action_ChangeUserName process 성공 - name 업데이트, validName = true`() = runTest {
        with(signUpViewModel()) {
            // given
            val fakeUserName = "차은우"
            process(SignUpUiAction.ChangeUserName(fakeUserName))
            // when & then
            uiState.test {
                with(awaitItem()) {
                    name shouldBe fakeUserName
                    isValidName shouldBe true
                }
            }
        }
    }

    @Test
    fun `Action_ChangeUserName process 영어 - name 업데이트, validName = true`() = runTest {
        with(signUpViewModel()) {
            // given
            val fakeUserName = "chaeunwoo"
            process(SignUpUiAction.ChangeUserName(fakeUserName))
            // when & then
            uiState.test {
                with(awaitItem()) {
                    name shouldBe fakeUserName
                    isValidName shouldBe true
                }
            }
        }
    }

    @Test
    fun `Action_ChangeUserName process 특수 문자 - name 업데이트,  validName = false`() = runTest {
        with(signUpViewModel()) {
            // given
            val fakeUserName = "차은우@@#"
            process(SignUpUiAction.ChangeUserName(fakeUserName))
            // when & then
            uiState.test {
                with(awaitItem()) {
                    name shouldBe fakeUserName
                    isValidName shouldBe false
                }
            }
        }
    }

    @Test
    fun `Action_ChangeUserId process`() = runTest {
        with(signUpViewModel()) {
            // given
            val userId = "cha_eunwoo123"
            // when
            process(SignUpUiAction.ChangeUserId(userId))
            // then
            // Add assertions here
        }
    }

    @Test
    fun `Action_ChangeUserPw process`() = runTest {
        with(signUpViewModel()) {
            // given
            val userPw = "password123"
            // when
            process(SignUpUiAction.ChangeUserPw(userPw))
            // then
            // Add assertions here
        }
    }

    @Test
    fun `Action_ChangeUserPwCheck process`() = runTest {
        with(signUpViewModel()) {
            // given
            val userPw = "password123"
            // when
            process(SignUpUiAction.ChangeUserPwCheck(userPw))
            // then
            // Add assertions here
        }
    }

    @Test
    fun `Action_ChangeEmail process`() = runTest {
        with(signUpViewModel()) {
            // given
            val email = "test@example.com"
            // when
            process(SignUpUiAction.ChangeEmail(email))
            // then
            // Add assertions here
        }
    }

    @Test
    fun `Action_ChangeEmailAuthNum process`() = runTest {
        with(signUpViewModel()) {
            // given
            val authNum = "123456"
            // when
            process(SignUpUiAction.ChangeEmailAuthNum(authNum))
            // then
            // Add assertions here
        }
    }

    @Test
    fun `Action_ClickPreviousPage process`() = runTest {
        with(signUpViewModel()) {
            // when
            process(SignUpUiAction.ClickPreviousPage)
            // then
            // Add assertions here
        }
    }

    @Test
    fun `Action_ClickNextPage process`() = runTest {
        with(signUpViewModel()) {
            // when
            process(SignUpUiAction.ClickNextPage)
            // then
            // Add assertions here
        }
    }

    @Test
    fun `Action_ClickEmailAuthNumSend process`() = runTest {
        with(signUpViewModel()) {
            // when
            process(SignUpUiAction.ClickEmailAuthNumSend)
            // then
            // Add assertions here
        }
    }

    @Test
    fun `Action_ClickIdDuplicateCheck process`() = runTest {
        with(signUpViewModel()) {
            // when
            process(SignUpUiAction.ClickIdDuplicateCheck)
            // then
            // Add assertions here
        }
    }

    @Test
    fun `Action_ClickSignUp process`() = runTest {
        with(signUpViewModel()) {
            // when
            process(SignUpUiAction.ClickSignUp)
            // then
            // Add assertions here
        }
    }
}
