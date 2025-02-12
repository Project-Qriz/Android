package findPassword

import app.cash.turbine.test
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.findPassword.auth.FindPasswordAuthUiAction
import com.qriz.app.feature.sign.findPassword.auth.FindPasswordAuthViewModel
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class FindPasswordAuthViewModelTest {
    @get:Rule
    val testDispatcher = MainDispatcherRule(testDispatcher = StandardTestDispatcher())

    private val mockUserRepository = mockk<UserRepository>()
    private val viewModel = FindPasswordAuthViewModel(mockUserRepository)

    @Test
    fun `Action_OnChangeEmail 이메일 입력 시 상태 업데이트`() = runTest {
        //given
        val testEmail = "test123@gmail.com"

        //when
        viewModel.process(FindPasswordAuthUiAction.OnChangeEmail(email = testEmail))

        //then
        viewModel.uiState.test {
            with(awaitItem()) {
                email shouldBe testEmail
            }
        }
    }

    @Test
    fun `Action_SendAuthNumberEmail 이메일 전송 성공 시 상태 업데이트`() = runTest {
        //given
        val email = "test123@gmail.com"
        coEvery { mockUserRepository.sendEmailToFindPassword(email) } returns Unit

        //when
        viewModel.process(FindPasswordAuthUiAction.OnChangeEmail(email = email))
        viewModel.process(FindPasswordAuthUiAction.SendAuthNumberEmail)

        //then
        viewModel.uiState.test {
            with(awaitItem()) {
                emailSupportingTextResId shouldBe R.string.empty
                authNumberSupportingTextResId shouldBe R.string.success_send_email_auth_number
                enableInputAuthNumber shouldBe true
                showAuthNumberLayout shouldBe true
                verifiedAuthNumber shouldBe false
            }
        }
    }

    @Test
    fun `Action_SendAuthNumberEmail 이메일 전송 실패 시 상태 업데이트`() = runTest {
        //given
        val email = "test123@gmail.com"
        coEvery { mockUserRepository.sendEmailToFindPassword(email) } throws Exception()

        //when
        viewModel.process(FindPasswordAuthUiAction.OnChangeEmail(email = email))
        viewModel.process(FindPasswordAuthUiAction.SendAuthNumberEmail)

        //then
        viewModel.uiState.test {
            with(awaitItem()) {
                showFailSendEmailDialog shouldBe true
            }
        }
    }

    @Test
    fun `Action_VerifyAuthNumber 인증번호 검증 성공 시 상태 업데이트`() = runTest {
        //given
        val authNumber = "123456"
        coEvery { mockUserRepository.verifyPasswordAuthNumber(authNumber) } returns Unit

        //when
        viewModel.process(FindPasswordAuthUiAction.OnChangeAuthNumber(authNumber = authNumber))
        viewModel.process(FindPasswordAuthUiAction.VerifyAuthNumber)

        //then
        viewModel.uiState.test {
            with(awaitItem()) {
                authNumberSupportingTextResId shouldBe R.string.success_verify_auth_number
                verifiedAuthNumber shouldBe true
            }
        }
    }

    @Test
    fun `Action_VerifyAuthNumber 인증번호 검증 실패 시 에러메시지`() = runTest {
        //given
        val authNumber = "123456"
        coEvery { mockUserRepository.verifyPasswordAuthNumber(authNumber) } throws Exception()

        //when
        viewModel.process(FindPasswordAuthUiAction.OnChangeAuthNumber(authNumber = authNumber))
        viewModel.process(FindPasswordAuthUiAction.VerifyAuthNumber)

        //then
        viewModel.uiState.test {
            with(awaitItem()) {
                authNumberSupportingTextResId shouldBe R.string.fail_verify_auth_number
            }
        }
    }

    @Test
    fun `Action_SendAuthNumberEmail 이메일 형식이 유효하지 않을 때 에러메시지`() = runTest {
        //given
        val email = "invalid-email-format"
        coEvery { mockUserRepository.sendEmailToFindPassword(email) } returns Unit

        //when
        viewModel.process(FindPasswordAuthUiAction.OnChangeEmail(email = email))
        viewModel.process(FindPasswordAuthUiAction.SendAuthNumberEmail)

        //then
        viewModel.uiState.test {
            with(awaitItem()) {
                emailSupportingTextResId shouldBe R.string.email_is_invalid_format
            }
        }
    }

    @Test
    fun `Action_VerifyAuthNumber 인증번호 형식이 유효하지 않을 때 에러메시지`() = runTest {
        //given
        val authNumber = "123"
        coEvery { mockUserRepository.verifyPasswordAuthNumber(authNumber) } returns Unit

        //when
        viewModel.process(FindPasswordAuthUiAction.OnChangeAuthNumber(authNumber = authNumber))
        viewModel.process(FindPasswordAuthUiAction.VerifyAuthNumber)

        //then
        viewModel.uiState.test {
            with(awaitItem()) {
                authNumberSupportingTextResId shouldBe R.string.invalid_auth_number_format
            }
        }
    }
}
