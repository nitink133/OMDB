package com.omdbapi.app.ui.movie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omdbapi.app.network.base.model.Response
import com.omdbapi.app.network.movie.model.response.MovieDetail
import com.omdbapi.app.repository.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    val responseStateForMovieDetails: MutableStateFlow<Response<MovieDetail>> =
        MutableStateFlow(Response.Initial)

    fun getMovieDetail(
        id: String? = null,
    ) {
        if (id.isNullOrEmpty()) return
        viewModelScope.launch {
            responseStateForMovieDetails.value = Response.Loading
            responseStateForMovieDetails.value = movieRepository.getMovieDetails(id)
        }
    }
}