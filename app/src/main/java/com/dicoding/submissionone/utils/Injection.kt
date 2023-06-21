package com.dicoding.submissionone.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.submissionone.data.retrofit.ApiConfig

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(preferences, apiService)
    }
}