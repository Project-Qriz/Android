package com.qriz.core.networ.core

import com.qriz.app.core.network.core.adapter.QrizCallAdapterFactory
import com.qriz.app.core.network.core.auth.AuthManager
import com.qriz.app.core.network.core.const.ACCESS_TOKEN_HEADER_KEY
import com.qriz.app.core.network.core.interceptor.AuthInterceptor
import com.qriz.app.core.network.core.interceptor.AuthInterceptor.Companion.TOKEN_PREFIX
import com.qriz.app.core.network.user.api.UserApi
import com.qriz.app.core.network.user.model.request.LoginRequest
import com.qriz.app.core.testing.MainDispatcherRule
import com.qriz.core.data.token.token_api.TokenRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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

    private val mockAuthManager = mockk<AuthManager>()

    private val json = Json

    private val successResponseBody = """
                {
                    "code": 1,
                    "msg": "로그인성공",
                    "data": {
                        "name" : "test"
                    }
                }
            """.trimIndent()

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val okHttpClient = OkHttpClient.Builder().addInterceptor(
                AuthInterceptor(
                    json = json,
                    authManager = mockAuthManager
                )
            ).build()

        val json = Json { ignoreUnknownKeys = true }
        val converterFactory = json.asConverterFactory("application/json".toMediaType())

        retrofit = Retrofit.Builder().baseUrl(mockWebServer.url("/")).client(okHttpClient)
            .addCallAdapterFactory(QrizCallAdapterFactory()).addConverterFactory(converterFactory)
            .build()

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
            "$TOKEN_PREFIX$accessToken",
            request.getHeader(ACCESS_TOKEN_HEADER_KEY)
        )
    }

    @Test
    fun `AuthInterceptor Test - 응답에 token이 포함되어 있으면 repository 저장 메소드를 호출한다`() = runTest {
        //given
        val accessToken = "access"
        coEvery { mockTokenRepository.getAccessToken() } returns null
        mockWebServer.enqueue(
            MockResponse().addHeader(
                ACCESS_TOKEN_HEADER_KEY,
                accessToken
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
            mockTokenRepository.saveToken(accessToken = accessToken)
        }
    }

    //TODO: ResponseConvertInterceptor 테스트 코드 추가
    //TODO: CallAdapter 테스트 코드 추가
}
