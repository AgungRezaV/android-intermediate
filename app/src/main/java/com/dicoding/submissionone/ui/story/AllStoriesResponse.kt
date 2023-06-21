package com.dicoding.submissionone.ui.story

data class AllStoriesResponse(
    val error: Boolean,
    val message: String,
    val listStory: ArrayList<ListStory>
)