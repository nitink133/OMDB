package com.omdbapi.app.network.movie.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omdbapi.app.network.base.exception.NetworkLayerException
import com.omdbapi.app.network.base.model.Response
import com.omdbapi.app.network.movie.MovieNetwork
import com.omdbapi.app.network.movie.model.response.MovieDetail
import com.omdbapi.app.utils.functional.LogUtils
import javax.inject.Inject

/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
class MoviesPageSource @Inject constructor(private val movieNetwork: MovieNetwork) :
    PagingSource<Int, MovieDetail>() {
    var searchQuery: String? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetail> {
        val position = params.key ?: 1
        val response = movieNetwork.searchMovie(
            position,
            searchQuery!!
        )

        return when (response) {
            is Response.Success -> {
                LogUtils.data(
                    "Nitin",
                    (if (searchQuery?.length ?: 0 < 3) null else if (response.data.isNullOrEmpty()) null else position + 1).toString()
                )
                LoadResult.Page(
                    data = response.data,
                    prevKey = if (searchQuery?.length ?: 0 <= 3) null else if (position == 1) null else position,
                    nextKey = if (searchQuery?.length ?: 0 <= 3) null else if (response.data.isNullOrEmpty()) null else position + 1
                )
            }
            is Response.Error -> {
                LoadResult.Error(
                    NetworkLayerException(
                        message = response.error.message,
                        errorModel = response.error
                    )
                )
            }
            else -> LoadResult.Page(
                data = arrayListOf(),
                prevKey = if (position == 1) null else position,
                nextKey = null
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDetail>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
