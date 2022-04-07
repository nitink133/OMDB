package com.omdbapi.app.network.base

/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
object ApiConstants {
    const val BASE_STAGE_URL = "https://www.omdbapi.com/"
    const val API_KEY = "5d81e1ce"
    object ErrorCode {
        const val DEFAULT_ERROR = "default_error"
        const val NO_INTERNET_ERROR = "no_network"

        fun isGenericError(errorCode: String?): Boolean {
            if (errorCode.isNullOrEmpty()) return false
            return errorCode == NO_INTERNET_ERROR || errorCode == DEFAULT_ERROR
        }
    }

    object ApiCode {
        const val SEARCH = 0
        const val MOVIE_DETAIL = 1
    }

}