package com.qriz.core.data.user

import com.qriz.app.core.data.user.user.repository.UserRepositoryImpl
import com.qriz.app.core.network.common.NetworkResponse
import com.qriz.app.core.network.common.exception.QrizNetworkException
import com.qriz.app.core.network.user.api.UserApi
import com.qriz.app.core.network.user.model.request.FindIdRequest
import com.qriz.app.core.network.user.model.request.FindPwdRequest
import com.qriz.app.core.network.user.model.request.ResetPwdRequest
import com.qriz.app.core.network.user.model.request.VerifyPwdResetRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertFailsWith

class UserRepositoryTest {

    private val mockApi = mockk<UserApi>()

    private val userRepository = UserRepositoryImpl(userApi = mockApi)

    private val mockEmail = "test@gmail.com"

    @Test
    fun `아이디 찾기 메소드 실행 시 이메일 전송 api를 호출한다`() = runTest {
        //given
        coEvery { mockApi.sendEmailToFindId(FindIdRequest(email = mockEmail)) } returns NetworkResponse(
            code = 1,
            message = "",
            data = Unit
        )

        //when
        userRepository.sendEmailToFindId(mockEmail)

        //then
        coVerify { mockApi.sendEmailToFindId(FindIdRequest(email = mockEmail)) }
    }

    @Test
    fun `가입된 이메일로 인증번호 요청 시 비밀번호 변경을 위한 이메일 전송 api를 호출한다`() = runTest {
        //given
        coEvery { mockApi.sendEmailToPwd(FindPwdRequest(email = mockEmail)) } returns NetworkResponse(
            code = 1,
            message = "",
            data = Unit
        )

        //when
        userRepository.sendEmailToFindPassword(email = mockEmail)

        //then
        coVerify { mockApi.sendEmailToPwd(FindPwdRequest(email = mockEmail)) }
    }

    @Test
    fun `가입되지 않은 이메일로 인증번호 요청 시 예외를 던진다`() = runTest {
        //given
        val mockFailResponse = NetworkResponse(
            code = -1,
            message = "가입되지 않은 이메일입니다.",
            data = Unit,
        )
        coEvery {
            mockApi.sendEmailToPwd(
                request = FindPwdRequest(
                    email = mockEmail
                )
            )
        } returns mockFailResponse

        //when
        val exception = assertFailsWith<QrizNetworkException> {
            userRepository.sendEmailToFindPassword(email = mockEmail)
        }

        //then
        assert(exception.code == mockFailResponse.code)
        assert(exception.message == mockFailResponse.message)
    }

    @Test
    fun `비밀번호 변경을 위한 인증번호 인증 요청 시 인증 api를 호출한다`() = runTest {
        //given
        val mockVerifyAuthNumber = "123456"
        coEvery {
            mockApi.verifyPwdReset(
                VerifyPwdResetRequest(
                    authNumber = mockVerifyAuthNumber
                )
            )
        } returns NetworkResponse(
            code = 1,
            message = "",
            data = Unit,
        )

        //when
        userRepository.verifyPasswordAuthNumber(authNumber = mockVerifyAuthNumber)

        //then
        coVerify { mockApi.verifyPwdReset(VerifyPwdResetRequest(authNumber = mockVerifyAuthNumber)) }
    }

    @Test
    fun `잘못된 인증번호로 인증 시도 시 잘못된 응답코드를 받고 예외를 던진다`() = runTest {
        //given
        val mockFailVerifyAuthNumber = "111111"
        val mockFailMessage = "잘못된 인증번호입니다."
        val failResponse = NetworkResponse(
            code = -1,
            message = mockFailMessage,
            data = Unit,
        )

        coEvery {
            mockApi.verifyPwdReset(
                request = VerifyPwdResetRequest(authNumber = mockFailVerifyAuthNumber)
            )
        } returns failResponse

        //when
        val exception = assertFailsWith<QrizNetworkException> {
            userRepository.verifyPasswordAuthNumber(authNumber = mockFailVerifyAuthNumber)
        }

        //then
        assert(exception.code == failResponse.code)
        assert(exception.message == failResponse.message)
    }

    @Test
    fun `비밀번호 변경 요청 시 비밀번호 변경 api를 호출한다`() = runTest {
        //given
        val mockPassword = "test1234"
        coEvery {
            mockApi.resetPwd(
                request = ResetPwdRequest(
                    password = mockPassword
                )
            )
        } returns NetworkResponse(
            code = 1,
            message = "",
            data = Unit,
        )

        //when
        userRepository.resetPassword(password = mockPassword)

        //then
        coVerify {
            mockApi.resetPwd(
                request = ResetPwdRequest(password = mockPassword)
            )
        }
    }

    @Test
    fun `비밀번호 변경 요청이 실패할 경우 예외를 던진다`() = runTest {
        //given
        val mockPassword = "test1234"
        val failResponse = NetworkResponse(
            code = -1,
            message = "잘못된 비밀번호 형식입니다.",
            data = Unit,
        )

        coEvery {
            mockApi.resetPwd(
                request = ResetPwdRequest(
                    password = mockPassword,
                )
            )
        } returns failResponse

        //when
        val exception = assertFailsWith<QrizNetworkException> {
            userRepository.resetPassword(password = mockPassword)
        }

        //then
        assert(exception.code == failResponse.code)
        assert(exception.message == failResponse.message)
    }

    @Test
    fun `아이디 찾기 이메일 요청 시 서버에서 받은 응답의 코드가 올바르지 않을 때 예외를 던진다`() = runTest {
        //given
        val failResponse = NetworkResponse(
            code = 0,
            message = "테스트",
            data = Unit,
        )
        coEvery { mockApi.sendEmailToFindId(FindIdRequest(email = mockEmail)) } returns failResponse

        //when
        val exception = assertFailsWith<QrizNetworkException> {
            userRepository.sendEmailToFindId(mockEmail)
        }

        //then
        assert(exception.code == failResponse.code)
        assert(exception.message == failResponse.message)
    }
}
