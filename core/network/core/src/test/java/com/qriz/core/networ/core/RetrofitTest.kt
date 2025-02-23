package com.qriz.core.networ.core

import com.qriz.app.core.network.core.const.ACCESS_TOKEN_HEADER_KEY
import com.qriz.app.core.network.core.const.REFRESH_TOKEN_HEADER_KEY
import com.qriz.app.core.network.core.interceptor.AuthInterceptor
import com.qriz.app.core.network.core.interceptor.TokenAuthenticator
import com.qriz.app.core.network.user.api.UserApi
import com.qriz.app.core.network.user.model.request.LoginRequest
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.core.data.token.token_api.TokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class RetrofitTest {
    @get:Rule
    val testDispatcher = MainDispatcherRule()

    private lateinit var mockWebServer: MockWebServer
    private lateinit var userApi: UserApi
    private lateinit var retrofit: Retrofit

    private val mockTokenRepository = mockk<TokenRepository>(relaxed = true)

    private val successResponseBody = """
                {
                    "code": 1,
                    "msg": "로그인성공",
                    "data": {
                        "id": 3,
                        "username": "test",
                        "nickname" : "nickname"
                        "createdAt": "2024-12-31 15:34:47"
                        "previewTestStatus": "NOT_STARTED"
                    }
                }
            """.trimIndent()

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenRepository = mockTokenRepository))
            .authenticator(TokenAuthenticator(tokenRepository = mockTokenRepository)).build()

        val json = Json { ignoreUnknownKeys = true }
        val converterFactory = json.asConverterFactory("application/json".toMediaType())

        retrofit = Retrofit.Builder().baseUrl(mockWebServer.url("/")).client(okHttpClient)
            .addConverterFactory(converterFactory).build()

        userApi = retrofit.create(UserApi::class.java)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `AuthInterceptor Test - token이 null이 아닐 때 header에 access token을 포함한다`() = runTest {
        //given
        val accessToken = "access"
        coEvery { mockTokenRepository.getAccessToken() } returns accessToken
        mockWebServer.enqueue(MockResponse().setBody(successResponseBody))

        //when
        //아무 api나 호출한다.
        userApi.login(
            LoginRequest(
                "test",
                "password"
            )
        )
        val request = mockWebServer.takeRequest()

        //then
        Assertions.assertEquals(
            accessToken,
            request.getHeader(ACCESS_TOKEN_HEADER_KEY)
        )
    }

    @Test
    fun `AuthInterceptor Test - 응답에 token이 포함되어 있으면 repository 저장 메소드를 호출한다`() = runTest {
        //given
        val accessToken = "access"
        val refreshToken = "refresh"
        coEvery { mockTokenRepository.getAccessToken() } returns null
        mockWebServer.enqueue(
            MockResponse().addHeader(
                    ACCESS_TOKEN_HEADER_KEY,
                    accessToken
                ).addHeader(
                    REFRESH_TOKEN_HEADER_KEY,
                    refreshToken
                ).setBody(successResponseBody)
        )

        //when
        userApi.login(
            LoginRequest(
                "test",
                "password"
            )
        )

        //then
        coVerify {
            mockTokenRepository.saveToken(
                accessToken = accessToken,
                refreshToken = refreshToken,
            )
        }
    }

    @Test
    fun `TokenAuthenticator Test - 401 응답이 발생하면 refresh token을 포함하여 요청한다`() = runTest {
        //given
        val accessToken = "access"
        val refreshToken = "refresh"
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(401)
                .setBody("")
        )

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(successResponseBody)
        )

        coEvery { mockTokenRepository.flowTokenExist } returns flow { emit(true) }
        coEvery { mockTokenRepository.getAccessToken() } returns accessToken
        coEvery { mockTokenRepository.getRefreshToken() } returns refreshToken

        //when
        userApi.login(
            LoginRequest(
                "test",
                "password"
            )
        )
        val firstRequest = mockWebServer.takeRequest()
        val secondRequest = mockWebServer.takeRequest()

        //then
        Assertions.assertNull(firstRequest.getHeader(REFRESH_TOKEN_HEADER_KEY))
        Assertions.assertEquals(refreshToken, secondRequest.getHeader(REFRESH_TOKEN_HEADER_KEY))
    }
}
