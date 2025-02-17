package findPassword

import com.qriz.app.feature.sign.R
import app.cash.turbine.test
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.feature.sign.findPassword.reset.ResetPasswordUiAction
import com.qriz.app.feature.sign.findPassword.reset.ResetPasswordUiEffect
import com.qriz.app.feature.sign.findPassword.reset.ResetPasswordViewModel
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class ResetPasswordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockUserRepository: UserRepository = mockk()
    private val viewModel: ResetPasswordViewModel = ResetPasswordViewModel(mockUserRepository)

    @Test
    fun `Action_OnChangePassword 올바른 비밀번호 입력 시 email 업데이트, 유효성 검사 성공`() = runTest {
        // given
        val password = "newPassword123!"

        // when
        viewModel.process(ResetPasswordUiAction.OnChangePassword(password))

        // then
        viewModel.uiState.test {
            with(awaitItem()) {
                password shouldBe password
                isValidPasswordFormat shouldBe true
                isValidPasswordLength shouldBe true
            }
        }
    }

    @Test
    fun `Action_OnChangePasswordConfirm 올바르지 않은 비밀번호 입력 시 email 업데이트, 유효성 검사 실패`() = runTest {
        // given
        val passwordConfirm = "newPw"

        // when
        viewModel.process(ResetPasswordUiAction.OnChangePasswordConfirm(passwordConfirm))

        // then
        viewModel.uiState.test {
            with(awaitItem()) {
                passwordConfirm shouldBe passwordConfirm
                isValidPasswordFormat shouldBe false
                isValidPasswordLength shouldBe false
            }
        }
    }


    @Test
    fun `Action_OnChangePasswordConfirm 비밀번호 변경 가능한 조건 입력 시 상태 업데이트`() = runTest {
        // given
        val password = "newPassword123!"
        val passwordConfirm = "newPassword123!"

        // when
        viewModel.process(ResetPasswordUiAction.OnChangePassword(password))
        viewModel.process(ResetPasswordUiAction.OnChangePasswordConfirm(passwordConfirm))

        // then
        viewModel.uiState.test {
            with(awaitItem()) {
                isEqualsPassword shouldBe true
                canResetPassword shouldBe true
            }
        }
    }

    @Test
    fun `Action_OnChangePasswordConfirm 입력된 비밀번호와 다를 시 errorMessageResId`() = runTest {
        // given
        val password = "newPassword123!"
        val passwordConfirm = "newPassword123"

        // when
        viewModel.process(ResetPasswordUiAction.OnChangePassword(password))
        viewModel.process(ResetPasswordUiAction.OnChangePasswordConfirm(passwordConfirm))

        // then
        viewModel.uiState.test {
            awaitItem().passwordConfirmErrorMessageResId shouldBe R.string.passwords_do_not_match
        }
    }

    @Test
    fun `Action_ResetPassword 비밀번호 변경 성공 시 ResetComplete 발생`() = runTest {
        // given
        val password = "newPassword123!"
        coEvery { mockUserRepository.resetPassword(password) } returns Unit
        viewModel.process(ResetPasswordUiAction.OnChangePassword(password))
        viewModel.process(ResetPasswordUiAction.OnChangePasswordConfirm(password))

        // when
        viewModel.process(ResetPasswordUiAction.ResetPassword)

        // then
        viewModel.effect.test {
            awaitItem() shouldBe ResetPasswordUiEffect.ResetComplete
        }
    }

    //TODO: 비밀번호 변경 실패 시 다이얼로그 노출 기능 추가 후 테스트 코드 작성
//    @Test
//    fun `Action_ResetPassword 비밀번호 변경 성공 시 ResetComplete 발생`() = runTest {
//        // given
//        val password = "newPassword123!"
//        coEvery { mockUserRepository.resetPassword(password) } returns Unit
//        viewModel.process(ResetPasswordUiAction.OnChangePassword(password))
//        viewModel.process(ResetPasswordUiAction.OnChangePasswordConfirm(password))
//
//        // when
//        viewModel.process(ResetPasswordUiAction.ResetPassword)
//
//        // then
//        viewModel.effect.test {
//            awaitItem() shouldBe ResetPasswordUiEffect.ResetComplete
//        }
//    }
}
