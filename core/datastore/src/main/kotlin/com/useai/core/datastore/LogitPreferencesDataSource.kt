package com.useai.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogitPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val accessTokenKey = stringPreferencesKey("access_token")
    val accessToken: Flow<String?> = dataStore.data.map { it[accessTokenKey] }

    suspend fun setAccessToken(token: String) {
        dataStore.edit {
            it[accessTokenKey] = token
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }
}
