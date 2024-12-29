package com.qriz.app.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataStore(context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    fun flowRefreshToken(): Flow<String> {
        return dataStore.data.map { it[REFRESH_TOKEN_KEY] ?: "" }
    }

    companion object {
        private const val USER_DATA_STORE_NAME = "user"
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refreshToken")

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            USER_DATA_STORE_NAME
        )
    }
}
