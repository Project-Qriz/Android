package signup

import app.cash.turbine.test
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.SignUpUiAction
import com.qriz.app.feature.sign.signup.SignUpUiEffect
import com.qriz.app.feature.sign.signup.SignUpUiState.AuthenticationState
import com.qriz.app.feature.sign.signup.SignUpUiState.SignUpPage.EMAIL_AUTH
import com.qriz.app.feature.sign.signup.SignUpViewModel
import com.qriz.app.feature.sign.signup.SignUpViewModel.Companion.AUTHENTICATION_LIMIT_MILS
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
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
    fun `Action_ChangeUserName process - name 업데이트, 특수문자 제외 한글,영문이름 가능`() = runTest {
        with(signUpViewModel()) {
            // given
            val testDataList = listOf(
                VerificationTestData(
                    contents = "차은우",
                    expectedValidResult = true
                ),
                VerificationTestData(
                    contents = "chaeunwoo",
                    expectedValidResult = true
                ),
                VerificationTestData(
                    contents = "차은우@@#",
                    expectedValidResult = false
                ),
            )
            testDataList.forEach { testData ->
                // when
                process(SignUpUiAction.ChangeUserName(testData.contents))
                // then
                uiState.test {
                    with(awaitItem()) {
                        name shouldBe testData.contents
                        isValidName shouldBe testData.expectedValidResult
                    }
                }
            }
        }
    }

    @Test
    fun `Action_ChangeEmail process - email 업데이트, 이메일 정규식 통과 시에만 가능, 부합하는 에러메세지 업데이트`() = runTest {
        with(signUpViewModel()) {
            // given
            val testDataList = listOf(
                VerificationTestData(
                    contents = "test",
                    expectedValidResult = false,
                    expectedErrorResId = R.string.please_check_email_again
                ),
                VerificationTestData(
                    contents = "",
                    expectedValidResult = false,
                    expectedErrorResId = R.string.empty
                ),
                VerificationTestData(
                    contents = "test@example.com",
                    expectedValidResult = true,
                    expectedErrorResId = R.string.empty
                ),
                VerificationTestData(
                    contents = "test1234@example.com",
                    expectedValidResult = true,
                    expectedErrorResId = R.string.empty
                ),
            )
            testDataList.forEach { testData ->
                // when
                process(SignUpUiAction.ChangeEmail(testData.contents))
                // then
                uiState.test {
                    with(awaitItem()) {
                        email shouldBe testData.contents
                        isValidEmail shouldBe testData.expectedValidResult
                        emailErrorMessageResId shouldBe testData.expectedErrorResId
                    }
                }
            }
        }
    }

    @Test
    fun `Action_ChangeEmailAuthNum process 입력된 값이 6자리가 되지 않음 - 검증 API 호출하지 않음`() = runTest {
        with(signUpViewModel()) {
            // given
            val authNum = "12"
            // when
            process(SignUpUiAction.ChangeEmailAuthNum(authNum))
            // then
            coVerify(exactly = 0) { fakeUserRepository.verifyEmailAuthNumber(authNum) }
        }
    }

    @Test
    fun `Action_ChangeEmailAuthNum process 입력된 값이 6자리 됨 - 자동으로 검증 API 호출`() = runTest {
        with(signUpViewModel()) {
            // given
            val authNum = "123456"
            // when
            process(SignUpUiAction.ChangeEmailAuthNum(authNum))
            // then
            coVerify { fakeUserRepository.verifyEmailAuthNumber(authNum) }
        }
    }

    @Test
    fun `Action_ChangeEmailAuthNum process 검증 API 성공 (번호 유효함) - 타이머 종료, VERIFIED 상태 업데이트`() =
        runTest {
            with(signUpViewModel()) {
                // given
                val authNum = "123456"
                coEvery { fakeUserRepository.verifyEmailAuthNumber(authNum) } returns true
                // when
                process(SignUpUiAction.ChangeEmailAuthNum(authNum))
                // then
                uiState.test {
                    awaitItem().emailAuthState shouldBe AuthenticationState.VERIFIED
                    timerJob.shouldBeNull()
                }
            }
        }

    @Test
    fun `Action_ChangeEmailAuthNum process 검증 API 성공 (번호 유효 X) - REJECTED 상태, 에러 메세지 업데이트`() =
        runTest {
            with(signUpViewModel()) {
                // given
                val authNum = "123456"
                coEvery { fakeUserRepository.verifyEmailAuthNumber(authNum) } returns false
                // when
                process(SignUpUiAction.ChangeEmailAuthNum(authNum))
                // then
                uiState.test {
                    with(awaitItem()) {
                        emailAuthState shouldBe AuthenticationState.REJECTED
                        emailAuthNumberErrorMessageResId shouldBe R.string.email_auth_num_is_different
                    }
                }
            }
        }

//    @Test
//    fun `Action_ChangeEmailAuthNum process 검증 API 실패 - 다이얼로그 노출`() = runTest {
//        with(signUpViewModel()) {
//            // given
//            val authNum = "123456"
//            coEvery { fakeUserRepository.verifyEmailAuthNumber(authNum) } throws Exception()
//            // when
//            process(SignUpUiAction.ChangeEmailAuthNum(authNum))
//            // then
//
//        }
//    }

    @Test
    fun `Action_ChangeEmailAuthNum process 이미 검증 완료 - emailAuthNumber 업데이트 X, 검증 API 호출 x`() =
        runTest {
            val signUpViewModel = object : SignUpViewModel(
                userRepository = fakeUserRepository
            ) {
                init {
                    updateState { copy(emailAuthState = AuthenticationState.VERIFIED) }
                }
            }
            with(signUpViewModel) {
                // given
                val fakeEmailAuthNum = "123456"
                // when
                process(SignUpUiAction.ChangeEmailAuthNum(fakeEmailAuthNum))
                // then
                uiState.test { awaitItem().emailAuthNumber shouldNotBe fakeEmailAuthNum }
                coVerify(exactly = 0) { fakeUserRepository.verifyEmailAuthNumber(any()) }
            }
        }

    @Test
    fun `Action_ChangeEmailAuthNum process 시간 만료 상태 - emailAuthNumber 업데이트, 검증 API 호출 x`() =
        runTest {
            val signUpViewModel = object : SignUpViewModel(
                userRepository = fakeUserRepository
            ) {
                init {
                    updateState { copy(emailAuthState = AuthenticationState.TIME_EXPIRED) }
                }
            }
            with(signUpViewModel) {
                // given
                val fakeEmailAuthNum = "123456"
                // when
                process(SignUpUiAction.ChangeEmailAuthNum(fakeEmailAuthNum))
                // then
                uiState.test { awaitItem().emailAuthNumber shouldBe fakeEmailAuthNum }
                coVerify(exactly = 0) { fakeUserRepository.verifyEmailAuthNumber(any()) }
            }
        }

    @Test
    fun `Action_ChangeEmailAuthNum process 이메일 전송 실패 상태 - emailAuthNumber 업데이트, 검증 API 호출 x`() =
        runTest {
            val signUpViewModel = object : SignUpViewModel(
                userRepository = fakeUserRepository
            ) {
                init {
                    updateState { copy(emailAuthState = AuthenticationState.SEND_FAILED) }
                }
            }
            with(signUpViewModel) {
                // given
                val fakeEmailAuthNum = "123456"
                // when
                process(SignUpUiAction.ChangeEmailAuthNum(fakeEmailAuthNum))
                // then
                uiState.test { awaitItem().emailAuthNumber shouldBe fakeEmailAuthNum }
                coVerify(exactly = 0) { fakeUserRepository.verifyEmailAuthNumber(any()) }
            }
        }

    @Test
    fun `Action_ChangeEmailAuthNum process 인증번호 반려 상태 - emailAuthNumber 업데이트, AuthenticationState 상태 업데이트, 검증 API 호출 O`() =
        runTest {
            val signUpViewModel = object : SignUpViewModel(
                userRepository = fakeUserRepository
            ) {
                init {
                    updateState { copy(emailAuthState = AuthenticationState.REJECTED) }
                }
            }
            with(signUpViewModel) {
                // given
                val fakeEmailAuthNum = "123456"
                // when
                process(SignUpUiAction.ChangeEmailAuthNum(fakeEmailAuthNum))
                // then
                uiState.test {
                    with(awaitItem()) {
                        emailAuthNumber shouldBe fakeEmailAuthNum
                        emailAuthState shouldBe AuthenticationState.NONE
                        emailAuthNumberErrorMessageResId shouldBe R.string.empty
                    }
                }
                coVerify { fakeUserRepository.verifyEmailAuthNumber(any()) }
            }
        }

    @Test
    fun `Action_ChangeEmailAuthNum process 무인증 상태 - emailAuthNumber 업데이트, AuthenticationState 상태 업데이트, 검증 API 호출 O`() =
        runTest {
            with(signUpViewModel()) {
                // given
                val fakeEmailAuthNum = "123456"
                // when
                process(SignUpUiAction.ChangeEmailAuthNum(fakeEmailAuthNum))
                // then
                uiState.test {
                    with(awaitItem()) {
                        emailAuthNumber shouldBe fakeEmailAuthNum
                        emailAuthState shouldBe AuthenticationState.NONE
                        emailAuthNumberErrorMessageResId shouldBe R.string.empty
                    }
                }
                coVerify { fakeUserRepository.verifyEmailAuthNumber(fakeEmailAuthNum) }
            }
        }

    @Test
    fun `Action_ChangeUserId process 정규식 통과 - 에러메세지 empty`() = runTest {
        with(signUpViewModel()) {
            // given
            val userId = "chaeunwoo123"
            // when
            process(SignUpUiAction.ChangeUserId(userId))
            // then
            uiState.test {
                with(awaitItem()) {
                    id shouldBe userId
                    isNotDuplicatedId shouldBe false
                    idErrorMessageResId shouldBe R.string.empty
                }
            }
        }
    }

    @Test
    fun `Action_ChangeUserId process 정규식 통과 실패 - 에러메세지 O`() = runTest {
        with(signUpViewModel()) {
            // given
            val userId = "cha_eunwoo123@"
            // when
            process(SignUpUiAction.ChangeUserId(userId))
            // then
            uiState.test {
                with(awaitItem()) {
                    id shouldBe userId
                    isNotDuplicatedId shouldBe false
                    idErrorMessageResId shouldBe R.string.id_cannot_be_used
                }
            }
        }
    }

    @Test
    fun `Action_ChangeUserPw process 정규식 통과, 체크 pw 동일 - 에러메세지 둘 다 empty`() = runTest {
        with(signUpViewModel()) {
            // given
            val userPw = "!Password123"
            val userPwCheck = userPw
            // when
            process(SignUpUiAction.ChangeUserPw(userPw))
            process(SignUpUiAction.ChangeUserPwCheck(userPwCheck))
            // then
            uiState.test {
                with(awaitItem()) {
                    pw shouldBe userPw
                    pwCheck shouldBe userPwCheck
                    pwErrorMessageResId shouldBe R.string.empty
                    pwCheckErrorMessageResId shouldBe R.string.empty
                }
            }
        }
    }

    @Test
    fun `Action_ChangeUserPw process 정규식 통과, 체크 pw 비동일 - pw 에러메세지 empty, pwCheck 에러메세지 O`() =
        runTest {
            with(signUpViewModel()) {
                // given
                val userPw = "!Password123"
                val userPwCheck = userPw + "az"
                // when
                process(SignUpUiAction.ChangeUserPw(userPw))
                process(SignUpUiAction.ChangeUserPwCheck(userPwCheck))
                // then
                uiState.test {
                    with(awaitItem()) {
                        pw shouldBe userPw
                        pwCheck shouldBe userPwCheck
                        pwErrorMessageResId shouldBe R.string.empty
                        pwCheckErrorMessageResId shouldBe R.string.password_is_incorrect
                    }
                }
            }
        }

    @Test
    fun `Action_ChangeUserPw process 정규식 통과 X, 체크 pw 동일 - pw 에러메세지 O, pwCheck 에러메세지 empty`() =
        runTest {
            with(signUpViewModel()) {
                // given
                val userPw = "password123"
                val userPwCheck = userPw
                // when
                process(SignUpUiAction.ChangeUserPw(userPw))
                process(SignUpUiAction.ChangeUserPwCheck(userPwCheck))
                // then
                uiState.test {
                    with(awaitItem()) {
                        pw shouldBe userPw
                        pwCheck shouldBe userPwCheck
                        pwErrorMessageResId shouldBe R.string.pw_warning
                        pwCheckErrorMessageResId shouldBe R.string.empty
                    }
                }
            }
        }

    @Test
    fun `Action_ChangeUserPw process 정규식 통과 X, 체크 pw 비동일 - pw 에러메세지 O, pwCheck 에러메세지 O`() =
        runTest {
            with(signUpViewModel()) {
                // given
                val userPw = "password123"
                val userPwCheck = userPw + "az"
                // when
                process(SignUpUiAction.ChangeUserPw(userPw))
                process(SignUpUiAction.ChangeUserPwCheck(userPwCheck))
                // then
                uiState.test {
                    with(awaitItem()) {
                        pw shouldBe userPw
                        pwCheck shouldBe userPwCheck
                        pwErrorMessageResId shouldBe R.string.pw_warning
                        pwCheckErrorMessageResId shouldBe R.string.password_is_incorrect
                    }
                }
            }
        }

    @Test
    fun `Action_ClickPreviousPage process 첫 페이지 - 상태변화 X, Effect_MoveToBack 발생`() = runTest {
        with(signUpViewModel()) {
            // given
            val previousUiState = uiState.value
            // when
            process(SignUpUiAction.ClickPreviousPage)
            // then
            uiState.test { awaitItem() shouldBe previousUiState }
            effect.test { awaitItem() shouldBe SignUpUiEffect.MoveToBack }
        }
    }

    @Test
    fun `Action_ClickPreviousPage process 첫 페이지 X - uiState page 감소`() = runTest {
        with(signUpViewModel()) {
            // given
            process(SignUpUiAction.ClickNextPage)
            process(SignUpUiAction.ClickNextPage)
            val previousUiState = uiState.value
            // when
            process(SignUpUiAction.ClickPreviousPage)
            // then
            uiState.test {
                awaitItem().page shouldBe previousUiState.page.previous
            }
        }
    }

    @Test
    fun `Action_ClickNextPage process - uiState page 증가`() = runTest {
        with(signUpViewModel()) {
            // given
            val previousUiState = uiState.value
            // when
            process(SignUpUiAction.ClickNextPage)
            // then
            uiState.test { awaitItem().page shouldBe previousUiState.page.next }
        }
    }

    @Test
    fun `Action_ClickNextPage process 현재 화면 EMAIL_AUTH- uiState page 증가, 타이머 종료`() = runTest {
        with(signUpViewModel()) {
            // given
            (1..EMAIL_AUTH.index).forEach { process(SignUpUiAction.ClickNextPage) }
            val previousUiState = uiState.value
            // when
            process(SignUpUiAction.ClickNextPage)
            // then
            uiState.test { awaitItem().page shouldBe previousUiState.page.next }
            timerJob.shouldBeNull()
        }
    }

    @Test
    fun `Action_ClickEmailAuthNumSend process `() = runTest {
        with(signUpViewModel()) {
            // given
            val fakeEmail = "test1234@example.com"
            process(SignUpUiAction.ChangeEmail(fakeEmail))
            // when
            process(SignUpUiAction.ClickEmailAuthNumSend)
            // then
            coVerify { fakeUserRepository.requestEmailAuthNumber(fakeEmail) }
        }
    }

    @Test
    fun `Action_RequestEmailAuthNumber process 성공 - Effect_ShowSnackBer 발생, 타이머 시작`() = runTest {
        with(signUpViewModel()) {
            // given
            val fakeEmail = "test1234@example.com"
            coEvery { fakeUserRepository.requestEmailAuthNumber(fakeEmail) } returns Unit
            process(SignUpUiAction.ChangeEmail(fakeEmail))
            // when
            process(SignUpUiAction.RequestEmailAuthNumber)
            // then
            effect.test { (awaitItem() is SignUpUiEffect.ShowSnackBer) shouldBe true }
            timerJob.shouldNotBeNull()
        }
    }

    @Test
    fun `Action_RequestEmailAuthNumber process 실패 - SEND_FAILED 상태 업데이트, 에러메세지 추가`() = runTest {
        with(signUpViewModel()) {
            // given
            val fakeEmail = "test1234@example.com"
            coEvery { fakeUserRepository.requestEmailAuthNumber(fakeEmail) } throws Exception()
            process(SignUpUiAction.ChangeEmail(fakeEmail))
            // when
            process(SignUpUiAction.RequestEmailAuthNumber)
            // then
            uiState.test {
                with(awaitItem()) {
                    emailAuthState shouldBe AuthenticationState.SEND_FAILED
                    emailAuthNumberErrorMessageResId shouldBe R.string.email_auth_sent_fail
                }
            }
        }
    }

    @Test
    fun `Action_RequestEmailAuthNumber process 이미 검증된 경우 - API 호출 X `() = runTest {
        val signUpViewModel = object : SignUpViewModel(
            userRepository = fakeUserRepository
        ) {
            init {
                updateState { copy(emailAuthState = AuthenticationState.VERIFIED) }
            }
        }
        with(signUpViewModel) {
            // given
            val fakeEmail = "test@example.com"
            process(SignUpUiAction.ChangeEmail(fakeEmail))
            // when
            process(SignUpUiAction.RequestEmailAuthNumber)
            // then
            coVerify(exactly = 0) { fakeUserRepository.requestEmailAuthNumber(any()) }
        }
    }

    @Test
    fun `Action_ClickIdDuplicateCheck process 입력된 Id가 없음 - 에러메세지 업데이트`() = runTest {
        with(signUpViewModel()) {
            // given
            val id = ""
            process(SignUpUiAction.ChangeUserId(id))
            // when
            process(SignUpUiAction.ClickIdDuplicateCheck)
            // then
            uiState.test {
                awaitItem().idErrorMessageResId shouldBe R.string.please_enter_id
            }
        }
    }

    @Test
    fun `Action_ClickIdDuplicateCheck process 정규식 통과 X - API 호출하지 않음`() = runTest {
        with(signUpViewModel()) {
            // given
            val id = "test@1234!#"
            process(SignUpUiAction.ChangeUserId(id))
            // when
            process(SignUpUiAction.ClickIdDuplicateCheck)
            // then
            coVerify(exactly = 0) { fakeUserRepository.isNotDuplicateId(id) }
        }
    }

    @Test
    fun `Action_ClickIdDuplicateCheck process 정규식 통과 O, API 성공, 사용가능 ID - 상태 업데이트`() = runTest {
        with(signUpViewModel()) {
            // given
            val id = "test1234"
            coEvery { fakeUserRepository.isNotDuplicateId(id) } returns true
            process(SignUpUiAction.ChangeUserId(id))
            // when
            process(SignUpUiAction.ClickIdDuplicateCheck)
            // then
            uiState.test {
                with(awaitItem()) {
                    isNotDuplicatedId shouldBe true
                    idErrorMessageResId shouldBe R.string.empty
                }
            }
        }
    }

    @Test
    fun `Action_ClickIdDuplicateCheck process 정규식 통과 O, API 성공, 중복 ID - 상태 업데이트`() = runTest {
        with(signUpViewModel()) {
            // given
            val id = "test1234"
            coEvery { fakeUserRepository.isNotDuplicateId(id) } returns false
            process(SignUpUiAction.ChangeUserId(id))
            // when
            process(SignUpUiAction.ClickIdDuplicateCheck)
            // then
            uiState.test {
                with(awaitItem()) {
                    isNotDuplicatedId shouldBe false
                    idErrorMessageResId shouldBe R.string.id_cannot_be_used
                }
            }
        }
    }

//    @Test
//    fun `Action_ClickIdDuplicateCheck process 정규식 통과 O, API 실패 - `() = runTest {
//        with(signUpViewModel()) {
//            // given
//            val id = "test@1234!#"
//            coEvery { fakeUserRepository.isNotDuplicateId(id) } throws Exception()
//            process(SignUpUiAction.ChangeUserId(id))
//            // when
//            process(SignUpUiAction.ClickIdDuplicateCheck)
//            // then
//        }
//    }

//    @Test
//    fun `Action_ClickSignUp process API 성공`() = runTest {
//        with(signUpViewModel()) {
//            // given
//            coEvery { fakeUserRepository.signUp() }
//            // when
//            process(SignUpUiAction.ClickSignUp)
//            // then
//        }
//    }

//    @Test
//    fun `Action_ClickSignUp process API 실패`() = runTest {
//        with(signUpViewModel()) {
//            // given
//            coEvery { fakeUserRepository.signUp() } throws Exception()
//            // when
//            process(SignUpUiAction.ClickSignUp)
//            // then
//        }
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Action_StartEmailAuthTimer 시작 후 - emailAuthTime 1초씩 감소`() = runTest {
        with(signUpViewModel()) {
            // given
            process(SignUpUiAction.StartEmailAuthTimer)
            (1..5).forEach { seconds ->
                // when
                advanceTimeBy(1010)
                // then
                uiState.test {
                    with(awaitItem()) {
                        emailAuthTime shouldBe AUTHENTICATION_LIMIT_MILS - 1000 * seconds
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Action_StartEmailAuthTimer 타이머 만료 - 상태 업데이트`() = runTest {
        with(signUpViewModel()) {
            // given
            process(SignUpUiAction.StartEmailAuthTimer)
            // when
            advanceTimeBy(AUTHENTICATION_LIMIT_MILS + 100L)
            // then
            uiState.test {
                with(awaitItem()) {
                    emailAuthTime shouldBe 0
                    emailAuthState shouldBe AuthenticationState.TIME_EXPIRED
                    emailAuthNumberErrorMessageResId shouldBe R.string.email_auth_time_has_expired
                }
            }
        }
    }

    companion object {
        class VerificationTestData(
            val contents: String,
            val expectedValidResult: Boolean,
            val expectedErrorResId: Int? = null,
        )
    }
}
