package com.omdbapi.app.network.base.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omdbapi.app.R
import com.omdbapi.app.network.base.ApiConstants
import com.omdbapi.app.network.base.model.Error
import com.omdbapi.app.network.base.model.GenericResponse
import com.omdbapi.app.utils.functional.LogUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
@Singleton
class NetworkHelper @Inject constructor(
    @ApplicationContext val context: Context,
) {
    private var networkType = -1

    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            return if (connectivityManager != null) {
                val networkInfo = connectivityManager.activeNetworkInfo
                if (networkInfo != null) {
                    networkType = networkInfo.type
                }
                if (networkInfo == null) {
                    return false
                }
                val network = networkInfo.state
                network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING
            } else {
                false
            }
        }

    fun handleRetrofitHttpException(
        apiCode: Int,
        exception: HttpException
    ): Error {
        return try {
            val response = getResponseModel(exception.response())
            Error(
                code = ApiConstants.ErrorCode.DEFAULT_ERROR,
                message = response?.error,
                apiCode = apiCode
            )
        } catch (e: Exception) {
            LogUtils.e(message = e.localizedMessage)
            Error(code = ApiConstants.ErrorCode.DEFAULT_ERROR)
        }
    }

    fun handleRetrofitIOException(
        apiCode: Int,
        exception: IOException
    ): Error {
        return Error(
            code = ApiConstants.ErrorCode.NO_INTERNET_ERROR,
            message = context.getString(R.string.err_no_internet_network)
        )
    }

    fun handleRetrofitException(
        apiCode: Int,
        exception: Exception
    ): Error {
        return Error(
            code = ApiConstants.ErrorCode.DEFAULT_ERROR,
            message = context.getString(R.string.err_something_went_wrong)
        )
    }

    private fun <T : Any> getResponseModel(response: Response<T>?): GenericResponse<Any>? {
        if (response == null) return null
        return if (response.isSuccessful) {
            null
        } else {
            try {
                val type = object : TypeToken<GenericResponse<Any>?>() {}.type
                val responseError =
                    Gson().fromJson<GenericResponse<Any>>(response.errorBody()?.string(), type)
                responseError
            } catch (e: Exception) {
                null
            }
        }
    }

    fun <T> getErrorModel(errorModel: Any?, errorClass: Class<T>): T? {
        if (errorModel == null) return null
        val gson = Gson()
        return gson.fromJson(gson.toJson(errorModel), errorClass)
    }
}