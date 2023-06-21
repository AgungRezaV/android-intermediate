package com.dicoding.submissionone.ui.story

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.submissionone.data.retrofit.ApiService
import com.dicoding.submissionone.utils.UserPreference
import kotlinx.coroutines.flow.first

class PagingSource(private val apiService: ApiService, private val userPreference: UserPreference) : PagingSource<Int, ListStory>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStory> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = "Bearer ${userPreference.getUser().first().token}"
            val responseData = apiService.allStories(token, page, params.loadSize).listStory

            LoadResult.Page(
                data = responseData,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

     override fun getRefreshKey(state: PagingState<Int, ListStory>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}