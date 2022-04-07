package com.omdbapi.app.network.movie

import com.omdbapi.app.network.base.model.GenericResponse
import com.omdbapi.app.network.movie.model.response.MovieDetail
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
interface MovieApiInterface {
    @GET(".")
    suspend fun searchMovie(
        @Query("apikey") apikey: String,
        @Query("page") page: Int,
        @Query("s") query: String?,
    ): GenericResponse<List<MovieDetail>>

    @GET(".")
    suspend fun getMovieDetail(
        @Query("apikey") apikey: String,
        @Query("t") id: String?,
    ): MovieDetail

}