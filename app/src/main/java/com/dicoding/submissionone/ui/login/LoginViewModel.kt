package com.dicoding.submissionone.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.submissionone.data.response.UserData
import com.dicoding.submissionone.utils.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repos: StoryRepository): ViewModel() {
    fun login(email: String, password: String) = repos.repoLogin(email, password)

    fun saveUser(userData: UserData) {
        viewModelScope.launch {
            repos.saveUserData(userData)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repos.logout()
        }
    }
}