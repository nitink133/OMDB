package com.omdbapi.app.utils.functional

import com.omdbapi.app.BuildConfig


/**
 * @author Nitin Khanna
 * @date 16/11/2021
 */
object BuildUtils {
    @JvmStatic
    fun isDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}