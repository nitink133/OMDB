package com.omdbapi.app.ui.movie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.omdbapi.app.network.base.model.Response
import com.omdbapi.app.network.movie.model.response.MovieDetail
import com.omdbapi.app.repository.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    val responseStateForAllMovies: MutableStateFlow<Response<PagingData<MovieDetail>>> =
        MutableStateFlow(Response.Initial)

    var bookingsListJob: Job? = null
    fun searchMovie(
        query: String? = null,
    ) {
        if (query.isNullOrEmpty()) return
        bookingsListJob?.cancel()
        bookingsListJob = viewModelScope.launch {
            responseStateForAllMovies.value = Response.Loading
            movieRepository.searchMovie(query).cachedIn(viewModelScope).collect {
                responseStateForAllMovies.value = Response.Success(it)
            }
        }
    }
}