package com.qriz.app.core.data.user.user.repository

import com.qriz.app.core.network.user.api.UserApi
import com.qriz.app.core.network.user.mapper.toDataModel
import com.qriz.app.core.network.user.model.request.JoinRequest
import com.qriz.app.core.network.user.model.request.LoginRequest
import com.qriz.app.core.datastore.TokenDataStore
import com.quiz.app.core.data.user.user_api.model.User
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    tokenDataStore: TokenDataStore,
) : UserRepository {
    override val flowLogin: Flow<Boolean> = tokenDataStore.flowRefreshToken().map { it.isNotEmpty() }

    override suspend fun login(id: String, password: String): User {
        val response = userApi.login(
            LoginRequest(
                username = id,
                password = password
            )
        )

        return response.data.toDataModel()
    }

    /* TODO: 주소 값 나오면 실제 API 연결 */
    override suspend fun sendAuthenticationNumber(email: String): Boolean = true

    /* TODO: 주소 값 나오면 실제 API 연결 */
    override suspend fun verifyAuthenticationNumber(authenticationNumber: String): Boolean {
        return true
    }

    override suspend fun checkDuplicateId(id: String): Boolean {
        return true
    }

    override suspend fun signUp(
        loginId: String, password: String, email: String, nickname: String
    ): User {
        userApi.signUp(
            JoinRequest(
                username = loginId,
                password = password,
                email = email,
                nickname = nickname,
            )
        )

        val user = login(loginId, password)
        return user
    }
}
