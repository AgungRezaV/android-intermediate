package com.dicoding.submissionone.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.submissionone.data.request.LoginRequest
import com.dicoding.submissionone.data.request.RegisterRequest
import com.dicoding.submissionone.data.response.AddStoryResponse
import com.dicoding.submissionone.data.response.LoginResponse
import com.dicoding.submissionone.data.response.RegisterResponse
import com.dicoding.submissionone.data.response.UserData
import com.dicoding.submissionone.data.retrofit.ApiService
import com.dicoding.submissionone.ui.story.ListStory
import com.dicoding.submissionone.ui.story.PagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class StoryRepository(private val sharedPref: SharedPref, private val apiService: ApiService) {

    fun getStory(): LiveData<PagingData<ListStory>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                PagingSource(apiService, sharedPref)
            }
        ).liveData
    }

    fun rLogin(loginRequest: LoginRequest): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)

        try {
            val response = apiService.login(loginRequest)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("Login", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun repoRegister(name: String, email: String, password: String)
            : LiveData<Result<RegisterResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.register(RegisterRequest(name, email, password))
                emit(Result.Success(response))
            } catch (e: Exception) {
                Log.d("Signup", e.message.toString())
                emit(Result.Error(e.message.toString()))
            }
        }

    fun addStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody, lat: Double?, lon: Double?
    ): LiveData<Result<AddStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.addStory(token, file, description, lat, lon)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("Signup", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

//    fun getStoryLoc(token: String): LiveData<Result<AllStoriesResponse>> = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiService.getStoriesLocation(token, 1)
//            emit(Result.Success(response))
//        } catch (e: Exception) {
//            Log.d("Signup", e.message.toString())
//            emit(Result.Error(e.message.toString()))
//        }
//    }

    fun getUserData(): LiveData<UserData> {
        return sharedPref.getUser().asLiveData()
    }

    suspend fun saveUserData(userData: UserData) {
        sharedPref.saveUser(userData)
    }

    suspend fun login() {
        sharedPref.login()
    }

    suspend fun logout() {
        sharedPref.logout()
    }
}