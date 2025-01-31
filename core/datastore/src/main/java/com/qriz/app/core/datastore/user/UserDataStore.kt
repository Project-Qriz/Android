package com.qriz.app.core.datastore.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.qriz.app.core.datastore.util.crypto.CryptographyUtil
import com.qriz.app.core.datastore.user.mapper.toUser
import com.qriz.app.core.datastore.user.mapper.toUserEntity
import com.qriz.app.core.datastore.user.model.UserEntity
import com.quiz.app.core.data.user.user_api.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserDataStore @Inject constructor(
    @ApplicationContext context: Context,
    private val jsonSerializer: Json,
    private val cryptographyUtil: CryptographyUtil,
) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    fun getUserFlow(): Flow<User?> =
        dataStore.data.catch { emit(emptyPreferences()) }.map { preferences ->
            val saved = preferences[USER_KEY] ?: return@map null
            val decryptedUserString = cryptographyUtil.decrypt(saved)
            jsonSerializer.decodeFromString<UserEntity>(decryptedUserString)
        }.map { it?.toUser() }

    suspend fun saveUser(user: User) {
        val userString = jsonSerializer.encodeToString(user.toUserEntity())
        val encryptedUser = cryptographyUtil.encrypt(userString)

        dataStore.edit { preferences ->
            preferences[USER_KEY] = encryptedUser
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(USER_KEY)
        }
    }

    companion object {
        private val USER_KEY = stringPreferencesKey("user")
        private const val USER_DATA_STORE_NAME = "user_store"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            USER_DATA_STORE_NAME
        )
    }
}
