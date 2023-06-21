package com.dicoding.submissionone.ui.login

import androidx.lifecycle.ViewModel
import com.dicoding.submissionone.utils.StoryRepository

class LoginViewModel(private val repos: StoryRepository): ViewModel() {
    fun login(email: String, password: String) = repos.repoLogin(email, password)
}