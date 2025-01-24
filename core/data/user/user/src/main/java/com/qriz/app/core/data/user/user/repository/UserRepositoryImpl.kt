package com.qriz.app.core.data.user.user.repository

import com.qriz.app.core.network.common.util.verifyResponseCode
import com.qriz.app.core.network.user.api.UserApi
import com.qriz.app.core.network.user.mapper.toDataModel
import com.qriz.app.core.network.user.model.request.FindIdRequest
import com.qriz.app.core.network.user.model.request.FindPwdRequest
import com.qriz.app.core.network.user.model.request.JoinRequest
import com.qriz.app.core.network.user.model.request.LoginRequest
import com.qriz.app.core.network.user.model.request.ResetPwdRequest
import com.qriz.app.core.network.user.model.request.VerifyPwdResetRequest
import com.quiz.app.core.data.user.user_api.model.User
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
) : UserRepository {
    private val client = MutableStateFlow<User?>(null)

    override suspend fun login(id: String, password: String): User {
        val response = userApi.login(
            LoginRequest(
                username = id,
                password = password
            )
        )
        val newClient = response.data.toDataModel()
        client.update { newClient }
        return newClient
    }

    override fun getClientFlow(): Flow<User> {
        return client.asStateFlow().filterNotNull()
    }

    /* TODO: 주소 값 나오면 실제 API 연결 */
    override suspend fun requestEmailAuthNumber(email: String) {
    }

    /* TODO: 주소 값 나오면 실제 API 연결 */
    override suspend fun verifyEmailAuthNumber(authenticationNumber: String): Boolean {
        return true
    }

    override suspend fun isNotDuplicateId(id: String): Boolean {
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

        val user = login(
            loginId,
            password
        )
        return user
    }

    override suspend fun sendEmailToFindId(email: String) {
        userApi.sendEmailToFindId(
            request = FindIdRequest(
                email = email
            )
        ).verifyResponseCode()
    }

    override suspend fun sendEmailToFindPassword(email: String) {
        userApi.sendEmailToPwd(
            request = FindPwdRequest(
                email = email
            )
        ).verifyResponseCode()
    }

    override suspend fun verifyPasswordAuthNumber(authNumber: String) {
        userApi.verifyPwdReset(
            request = VerifyPwdResetRequest(
                authNumber = authNumber
            )
        ).verifyResponseCode()
    }

    override suspend fun resetPassword(password: String) {
        userApi.resetPwd(
            request = ResetPwdRequest(
                password = password
            )
        ).verifyResponseCode()
    }
}
