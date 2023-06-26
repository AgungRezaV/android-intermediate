package com.dicoding.submissionone.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.submissionone.data.DummyData
import com.dicoding.submissionone.data.response.AddStoryResponse
import com.dicoding.submissionone.data.response.UserData
import com.dicoding.submissionone.ui.story.add.AddStoryViewModel
import com.dicoding.submissionone.utils.StoryRepository
import com.dicoding.submissionone.utils.Result
import com.dicoding.submissionone.utils.getOrAwaitValue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class AddStoryViewModelTest{
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddStoryViewModel

    @Mock
    private var repository = mock(StoryRepository::class.java)
    private val dummy= DummyData.generateDummyAddNewStoryResponse()
    private val dummyUser = DummyData.generateDummyGetUser()

    @Before
    fun setUp() {
        viewModel = AddStoryViewModel(repository)
    }

    @Test
    fun whenUploadReturnSuccess() {
        val desc = "ini desc".toRequestBody("text/plain".toMediaType())
        val file = mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val fileImg: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "nameFile",
            requestImageFile
        )

        val expectedStory = MutableLiveData<Result<AddStoryResponse>>()
        expectedStory.value = Result.Success(dummy)
        Mockito.`when`(repository.addStory(token, fileImg, desc, lat.toFloat(), long.toFloat())).thenReturn(expectedStory)

        val actualStory = viewModel.addStory(token, fileImg, desc, lat.toFloat(), long.toFloat()).getOrAwaitValue()
        Mockito.verify(repository).addStory(token, fileImg, desc, lat.toFloat(), long.toFloat())
        assertNotNull(actualStory)

        assertTrue(actualStory is Result.Success)
    }

    @Test
    fun whenUploadReturnError() {
        val desc = "ini desc".toRequestBody("text/plain".toMediaType())
        val file = mock(File::class.java)
        val requestImageFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val fileImg: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "nameFile",
            requestImageFile
        )

        val expectedStory = MutableLiveData<Result<AddStoryResponse>>()
        expectedStory.value = Result.Error("Error")
        Mockito.`when`(repository.addStory(token, fileImg, desc, lat.toFloat(), long.toFloat())).thenReturn(expectedStory)

        val actualStory = viewModel.addStory(token, fileImg, desc, lat.toFloat(), long.toFloat()).getOrAwaitValue()
        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Error)

        Mockito.verify(repository).addStory(token, fileImg, desc, lat.toFloat(), long.toFloat())
    }

    @Test
    fun whenGetUserReturnSuccess() {
        val expectedGetUser = MutableLiveData<UserData>()
        expectedGetUser.value = dummyUser
        Mockito.`when`(repository.getUserData()).thenReturn(expectedGetUser)

        val actualUser = viewModel.getUser().getOrAwaitValue()
        assertNotNull(actualUser)
        assertEquals(dummyUser, actualUser)

        Mockito.verify(repository).getUserData()
    }

    companion object {
        private const val lat = 2.69
        private const val long = 2.69
        private const val token = "Bearer token"
    }
}