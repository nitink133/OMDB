package com.omdbapi.app.repository.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omdbapi.app.network.base.model.Response
import com.omdbapi.app.network.movie.MovieNetwork
import com.omdbapi.app.network.movie.model.response.MovieDetail
import com.omdbapi.app.network.movie.pagination.MoviesPageSource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
@ViewModelScoped
class MovieRepository @Inject constructor(
    private val movieNetwork: MovieNetwork,
    private val moviesPageSource: MoviesPageSource
) {

    fun searchMovie(query: String): Flow<PagingData<MovieDetail>> {
        moviesPageSource.apply {
            this.searchQuery = query
        }

        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { moviesPageSource }
        ).flow
    }

    suspend fun getMovieDetails(id: String): Response<MovieDetail> {
        return movieNetwork.getMovieDetails(id)
    }
}