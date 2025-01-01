package com.qriz.app.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.qriz.app.crypto.CryptographyHelper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TokenDataStore(context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun getAccessToken(): String = dataStore.data.catch {
            emit(emptyPreferences())
        }.map { preferences ->
            val saved = preferences[ACCESS_TOKEN_KEY] ?: ""
            CryptographyHelper.decrypt(saved)
        }.first()

    suspend fun getRefreshToken(): String = dataStore.data.map { preferences ->
        val saved = preferences[REFRESH_TOKEN_KEY] ?: ""
        CryptographyHelper.decrypt(saved)
    }.first()

    suspend fun saveToken(accessToken: String, refreshToken: String) {
        val encryptedAccessToken = CryptographyHelper.encrypt(accessToken)
        val encryptedRefreshToken = CryptographyHelper.encrypt(refreshToken)

        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = encryptedAccessToken
            preferences[REFRESH_TOKEN_KEY] = encryptedRefreshToken
        }
    }
    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = ""
            preferences[REFRESH_TOKEN_KEY] = ""
        }
    }

    companion object {
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refreshToken")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("accessToken")

        private const val TOKEN_DATA_STORE_NAME = "token_store"

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(TOKEN_DATA_STORE_NAME)
    }
}
