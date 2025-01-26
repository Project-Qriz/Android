package com.qriz.core.data.token.token

import com.qriz.app.core.datastore.TokenDataStore
import com.qriz.core.data.token.token.repository.TokenRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TokenRepositoryTest {

    private val dataStore = mockk<TokenDataStore>(relaxed = true)

    private val repository = TokenRepositoryImpl(dataStore)

    @Test
    fun `저장된 토큰이 존재할 때 토큰을 반환한다`() = runTest {
        //given
        val accessToken = "access"
        val refreshToken = "refresh"
        coEvery { dataStore.flowAccessToken() } returns flow { emit(accessToken) }
        coEvery { dataStore.flowRefreshToken() } returns flow { emit(refreshToken) }

        //when
        val savedAccessToken = repository.getAccessToken()
        val savedRefreshToken = repository.getRefreshToken()

        //then
        Assertions.assertEquals(accessToken, savedAccessToken)
        Assertions.assertEquals(refreshToken, savedRefreshToken)
    }

    @Test
    fun `저장된 토큰이 없을 경우 null을 반환한다`() = runTest {
        //given
        coEvery { dataStore.flowAccessToken() } returns flow { emit("") }
        coEvery { dataStore.flowRefreshToken() } returns flow { emit("") }

        //when
        val accessToken = repository.getAccessToken()
        val refreshToken = repository.getRefreshToken()

        //then
        Assertions.assertNull(accessToken)
        Assertions.assertNull(refreshToken)
    }

    @Test
    fun `Repository 토큰 저장 메소드 호출 시 DataStore의 토큰 저장 메소드가 호출된다`() = runTest {
        //given
        val accessToken = "access"
        val refreshToken = "refresh"
        coEvery { dataStore.saveToken(accessToken, refreshToken) } returns Unit

        //when
        repository.saveToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )

        //then
        coVerify { dataStore.saveToken(accessToken, refreshToken) }
    }

    @Test
    fun `Repository 토큰 clear 메소드 호출 시 DataStore의 토큰 clear 메소드가 호출된다`() = runTest {
        //given
        coEvery { dataStore.clearToken() } returns Unit

        //when
        repository.clearToken()

        //then
        coVerify { dataStore.clearToken() }
    }
}
