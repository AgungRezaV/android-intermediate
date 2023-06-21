package com.dicoding.submissionone.ui.register

import androidx.lifecycle.ViewModel
import com.dicoding.submissionone.utils.StoryRepository

class RegisterViewModel (private val repos: StoryRepository): ViewModel() {
    fun register(name: String, email: String, password: String) = repos.repoRegister(name, email, password)
}