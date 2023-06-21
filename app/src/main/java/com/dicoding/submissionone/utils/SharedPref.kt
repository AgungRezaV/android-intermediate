package com.dicoding.submissionone.utils

import android.content.Context

class SharedPref constructor(context: Context) {
    private val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("TOKEN_KEY", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("TOKEN_KEY", null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove("TOKEN_KEY").apply()
    }

    companion object {
        private const val SHARED_PREF_NAME = "shared_pref"
    }
}