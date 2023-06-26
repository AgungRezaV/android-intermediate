package com.dicoding.submissionone.data

import com.dicoding.submissionone.data.response.AddStoryResponse
import com.dicoding.submissionone.data.response.LoginResponse
import com.dicoding.submissionone.data.response.RegisterResponse
import com.dicoding.submissionone.data.response.UserData
import com.dicoding.submissionone.ui.story.AllStoriesResponse
import com.dicoding.submissionone.ui.story.ListStory

object DummyData {
    fun generateDummyListStoryResponse(): AllStoriesResponse {
        val item = ArrayList<ListStory>()
        for (i in 0 until 10) {
            val story = ListStory(
                "user-5Tmi-atNlu7NpI2d",
                "Testing",
                "Ini Untuk Testing",
                "https://i.kym-cdn.com/entries/icons/original/000/026/642/kot1.jpg",
                "26-06-2023",
                106.64356,
                106.2443,
            )
            item.add(story)
        }
        return AllStoriesResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = item
        )
    }

    fun generateDummyLoginResponse(): LoginResponse {
        return LoginResponse(
            false,
            "message",
            loginResult = LoginResponse.LoginResult(
                userId = "123456",
                name = "Joe Biden",
                token = "aarea231dqw12"
            )
        )
    }

    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            false,
            "success"
        )
    }

    fun generateDummyStoryWithMapsResponse(): AllStoriesResponse {
        val items: MutableList<ListStory> = arrayListOf()
        for (i in 0..100) {
            val story = ListStory(
                "id + $i",
                i.toString(),
                "created + $i",
                "name + $i",
                "description + $i",
                i.toDouble(),
                i.toDouble(),
            )
            items.add(story)
        }
        return AllStoriesResponse(
            false,
            "success",
            items as ArrayList<ListStory>
        )
    }

    fun generateDummyAddNewStoryResponse(): AddStoryResponse {
        return AddStoryResponse(
            false,
            "success"
        )
    }

    fun generateDummyGetUser(): UserData {
        return UserData(
            "namaku",
            "token",
            true
        )
    }
}