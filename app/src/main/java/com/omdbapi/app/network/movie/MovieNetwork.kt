package com.omdbapi.app.network.movie

import android.content.Context
import com.omdbapi.app.R
import com.omdbapi.app.di.coroutine.IoDispatcher
import com.omdbapi.app.di.coroutine.MainDispatcher
import com.omdbapi.app.network.base.ApiConstants
import com.omdbapi.app.network.base.helper.NetworkHelper
import com.omdbapi.app.network.base.model.Error
import com.omdbapi.app.network.base.model.Response
import com.omdbapi.app.network.movie.model.response.MovieDetail
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
@ViewModelScoped
class MovieNetwork @Inject constructor(
    @ApplicationContext val context: Context,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val networkHelper: NetworkHelper,
    private val apiInterface: MovieApiInterface
) {

    suspend fun searchMovie(
        position: Int,
        query: String
    ): Response<List<MovieDetail>> {
        if (!networkHelper.isNetworkAvailable) {
            return withContext(mainDispatcher) {
                delay(100)
                Response.Error(
                    error = Error(
                        code = ApiConstants.ErrorCode.NO_INTERNET_ERROR,
                        message = context.getString(R.string.err_no_internet_network)
                    )
                )
            }
        }

        return withContext(ioDispatcher) {
            try {
                val response =
                    apiInterface.searchMovie(
                        ApiConstants.API_KEY,
                        position,
                        query
                    )
                if (response.search.isNullOrEmpty()) Response.IsEmpty
                else Response.Success(response.search!!)
            } catch (exception: IOException) {
                Response.Error(
                    error = networkHelper.handleRetrofitIOException(
                        apiCode = ApiConstants.ApiCode.SEARCH,
                        exception = exception
                    )
                )
            } catch (exception: HttpException) {
                Response.Error(
                    error = networkHelper.handleRetrofitHttpException(
                        apiCode = ApiConstants.ApiCode.SEARCH,
                        exception = exception
                    )
                )
            } catch (exception: Exception) {
                Response.Error(
                    error = networkHelper.handleRetrofitException(
                        apiCode = ApiConstants.ApiCode.SEARCH,
                        exception = exception
                    )
                )
            }
        }
    }

    suspend fun getMovieDetails(
        id: String
    ): Response<MovieDetail> {
        if (!networkHelper.isNetworkAvailable) {
            return withContext(mainDispatcher) {
                delay(100)
                Response.Error(
                    error = Error(
                        code = ApiConstants.ErrorCode.NO_INTERNET_ERROR,
                        message = context.getString(R.string.err_no_internet_network)
                    )
                )
            }
        }

        return withContext(ioDispatcher) {
            try {
                val response =
                    apiInterface.getMovieDetail(
                        ApiConstants.API_KEY,
                        id
                    )
                Response.Success(response)
            } catch (exception: IOException) {
                Response.Error(
                    error = networkHelper.handleRetrofitIOException(
                        apiCode = ApiConstants.ApiCode.MOVIE_DETAIL,
                        exception = exception
                    )
                )
            } catch (exception: HttpException) {
                Response.Error(
                    error = networkHelper.handleRetrofitHttpException(
                        apiCode = ApiConstants.ApiCode.MOVIE_DETAIL,
                        exception = exception
                    )
                )
            } catch (exception: Exception) {
                Response.Error(
                    error = networkHelper.handleRetrofitException(
                        apiCode = ApiConstants.ApiCode.MOVIE_DETAIL,
                        exception = exception
                    )
                )
            }
        }
    }
}
