package com.qriz.app.core.data.repository

import com.qriz.app.core.network.api.UserApi
import com.qriz.app.datastore.UserDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    userDataStore: UserDataStore,
): UserRepository {
    override val flowLogin: Flow<Boolean> = userDataStore.flowRefreshToken().map { it.isNotEmpty() }
}
