package signup

import app.cash.turbine.test
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.app.core.ui.common.resource.CHECK_NETWORK_AND_TRY_AGAIN
import com.qriz.app.core.ui.common.resource.CONTACT_DEVELOPER_IF_PERSISTS
import com.qriz.app.core.ui.common.resource.NETWORK_IS_UNSTABLE
import com.qriz.app.core.ui.common.resource.UNKNOWN_ERROR
import com.qriz.app.feature.sign.R
import com.qriz.app.feature.sign.signup.SignUpUiAction
import com.qriz.app.feature.sign.signup.SignUpUiEffect
import com.qriz.app.feature.sign.signup.SignUpUiState
import com.qriz.app.feature.sign.signup.SignUpUiState.AuthenticationState
import com.qriz.app.feature.sign.signup.SignUpUiState.UserIdValidationState.AVAILABLE
import com.qriz.app.feature.sign.signup.SignUpUiState.UserIdValidationState.NONE
import com.qriz.app.feature.sign.signup.SignUpUiState.UserIdValidationState.NOT_AVAILABLE
import com.qriz.app.feature.sign.signup.SignUpViewModel
import com.qriz.app.feature.sign.signup.SignUpViewModel.Companion.AUTHENTICATION_LIMIT_MILS
import com.quiz.app.core.data.user.user_api.model.User
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class SignUpViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val fakeUserRepository = mockk<UserRepository>()

    private fun TestScope.signUpViewModel() = SignUpViewModel(
        userRepository = fakeUserRepository
    )

    /*
    * ******************************************
    * EMAIL AUTHENTICATION
    * ******************************************
    */

    @Test
    fun `Action_ChangeEmail process - email 업데이트, 이메일 정규식 통과 시에만 가능, 부합하는 에러메세지 업데이트`() = runTest {
        with(signUpViewModel()) {
            // given
            val testDataList = listOf(
                VerificationTestData(
                    contents = "test",
                    expectedValidResult = false,
                    expectedErrorResId = R.string.email_is_invalid_format
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
                        emailSupportingTextResId shouldBe testData.expectedErrorResId
                    }
                }
            }
        }
    }

    @Test
    fun `Action_ClickEmailAuthNumSend process - 올바른 이메일 입력 후 인증번호 요청 시 API 호출`() = runTest {
        with(signUpViewModel()) {
            // given
            val fakeEmail = "test1234@example.com"
            process(SignUpUiAction.ChangeEmail(fakeEmail))
            coEvery { fakeUserRepository.requestEmailAuthNumber(fakeEmail) } returns ApiResult.Success(Unit)
            // when
            process(SignUpUiAction.ClickEmailAuthNumSend)
            // then
            coVerify { fakeUserRepository.requestEmailAuthNumber(fakeEmail) }
        }
    }

    @Test
    fun `Action_ClickEmailAuthNumSend process 이메일 입력되지 않음 - 에러메세지 업데이트`() = runTest {
        with(signUpViewModel()) {
            // given
            val emptyEmail = ""
            process(SignUpUiAction.ChangeEmail(emptyEmail))
            // when
            process(SignUpUiAction.ClickEmailAuthNumSend)
            // then
            uiState.test {
                awaitItem().emailSupportingTextResId shouldBe R.string.email_is_empty
            }
        }
    }

    @Test
    fun `Action_ClickEmailAuthNumSend process 이메일 형식 맞지 않음 - 에러메세지 업데이트`() = runTest {
        with(signUpViewModel()) {
            // given
            val invalidEmail = "invalid-email"
            process(SignUpUiAction.ChangeEmail(invalidEmail))
            // when
            process(SignUpUiAction.ClickEmailAuthNumSend)
            // then
            uiState.test {
                awaitItem().emailSupportingTextResId shouldBe R.string.email_is_invalid_format
            }
        }
    }

    @Test
    fun `Action_ClickEmailAuthNumSend process 성공 - 상태 업데이트, Effect_ShowSnackBer 발생, 타이머 시작`() = runTest {
        with(signUpViewModel()) {
            // given
            val fakeEmail = "test1234@example.com"
            coEvery { fakeUserRepository.requestEmailAuthNumber(fakeEmail) } returns ApiResult.Success(Unit)
            process(SignUpUiAction.ChangeEmail(fakeEmail))
            // when
            process(SignUpUiAction.ClickEmailAuthNumSend)
            // then
            effect.test { (awaitItem() is SignUpUiEffect.ShowSnackBer) shouldBe true }
            uiState.test {
                awaitItem().emailAuthState shouldBe AuthenticationState.SEND_SUCCESS
            }
            isTimerJobNotNull.shouldBeTrue()
        }
    }

    @Test
    fun `Action_ClickEmailAuthNumSend Failure - SEND_FAILED 상태 업데이트, 에러메세지 추가`() = runTest {
        with(signUpViewModel()) {
            // given
            val fakeEmail = "test1234@example.com"
            coEvery { fakeUserRepository.requestEmailAuthNumber(fakeEmail) } returns ApiResult.Failure(code = -1, message = "이메일 전송에 실패하였습니다.")
            process(SignUpUiAction.ChangeEmail(fakeEmail))
            // when
            process(SignUpUiAction.ClickEmailAuthNumSend)
            // then
            uiState.test {
                with(awaitItem()) {
                    emailAuthState shouldBe AuthenticationState.SEND_FAILED
                    authNumberSupportingTextResId shouldBe R.string.email_auth_sent_fail
                }
            }
        }
    }

    @Test
    fun `Action_ClickEmailAuthNumSend UnknownError - SEND_FAILED 상태 업데이트, 에러메세지 추가`() = runTest {
        with(signUpViewModel()) {
            // given
            val fakeEmail = "test1234@example.com"
            coEvery { fakeUserRepository.requestEmailAuthNumber(fakeEmail) } returns ApiResult.UnknownError(throwable = null)
            process(SignUpUiAction.ChangeEmail(fakeEmail))
            // when
            process(SignUpUiAction.ClickEmailAuthNumSend)
            // then
            uiState.test {
                with(awaitItem()) {
                    emailAuthState shouldBe AuthenticationState.SEND_FAILED
                    authNumberSupportingTextResId shouldBe R.string.email_auth_sent_fail
                }
            }
        }
    }

    @Test
    fun `Action_ClickEmailAuthNumSend process 이미 검증된 경우 - API 호출 X `() = runTest {
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
            process(SignUpUiAction.ClickEmailAuthNumSend)
            // then
            coVerify(exactly = 0) { fakeUserRepository.requestEmailAuthNumber(any()) }
        }
    }

    @Test
    fun `Action_ChangeEmailAuthNum process 시간 만료 상태 - emailAuthNumber 업데이트 x`() =
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
                uiState.test { awaitItem().emailAuthNumber.shouldBeEmpty() }
            }
        }

    @Test
    fun `Action_ChangeEmailAuthNum 이미 검증 완료 상태 - emailAuthNumber 업데이트 x`() =
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
                uiState.test { awaitItem().emailAuthNumber.shouldBeEmpty() }
            }
        }

    @Test
    fun `Action_ChangeEmailAuthNum 이메일 전송 완료 상태 - emailAuthNumber 업데이트`() =
        runTest {
            val signUpViewModel = object : SignUpViewModel(
                userRepository = fakeUserRepository
            ) {
                init {
                    updateState { copy(emailAuthState = AuthenticationState.SEND_SUCCESS) }
                }
            }
            with(signUpViewModel) {
                // given
                val fakeEmailAuthNum = "123456"
                // when
                process(SignUpUiAction.ChangeEmailAuthNum(fakeEmailAuthNum))
                // then
                uiState.test { awaitItem().emailAuthNumber shouldBe fakeEmailAuthNum }
            }
        }

    @Test
    fun `Action_ChangeEmailAuthNum 이메일 전송 실패 상태 - emailAuthNumber 업데이트`() =
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
            }
        }

    @Test
    fun `Action_ChangeEmailAuthNum process 인증번호 반려 상태 - emailAuthNumber 업데이트, AuthenticationState 상태 업데이트`() =
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
                    }
                }
            }
        }

    @Test
    fun `Action_ChangeEmailAuthNum process 무인증 상태 - emailAuthNumber 업데이트, AuthenticationState 상태 업데이트`() =
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
                        authNumberSupportingTextResId shouldBe R.string.empty
                    }
                }
            }
        }

    @Test
    fun `Action_ClickVerifyAuthNum process 검증 API 성공 (번호 유효함) - 타이머 종료, VERIFIED 상태 업데이트`() =
        runTest {
            with(signUpViewModel()) {
                // given
                val fakeEmail = "test@example.com"
                val authNum = "123456"
                process(SignUpUiAction.ChangeEmail(fakeEmail))
                process(SignUpUiAction.ChangeEmailAuthNum(authNum))
                coEvery { fakeUserRepository.verifyEmailAuthNumber(fakeEmail, authNum) } returns ApiResult.Success(Unit)
                // when
                process(SignUpUiAction.ClickVerifyAuthNum)
                // then
                uiState.test {
                    with(awaitItem()) {
                        emailAuthState shouldBe AuthenticationState.VERIFIED
                        authNumberSupportingTextResId shouldBe R.string.success_verify_auth_number
                    }
                    isTimerJobNull.shouldBeTrue()
                }
            }
        }

    @Test
    fun `Action_ClickVerifyAuthNum process 검증 API 성공 (번호 유효 X) - REJECTED 상태, 에러 메세지 업데이트`() =
        runTest {
            with(signUpViewModel()) {
                // given
                val fakeEmail = "test@example.com"
                val authNum = "123456"
                process(SignUpUiAction.ChangeEmail(fakeEmail))
                process(SignUpUiAction.ChangeEmailAuthNum(authNum))
                coEvery { fakeUserRepository.verifyEmailAuthNumber(fakeEmail, authNum) } returns ApiResult.Failure(code = -1, "인증번호가 다르게 입력되었어요")
                // when
                process(SignUpUiAction.ClickVerifyAuthNum)
                // then
                uiState.test {
                    with(awaitItem()) {
                        emailAuthState shouldBe AuthenticationState.REJECTED
                        authNumberSupportingTextResId shouldBe R.string.email_auth_num_is_different
                    }
                }
            }
        }
    @Test
    fun `Action_ClickVerifyAuthNum process 검증 API NetworkError - 네트워크 확인 다이얼로그 노출`() =
        runTest {
            with(signUpViewModel()) {
                // given
                val fakeEmail = "test@example.com"
                val authNum = "123456"
                process(SignUpUiAction.ChangeEmailAuthNum(authNum))
                process(SignUpUiAction.ChangeEmail(fakeEmail))
                coEvery { fakeUserRepository.verifyEmailAuthNumber(fakeEmail, authNum) } returns ApiResult.NetworkError(IOException())
                // when
                process(SignUpUiAction.ClickVerifyAuthNum)
                // then
                uiState.test {
                    with(awaitItem()) {
                        failureDialogState shouldBe SignUpUiState.FailureDialogState(
                            title = NETWORK_IS_UNSTABLE,
                            message = CHECK_NETWORK_AND_TRY_AGAIN,
                            retryAction = SignUpUiAction.ClickVerifyAuthNum
                        )
                    }
                }
            }
        }

    @Test
    fun `Action_ClickVerifyAuthNum process 검증 API UnknownError - 알 수 없는 오류 다이얼로그 노출`() =
        runTest {
            with(signUpViewModel()) {
                // given
                val fakeEmail = "test@example.com"
                val authNum = "123456"
                process(SignUpUiAction.ChangeEmail(fakeEmail))
                process(SignUpUiAction.ChangeEmailAuthNum(authNum))
                coEvery { fakeUserRepository.verifyEmailAuthNumber(fakeEmail, authNum) } returns ApiResult.UnknownError(throwable = null)
                // when
                process(SignUpUiAction.ClickVerifyAuthNum)
                // then
                uiState.test {
                    with(awaitItem()) {
                        failureDialogState shouldBe SignUpUiState.FailureDialogState(
                            title = UNKNOWN_ERROR,
                            message = CONTACT_DEVELOPER_IF_PERSISTS,
                        )
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

    /*
    * ******************************************
    * NAME
    * ******************************************
    */

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

    /*
    * ******************************************
    * ID
    * ******************************************
    */

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
                    idValidationState shouldBe NONE
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
                    idValidationState shouldBe NONE
                    idErrorMessageResId shouldBe R.string.id_format_guide
                }
            }
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
                with(awaitItem()) {
                    idValidationState shouldBe NONE
                    idErrorMessageResId shouldBe R.string.please_enter_id
                }
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
            coEvery { fakeUserRepository.isNotDuplicateId(id) } returns ApiResult.Success(true)
            process(SignUpUiAction.ChangeUserId(id))
            // when
            process(SignUpUiAction.ClickIdDuplicateCheck)
            // then
            uiState.test {
                with(awaitItem()) {
                    idValidationState shouldBe AVAILABLE
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
            coEvery { fakeUserRepository.isNotDuplicateId(id) } returns ApiResult.Failure(code = -1, "이미 존재하는 아이디 입니다.")
            process(SignUpUiAction.ChangeUserId(id))
            // when
            process(SignUpUiAction.ClickIdDuplicateCheck)
            // then
            uiState.test {
                with(awaitItem()) {
                    idValidationState shouldBe NOT_AVAILABLE
                    idErrorMessageResId shouldBe R.string.id_cannot_be_used
                }
            }
        }
    }

    @Test
    fun `Action_ClickIdDuplicateCheck process 정규식 통과 O, API 성공, NetworkError - 상태 업데이트`() = runTest {
        with(signUpViewModel()) {
            // given
            val id = "test1234"
            coEvery { fakeUserRepository.isNotDuplicateId(id) } returns ApiResult.NetworkError(IOException())
            process(SignUpUiAction.ChangeUserId(id))
            // when
            process(SignUpUiAction.ClickIdDuplicateCheck)
            // then
            uiState.test {
                with(awaitItem()) {
                    failureDialogState shouldBe SignUpUiState.FailureDialogState(
                        title = NETWORK_IS_UNSTABLE,
                        message = CHECK_NETWORK_AND_TRY_AGAIN,
                        retryAction = SignUpUiAction.ClickIdDuplicateCheck
                    )
                }
            }
        }
    }

    @Test
    fun `Action_ClickIdDuplicateCheck process 정규식 통과 O, API 성공, UnknownError - 상태 업데이트`() = runTest {
        with(signUpViewModel()) {
            // given
            val id = "test1234"
            val expected = ApiResult.UnknownError(throwable = Throwable(message = "Unknown Error"))
            coEvery { fakeUserRepository.isNotDuplicateId(id) } returns expected
            process(SignUpUiAction.ChangeUserId(id))
            // when
            process(SignUpUiAction.ClickIdDuplicateCheck)
            // then
            uiState.test {
                with(awaitItem()) {
                    failureDialogState shouldBe SignUpUiState.FailureDialogState(
                        title = expected.throwable?.message ?: UNKNOWN_ERROR,
                        message = CONTACT_DEVELOPER_IF_PERSISTS,
                    )
                }
            }
        }
    }

    /*
    * ******************************************
    * PASSWORD
    * ******************************************
    */

    @Test
    fun `Action_ChangeUserPw process 정규식&길이 통과, 체크 pw 동일 - 정규식 검사 통과 O, 체크 pw 에러메시지 empty`() = runTest {
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
                    isPasswordValidFormat shouldBe true
                    isPasswordValidLength shouldBe true
                    isEqualsPassword shouldBe true
                    pwCheckErrorMessageResId shouldBe R.string.empty
                }
            }
        }
    }

    @Test
    fun `Action_ChangeUserPw process 정규식&길이 통과, 체크 pw 비동일 - 정규식 검사 통과 여부 O, pwCheck 에러메세지 O`() =
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
                        isPasswordValidFormat shouldBe true
                        isPasswordValidLength shouldBe true
                        isEqualsPassword shouldBe false
                        pwCheckErrorMessageResId shouldBe R.string.password_is_incorrect
                    }
                }
            }
        }

    @Test
    fun `Action_ChangeUserPw process 정규식 통과 X, pw 길이 통과 O, 체크 pw 동일 - pw 에러메세지 O, pwCheck 에러메세지 empty`() =
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
                        isPasswordValidFormat shouldBe false
                        isPasswordValidLength shouldBe true
                        isEqualsPassword shouldBe true
                        pwCheckErrorMessageResId shouldBe R.string.empty
                    }
                }
            }
        }

    @Test
    fun `Action_ChangeUserPw process 정규식 통과 X, pw 길이 통과 X, 체크 pw 비동일 - pw 에러메세지 O, pwCheck 에러메세지 O`() =
        runTest {
            with(signUpViewModel()) {
                // given
                val userPw = "pw123"
                val userPwCheck = userPw + "az"
                // when
                process(SignUpUiAction.ChangeUserPw(userPw))
                process(SignUpUiAction.ChangeUserPwCheck(userPwCheck))
                // then
                uiState.test {
                    with(awaitItem()) {
                        pw shouldBe userPw
                        pwCheck shouldBe userPwCheck
                        isPasswordValidFormat shouldBe false
                        isPasswordValidLength shouldBe false
                        isEqualsPassword shouldBe false
                        pwCheckErrorMessageResId shouldBe R.string.password_is_incorrect
                    }
                }
            }
        }

    /*
    * ******************************************
    * COMMON
    * ******************************************
    */

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
    fun `Action_ChangeFocusState process - focusState 업데이트`() = runTest {
        with(signUpViewModel()) {
            SignUpUiState.FocusState.entries.forEach {
                // given
                val focusState = it
                // when
                process(SignUpUiAction.ChangeFocusState(focusState))
                // then
                uiState.test { awaitItem().focusState shouldBe focusState }
            }
        }
    }

    @Test
    fun `Action_ClickSignUp process API Success - 스낵바 노출, 완료 이벤트`() = runTest {
        with(signUpViewModel()) {
            // given
            val loginId = "test123"
            val password = "Test123!"
            val email = "test@example.com"
            val name = "Test"
            process(SignUpUiAction.ChangeUserId(loginId))
            process(SignUpUiAction.ChangeUserPw(password))
            process(SignUpUiAction.ChangeEmail(email))
            process(SignUpUiAction.ChangeUserName(name))
            coEvery { fakeUserRepository.signUp(
                loginId = loginId,
                password = password,
                email = email,
                nickname = name,
            ) } returns ApiResult.Success(User.Default)

            // when
            process(SignUpUiAction.ClickSignUp)

            // then
            effect.test {
                awaitItem() shouldBe SignUpUiEffect.ShowSnackBer(R.string.sign_up_complete)
                awaitItem() shouldBe SignUpUiEffect.SignUpUiComplete
            }
        }
    }

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
                    authNumberSupportingTextResId shouldBe R.string.email_auth_time_has_expired
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
