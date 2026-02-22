package com.qriz.app.core.data.user.user.repository

import com.qriz.app.core.data.user.user.mapper.toDataModel
import com.qriz.app.core.datastore.TokenDataStore
import com.qriz.app.core.model.ApiResult
import com.qriz.app.core.model.flatMapSuspend
import com.qriz.app.core.model.map
import com.qriz.app.core.network.user.api.UserApi
import com.qriz.app.core.network.user.model.request.EmailAuthenticationRequest
import com.qriz.app.core.network.user.model.request.FindIdRequest
import com.qriz.app.core.network.user.model.request.FindPwdRequest
import com.qriz.app.core.network.user.model.request.JoinRequest
import com.qriz.app.core.network.user.model.request.LoginRequest
import com.qriz.app.core.network.user.model.request.ResetPwdRequest
import com.qriz.app.core.network.user.model.request.SingleEmailRequest
import com.qriz.app.core.network.user.model.request.VerifyPwdResetRequest
import com.quiz.app.core.data.user.user_api.model.LoginType
import com.quiz.app.core.data.user.user_api.model.SocialLoginType
import com.quiz.app.core.data.user.user_api.model.User
import com.quiz.app.core.data.user.user_api.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val tokenDataStore: TokenDataStore,
) : UserRepository {
    private val user = MutableStateFlow<User?>(null)

    override suspend fun login(id: String, password: String): ApiResult<User> {
        val response = userApi.login(
            LoginRequest(
                username = id,
                password = password
            )
        ).map {
            tokenDataStore.saveToken(
                accessToken = it.accessToken,
                refreshToken = it.refreshToken
            )
            it.user.toDataModel(loginType = LoginType.EMAIL)
                .also { user -> this.user.value = user }
        }

        return response
    }

    override fun getUserFlow(): Flow<User> {
        return user.asStateFlow().filterNotNull()
    }

    override suspend fun getUser(): ApiResult<User> {
        val cached = user.firstOrNull()
        val result = if (cached != null) {
            ApiResult.Success(cached)
        } else {
            val response = getUserProfileFromServer()
            if (response is ApiResult.Success) {
                user.update { response.data }
            }
            response
        }

        return result
    }

    override suspend fun fetchUser(): ApiResult<Unit> {
        return when (val result = getUserProfileFromServer()) {
            is ApiResult.Success -> {
                user.update { result.data }
                ApiResult.Success(Unit)
            }
            is ApiResult.Failure -> result
            is ApiResult.NetworkError -> result
            is ApiResult.UnknownError -> result
        }
    }

    private suspend fun getUserProfileFromServer(): ApiResult<User> {
        return userApi.getUserProfile().map {
            it.toDataModel(loginType = LoginType.from(it.provider))
        }
    }

    override suspend fun requestEmailAuthNumber(email: String): ApiResult<Unit> =
        userApi.sendAuthEmail(SingleEmailRequest(email))

    override suspend fun verifyEmailAuthNumber(
        email: String,
        authenticationNumber: String,
    ): ApiResult<Unit> {
        return userApi.verifyEmailAuthenticationNumber(
            EmailAuthenticationRequest(
                email = email,
                authNum = authenticationNumber,
            )
        )
    }

    override suspend fun isNotDuplicateId(id: String): ApiResult<Boolean> {
        return userApi.checkDuplicateId(id).map { it.available }
    }

    override suspend fun signUp(
        loginId: String,
        password: String,
        email: String,
        nickname: String,
    ): ApiResult<User> {
        return userApi.signUp(
            JoinRequest(
                username = loginId,
                password = password,
                email = email,
                nickname = nickname,
            )
        ).flatMapSuspend {
            login(
                loginId,
                password
            )
        }
    }

    override suspend fun sendEmailToFindId(email: String): ApiResult<Unit> {
        return userApi.sendEmailToFindId(
            request = FindIdRequest(
                email = email
            )
        )
    }

    override suspend fun sendEmailToFindPassword(email: String): ApiResult<Unit> {
        return userApi.sendEmailToPwd(
            request = FindPwdRequest(
                email = email
            )
        )
    }

    override suspend fun verifyPasswordAuthNumber(
        email: String,
        authNumber: String,
    ): ApiResult<String> {
        return userApi.verifyPwdReset(
            request = VerifyPwdResetRequest(
                email = email,
                authNumber = authNumber
            )
        ).map { it.resetToken }
    }

    override suspend fun resetPassword(password: String, resetToken: String): ApiResult<Unit> {
        return userApi.resetPwd(
            request = ResetPwdRequest(
                password = password,
                resetToken = resetToken,
            )
        )
    }

    override suspend fun logout() {
        tokenDataStore.clearToken()
    }

    override suspend fun withdraw(): ApiResult<Unit> {
        val result = userApi.withdraw()
        if (result is ApiResult.Success) {
            tokenDataStore.clearToken()
        }
        return result
    }

    override suspend fun socialLogin(socialLoginType: SocialLoginType, token: String) =
        userApi.socialLogin(
            mapOf(
                "provider" to socialLoginType.name.lowercase(),
                when (socialLoginType) {
                    SocialLoginType.KAKAO -> "authCode"
                    SocialLoginType.GOOGLE -> "serverAuthCode"
                } to token,
                "platform" to "android"
            )
        ).map {
            tokenDataStore.saveToken(
                accessToken = it.accessToken,
                refreshToken = it.refreshToken
            )
            it.user.toDataModel(
                loginType = when (socialLoginType) {
                    SocialLoginType.KAKAO -> LoginType.KAKAO
                    SocialLoginType.GOOGLE -> LoginType.GOOGLE
                }
            ).also { user -> this.user.value = user }
        }
}
