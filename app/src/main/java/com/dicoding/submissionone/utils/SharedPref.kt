package com.dicoding.submissionone.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dicoding.submissionone.data.response.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SharedPref private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<UserData> {
        return dataStore.data.map { preferences ->
            UserData(
                preferences[NAME_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(userData: UserData) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = userData.name
            preferences[TOKEN_KEY] = userData.token
            preferences[STATE_KEY] = userData.isLogin
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SharedPref? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): SharedPref {
            return INSTANCE ?: synchronized(this) {
                val instance = SharedPref(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}