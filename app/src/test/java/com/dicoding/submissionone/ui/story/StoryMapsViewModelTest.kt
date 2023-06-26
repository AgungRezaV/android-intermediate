package com.dicoding.submissionone.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.submissionone.data.DummyData
import com.dicoding.submissionone.ui.map.MapViewModel
import com.dicoding.submissionone.utils.StoryRepository
import com.dicoding.submissionone.utils.Result
import com.dicoding.submissionone.utils.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryMapsViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MapViewModel

    @Mock
    private var repo = mock(StoryRepository::class.java)
    private val dummy = DummyData.generateDummyStoryWithMapsResponse()

    @Before
    fun setUp() {
        viewModel = MapViewModel(repo)
    }

    @Test
    fun whenGetStoryWithMapsReturnSuccess() {
        val expectedStory = MutableLiveData<Result<AllStoriesResponse>>()
        expectedStory.value = Result.Success(dummy)
        `when`(repo.getStoryLoc(token)).thenReturn(expectedStory)

        val actualStory = viewModel.getMapLocation(token).getOrAwaitValue()
        Mockito.verify(repo).getStoryLoc(token)
        assertNotNull(actualStory)

        assertTrue(actualStory is Result.Success)
    }

    @Test
    fun whenGetStoryReturnError() {
        val expectedStory = MutableLiveData<Result<AllStoriesResponse>>()
        expectedStory.value = Result.Error("Error")
        `when`(repo.getStoryLoc(token)).thenReturn(expectedStory)

        val actualStory = viewModel.getMapLocation(token).getOrAwaitValue()
        assertNotNull(actualStory)
        assertTrue(actualStory is Result.Error)

        Mockito.verify(repo).getStoryLoc(token)
    }

    companion object {
        private const val token = "Bearer token"
    }
}
