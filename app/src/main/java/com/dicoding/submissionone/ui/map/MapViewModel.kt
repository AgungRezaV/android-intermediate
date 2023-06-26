package com.dicoding.submissionone.ui.map

import androidx.lifecycle.ViewModel
import com.dicoding.submissionone.utils.StoryRepository

class MapViewModel(private val repository: StoryRepository): ViewModel() {
    fun getMapLocation(token: String) = repository.getStoryLoc(token)

}