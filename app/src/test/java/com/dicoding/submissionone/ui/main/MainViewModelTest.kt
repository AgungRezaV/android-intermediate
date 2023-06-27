package com.dicoding.submissionone.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.submissionone.data.DummyData
import com.dicoding.submissionone.ui.story.ListStory
import com.dicoding.submissionone.ui.story.StoryAdaptor
import com.dicoding.submissionone.utils.MainDispatcherRule
import com.dicoding.submissionone.utils.StoryRepository
import com.dicoding.submissionone.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MainActivityViewModel

    @Mock
    private lateinit var repository: StoryRepository

    @Before
    fun setUp() {
        viewModel = MainActivityViewModel(repository)
    }

    @Test
    fun whenListStoryResponseSuccess() = runTest {
        val dummyStories = DummyData.generateDummyListStoryResponse()
        val data: PagingData<ListStory> = PagingSourceTest.snapshot(dummyStories.listStory)

        val expectedStory = MutableLiveData<PagingData<ListStory>>()
        expectedStory.value = data
        Mockito.`when`(repository.getStory()).thenReturn(expectedStory)

        val actualStory: PagingData<ListStory> = viewModel.getStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdaptor.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        //Data Not Null
        Assert.assertNotNull(differ.snapshot())

        //Data matches
        Assert.assertEquals(dummyStories.listStory, differ.snapshot())
        Assert.assertEquals(dummyStories.listStory.size, differ.snapshot().size)

        //First Data Matches
        Assert.assertEquals(dummyStories.listStory[0], differ.snapshot()[0])
    }

    @Test
    fun whenListStoryResponseNoStory () = runTest {
        val data: PagingData<ListStory> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<ListStory>>()

        expectedStory.value = data
        Mockito.`when`(repository.getStory()).thenReturn(expectedStory)

        val actualStory: PagingData<ListStory> = viewModel.getStory().getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdaptor.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        differ.submitData(actualStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

private val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class PagingSourceTest : PagingSource<Int, LiveData<List<ListStory>>>() {
    companion object {
        fun snapshot(items: List<ListStory>): PagingData<ListStory> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStory>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStory>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}