package com.qriz.core.data.user

import com.qriz.app.core.data.user.user.repository.UserRepositoryImpl
import com.qriz.app.core.network.common.NetworkResponse
import com.qriz.app.core.network.user.api.UserApi
import com.qriz.app.core.network.user.model.request.FindIdRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class UserRepositoryTest {

    private val mockApi = mockk<UserApi>()

    private val userRepository = UserRepositoryImpl(userApi = mockApi)

    @Test
    fun `아이디 찾기 메소드 실행 시 이메일 전송 api를 호출한다`() = runTest {
        //given
        val email = "test@gmail.com"
        coEvery { mockApi.sendEmailToFindId(FindIdRequest(email = email)) } returns NetworkResponse(
            code = 200,
            message = "",
            data = Unit
        )

        //when
        userRepository.sendEmailToFindId(email)

        //then
        coVerify { mockApi.sendEmailToFindId(FindIdRequest(email = email)) }
    }
}
