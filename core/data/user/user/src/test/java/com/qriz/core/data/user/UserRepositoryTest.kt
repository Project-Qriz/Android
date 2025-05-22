package com.qriz.core.data.user

import com.qriz.app.core.data.user.user.repository.UserRepositoryImpl
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.network.user.api.UserApi
import com.qriz.app.core.network.user.model.request.EmailAuthenticationRequest
import com.qriz.app.core.network.user.model.request.FindIdRequest
import com.qriz.app.core.network.user.model.request.FindPwdRequest
import com.qriz.app.core.network.user.model.request.JoinRequest
import com.qriz.app.core.network.user.model.request.LoginRequest
import com.qriz.app.core.network.user.model.request.ResetPwdRequest
import com.qriz.app.core.network.user.model.request.SingleEmailRequest
import com.qriz.app.core.network.user.model.request.VerifyPwdResetRequest
import com.qriz.app.core.network.user.model.response.DuplicateResponse
import com.qriz.app.core.network.user.model.response.JoinResponse
import com.qriz.app.core.network.user.model.response.LoginResponse
import com.qriz.app.core.network.user.model.response.UserProfileResponse
import com.qriz.app.core.network.user.model.response.VerifyPwdResetRespoonse
import com.quiz.app.core.data.user.user_api.model.PreviewTestStatus
import com.quiz.app.core.data.user.user_api.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserRepositoryTest {

    private val mockApi = mockk<UserApi>()

    private val userRepository = UserRepositoryImpl(userApi = mockApi)

    private val mockEmail = "test@gmail.com"

    @Test
    fun `로그인 성공 시 사용자 정보를 반환한다`() = runTest {
        //given
        val mockId = "testId"
        val mockPassword = "testPassword"
        val mockName = "테스터"
        val expectedUser = User(
            userId = mockId,
            email = mockEmail,
            name = mockName,
            previewTestStatus = PreviewTestStatus.NOT_STARTED
        )

        coEvery {
            mockApi.login(
                request = LoginRequest(
                    username = mockId,
                    password = mockPassword
                )
            )
        } returns ApiResult.Success(LoginResponse(userName = mockName))

        coEvery { mockApi.getUserProfile() } returns ApiResult.Success(
            UserProfileResponse(
                userId = expectedUser.userId,
                email = expectedUser.email,
                name = expectedUser.name,
                previewStatus = expectedUser.previewTestStatus.toString()
            )
        )

        //when
        val result = userRepository.login(
            id = mockId,
            password = mockPassword
        )

        //then
        coVerify {
            mockApi.login(
                request = LoginRequest(
                    username = mockId,
                    password = mockPassword
                )
            )
        }
        coVerify { mockApi.getUserProfile() }
        assertTrue(result is ApiResult.Success)
        val userData = (result as ApiResult.Success).data

        assertEquals(expectedUser, userData)
    }

    @Test
    fun `로그인 실패 시 에러를 반환한다`() = runTest {
        //given
        val mockId = "testId"
        val mockPassword = "wrongPassword"
        val errorMessage = "Invalid credentials"

        coEvery {
            mockApi.login(
                request = LoginRequest(
                    username = mockId,
                    password = mockPassword
                )
            )
        } returns ApiResult.Failure(
            message = errorMessage,
            code = 401
        )

        //when
        val result = userRepository.login(
            id = mockId,
            password = mockPassword
        )

        //then
        coVerify {
            mockApi.login(
                request = LoginRequest(
                    username = mockId,
                    password = mockPassword
                )
            )
        }
        assertTrue(result is ApiResult.Failure)
        assertEquals(
            401,
            (result as ApiResult.Failure).code
        )
        assertEquals(
            errorMessage,
            result.message
        )
    }

    @Test
    fun `회원가입 성공 시 사용자 정보를 반환하고 자동 로그인한다`() = runTest {
        //given
        val mockId = "testId"
        val mockPassword = "testPassword"
        val mockNickname = "테스터"

        coEvery {
            mockApi.signUp(
                request = JoinRequest(
                    username = mockId,
                    password = mockPassword,
                    email = mockEmail,
                    nickname = mockNickname
                )
            )
        } returns ApiResult.Success(
            JoinResponse(
                id = 1,
                userId = mockId,
                name = mockNickname
            )
        )

        coEvery {
            mockApi.login(
                request = LoginRequest(
                    username = mockId,
                    password = mockPassword
                )
            )
        } returns ApiResult.Success(LoginResponse(userName = mockNickname))

        coEvery { mockApi.getUserProfile() } returns ApiResult.Success(
            UserProfileResponse(
                userId = mockId,
                email = mockEmail,
                name = mockNickname,
                previewStatus = "NOT_STARTED"
            )
        )

        //when
        val result = userRepository.signUp(
            loginId = mockId,
            password = mockPassword,
            email = mockEmail,
            nickname = mockNickname
        )

        //then
        coVerify {
            mockApi.signUp(
                request = JoinRequest(
                    username = mockId,
                    password = mockPassword,
                    email = mockEmail,
                    nickname = mockNickname
                )
            )
        }
        coVerify {
            mockApi.login(
                request = LoginRequest(
                    username = mockId,
                    password = mockPassword
                )
            )
        }
        coVerify { mockApi.getUserProfile() }
        assertTrue(result is ApiResult.Success)
    }

    @Test
    fun `이메일 인증 번호 요청 시 API를 호출한다`() = runTest {
        //given
        coEvery { mockApi.sendAuthEmail(request = SingleEmailRequest(email = mockEmail)) } returns ApiResult.Success(Unit)

        //when
        val result = userRepository.requestEmailAuthNumber(email = mockEmail)

        //then
        coVerify { mockApi.sendAuthEmail(request = SingleEmailRequest(email = mockEmail)) }
        assertTrue(result is ApiResult.Success)
    }

    @Test
    fun `이메일 인증 번호 검증 시 API를 호출한다`() = runTest {
        //given
        val mockAuthNumber = "123456"
        coEvery {
            mockApi.verifyEmailAuthenticationNumber(
                request = EmailAuthenticationRequest(
                    email = mockEmail,
                    authNum = mockAuthNumber
                )
            )
        } returns ApiResult.Success(Unit)

        //when
        val result = userRepository.verifyEmailAuthNumber(
            email = mockEmail,
            authenticationNumber = mockAuthNumber
        )

        //then
        coVerify {
            mockApi.verifyEmailAuthenticationNumber(
                request = EmailAuthenticationRequest(
                    email = mockEmail,
                    authNum = mockAuthNumber
                )
            )
        }
        assertTrue(result is ApiResult.Success)
    }

    @Test
    fun `ID 중복 체크 시 API를 호출하고 중복 여부를 반환한다`() = runTest {
        //given
        val mockId = "testId"
        coEvery { mockApi.checkDuplicateId(userId = mockId) } returns ApiResult.Success(DuplicateResponse(available = true))

        //when
        val result = userRepository.isNotDuplicateId(id = mockId)

        //then
        coVerify { mockApi.checkDuplicateId(userId = mockId) }
        assertTrue(result is ApiResult.Success)
        assertTrue((result as ApiResult.Success).data)
    }

    @Test
    fun `아이디 찾기 메소드 실행 시 이메일 전송 api를 호출한다`() = runTest {
        //given
        coEvery { mockApi.sendEmailToFindId(request = FindIdRequest(email = mockEmail)) } returns ApiResult.Success(Unit)

        //when
        userRepository.sendEmailToFindId(email = mockEmail)

        //then
        coVerify { mockApi.sendEmailToFindId(request = FindIdRequest(email = mockEmail)) }
    }

    @Test
    fun `가입된 이메일로 인증번호 요청 시 비밀번호 변경을 위한 이메일 전송 api를 호출한다`() = runTest {
        //given
        coEvery { mockApi.sendEmailToPwd(request = FindPwdRequest(email = mockEmail)) } returns ApiResult.Success(Unit)

        //when
        userRepository.sendEmailToFindPassword(email = mockEmail)

        //then
        coVerify { mockApi.sendEmailToPwd(request = FindPwdRequest(email = mockEmail)) }
    }

    @Test
    fun `비밀번호 변경을 위한 인증번호 인증 요청 시 인증 api를 호출한다`() = runTest {
        //given
        val mockVerifyAuthNumber = "123456"
        val mockResetToken = "mockResetToken"
        coEvery {
            mockApi.verifyPwdReset(
                request = VerifyPwdResetRequest(
                    email = mockEmail,
                    authNumber = mockVerifyAuthNumber
                )
            )
        } returns ApiResult.Success(VerifyPwdResetRespoonse(resetToken = mockResetToken))

        //when
        val result = userRepository.verifyPasswordAuthNumber(
            email = mockEmail,
            authNumber = mockVerifyAuthNumber
        )

        //then
        coVerify {
            mockApi.verifyPwdReset(
                request = VerifyPwdResetRequest(
                    email = mockEmail,
                    authNumber = mockVerifyAuthNumber
                )
            )
        }
        assertTrue(result is ApiResult.Success && result.data == mockResetToken)
    }

    @Test
    fun `비밀번호 변경 요청 시 비밀번호 변경 api를 호출한다`() = runTest {
        //given
        val mockPassword = "test1234"
        val mockResetToken = "mockResetToken"
        coEvery {
            mockApi.resetPwd(
                request = ResetPwdRequest(
                    password = mockPassword,
                    resetToken = mockResetToken
                )
            )
        } returns ApiResult.Success(Unit)

        //when
        userRepository.resetPassword(
            password = mockPassword,
            resetToken = mockResetToken
        )

        //then
        coVerify {
            mockApi.resetPwd(
                request = ResetPwdRequest(
                    password = mockPassword,
                    resetToken = mockResetToken
                )
            )
        }
    }
}
