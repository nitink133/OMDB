package com.omdbapi.app.network.base.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class GenericResponse<T> {
    @SerializedName("Response")
    var response: String? = null

    @SerializedName("Error")
    var error: String? = null

    @SerializedName("Search")
    var search: T? = null

    @SerializedName("totalResults")
    var totalResults: String? = null
}