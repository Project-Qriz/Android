package findId

import app.cash.turbine.test
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.findId.FindIdUiAction
import com.qriz.app.feature.sign.findId.FindIdUiState
import com.qriz.app.feature.sign.findId.FindIdViewModel
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import java.net.UnknownHostException

class FindIdViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val mockUserRepository = mockk<UserRepository>()

    private fun TestScope.findIdViewModel() = FindIdViewModel(
        userRepository = mockUserRepository
    )

    @Test
    fun `Action_OnChangeEmail - 이메일 텍스트 입력 시 상태 적용`() = runTest {
        with(findIdViewModel()) {
            //given
            val input = "test@gmail.com"

            //when
            process(FindIdUiAction.OnChangeEmail(email = input))

            //then
            uiState.test {
                awaitItem().email shouldBe input
            }
        }
    }

    @Test
    fun `Action_SendEmailToFind - 이메일이 유효한 형식으로 입력되지 않았을 때 error message`() = runTest {
        with(findIdViewModel()) {
            //given
            val input = "test@gmail"

            //when
            process(FindIdUiAction.OnChangeEmail(email = input))
            process(FindIdUiAction.SendEmailToFindId)

            //then
            uiState.test {
                with(awaitItem()) {
                    errorMessageResId shouldBe R.string.email_is_invalid_format
                    isValidEmailFormat shouldBe false
                }
            }
        }
    }

    @Test
    fun `Action_SendEmailToFind Success - 이메일 전송 성공 시 다이얼로그 띄움`() = runTest {
        with(findIdViewModel()) {
            //given
            val input = "test@gmail.com"
            coEvery { mockUserRepository.sendEmailToFindId(input) } returns ApiResult.Success(Unit)
            val expectedUiState = FindIdUiState.DEFAULT.copy(
                email = input,
                errorMessageResId = R.string.empty,
                isVisibleSuccessDialog = true,
            )

            //when
            process(FindIdUiAction.OnChangeEmail(email = input))
            process(FindIdUiAction.SendEmailToFindId)

            //then
            uiState.test {
                awaitItem() shouldBe expectedUiState
            }
        }
    }

    @Test
    fun `Action_SendEmailToFind Failure - 오류 메시지 상태 업데이트`() = runTest {
        with(findIdViewModel()) {
            //given
            val input = "test@gmail.com"
            coEvery { mockUserRepository.sendEmailToFindId(input) } returns ApiResult.Failure(code = -1, message = "가입되지 않은 이메일입니다")
            val expectedUiState = FindIdUiState.DEFAULT
                .copy(email = input, errorMessageResId = R.string.email_is_not_exist)

            //when
            process(FindIdUiAction.OnChangeEmail(email = input))
            process(FindIdUiAction.SendEmailToFindId)

            //then
            uiState.test {
                awaitItem() shouldBe expectedUiState
            }
        }
    }

    @Test
    fun `Action_SendEmailToFind NetworkError - 네트워크 에러 다이얼로그 띄움`() = runTest {
        with(findIdViewModel()) {
            //given
            val input = "test@gmail.com"
            coEvery { mockUserRepository.sendEmailToFindId(input) } returns ApiResult.NetworkError(IOException())
            val expectedUiState = FindIdUiState.DEFAULT.copy(
                email = input,
                errorMessageResId = R.string.empty,
                isVisibleNetworkErrorDialog = true,
            )

            //when
            process(FindIdUiAction.OnChangeEmail(email = input))
            process(FindIdUiAction.SendEmailToFindId)

            //then
            uiState.test {
                awaitItem() shouldBe expectedUiState
            }
        }
    }

    @Test
    fun `Action_SendEmailToFind Unknown - 오류발생 시 다이얼로그 띄움`() = runTest {
        with(findIdViewModel()) {
            //given
            val input = "test@gmail.com"
            val errorMessage = "올바르지 않은 접근입니다."
            val unknownErrorResult = ApiResult.UnknownError(throwable = Exception("알 수 없는 오류가 발생했습니다."))
            coEvery { mockUserRepository.sendEmailToFindId(input) } returns unknownErrorResult
            val expectedUiState = FindIdUiState.DEFAULT.copy(
                email = input,
                errorMessageResId = R.string.empty,
                errorDialogMessage = unknownErrorResult.throwable?.message ?: UNKNOWN_ERROR,
            )

            //when
            process(FindIdUiAction.OnChangeEmail(email = input))
            process(FindIdUiAction.SendEmailToFindId)

            //then
            uiState.test {
                awaitItem() shouldBe expectedUiState
            }
        }
    }
}
