package com.dicoding.submissionone.ui.story.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionone.data.response.UserData
import com.dicoding.submissionone.utils.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun addStory(token: String, file: MultipartBody.Part, description: RequestBody, lat: Float, lon: Float) =
        storyRepository.addStory(token, file, description, lat, lon)

    fun getUser(): LiveData<UserData> {
        return storyRepository.getUserData()
    }
}