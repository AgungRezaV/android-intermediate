package com.dicoding.submissionone.data.retrofit

import com.dicoding.submissionone.data.request.LoginRequest
import com.dicoding.submissionone.data.request.RegisterRequest
import com.dicoding.submissionone.data.response.AddStoryResponse
import com.dicoding.submissionone.data.response.LoginResponse
import com.dicoding.submissionone.data.response.RegisterResponse
import com.dicoding.submissionone.ui.story.AllStoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("stories")
    suspend fun allStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): AllStoriesResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float
    ): AddStoryResponse

    @GET("stories")
    suspend fun getMap(
        @Header("Authorization") auth: String,
        @Query("location") location : Int = 1,
    ): AllStoriesResponse
}