package findId

import app.cash.turbine.test
import com.qriz.app.core.testing.MainDispatcherRule
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
    fun `Action_SendEmailToFind - 이메일 전송 성공 시 다이얼로그 띄움`() = runTest {
        with(findIdViewModel()) {
            //given
            val input = "test@gmail.com"
            coEvery { mockUserRepository.sendEmailToFindId(input) } returns Unit
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
    fun `Action_SendEmailToFind - 네트워크 오류로 인한 실패 시, 다이얼로그 띄움`() = runTest {
        with(findIdViewModel()) {
            //given
            val input = "test@gmail.com"
            val errorMessage = "Http 404"
            coEvery { mockUserRepository.sendEmailToFindId(input) } throws UnknownHostException(
                errorMessage
            )
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
    fun `Action_SendEmailToFind - 이메일 전송 실패 시 다이얼로그 띄움`() = runTest {
        with(findIdViewModel()) {
            //given
            val input = "test@gmail.com"
            val errorMessage = "올바르지 않은 접근입니다."
            coEvery { mockUserRepository.sendEmailToFindId(input) } throws Exception(errorMessage)
            val expectedUiState = FindIdUiState.DEFAULT.copy(
                email = input,
                errorMessageResId = R.string.empty,
                errorDialogMessage = errorMessage,
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
