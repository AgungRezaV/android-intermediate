package com.dicoding.submissionone.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.submissionone.ui.story.ListStory
import com.dicoding.submissionone.utils.StoryRepository

class MainActivityViewModel(private val repository: StoryRepository): ViewModel() {
    fun getStory(): LiveData<PagingData<ListStory>>{
        return repository.getStory().cachedIn(viewModelScope)
    }
}