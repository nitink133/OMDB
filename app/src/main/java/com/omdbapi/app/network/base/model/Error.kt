package com.omdbapi.app.network.base.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Error(
    @SerializedName("code")
    var code: String? = null,
    @SerializedName("message")
    var message: String? = null,
    var data: Any? = null,
    var request: Any? = null,
    var apiCode: Int? = null
)
