package com.qriz.core.data.user

import com.qriz.app.core.data.user.user.repository.UserRepositoryImpl
import com.qriz.app.core.network.common.NetworkResponse
import com.qriz.app.core.network.common.exception.QrizNetworkException
import com.qriz.app.core.network.user.api.UserApi
import com.qriz.app.core.network.user.model.request.FindIdRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.fail

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
    fun `서버에서 받은 응답의 코드가 올바르지 않을 때 예외를 던진다`() = runTest {
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
