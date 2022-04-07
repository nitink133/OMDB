package com.omdbapi.app.utils.functional

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.omdbapi.app.constants.LogConstants

/**
 * @author Nitin Khanna
 * @date 16/11/2021
 */
object LogUtils {

    @JvmStatic
    fun e(messagePrefix: String? = null, message: String?) {
        if (message.isNullOrEmpty()) return
        if (!BuildUtils.isDebug()) return
        if (messagePrefix.isNullOrEmpty())
            Log.d(LogConstants.DEBUG_LOG_ERROR, message)
        else Log.d(LogConstants.DEBUG_LOG_ERROR, messagePrefix + message)
    }

    @JvmStatic
    fun d(messagePrefix: String? = null, message: String?) {
        if (message.isNullOrEmpty()) return
        if (!BuildUtils.isDebug()) return
        if (messagePrefix.isNullOrEmpty())
            Log.d(LogConstants.DEBUG_LOG_TAG, message)
        else Log.d(LogConstants.DEBUG_LOG_TAG, messagePrefix + message)
    }

    @JvmStatic
    fun flow(messagePrefix: String? = null, message: String?) {
        if (message.isNullOrEmpty()) return
        if (!BuildUtils.isDebug()) return
        if (messagePrefix.isNullOrEmpty())
            Log.d(LogConstants.DEBUG_FLOW_TAG, message)
        else Log.d(LogConstants.DEBUG_FLOW_TAG, messagePrefix + message)
    }

    @JvmStatic
    fun flow(activity: Activity?, message: String?) {
        if (message.isNullOrEmpty()) return
        if (!BuildUtils.isDebug()) return
        if (activity == null) return
        Log.d(LogConstants.DEBUG_FLOW_TAG, activity::class.java.simpleName + message)
    }

    @JvmStatic
    fun data(messagePrefix: String? = null, message: String?) {
        if (message.isNullOrEmpty()) return
        if (!BuildUtils.isDebug()) return
        if (messagePrefix.isNullOrEmpty())
            Log.d(LogConstants.DEBUG_DATA_TAG, message)
        else Log.d(LogConstants.DEBUG_DATA_TAG, messagePrefix + message)
    }

    @JvmStatic
    fun api(messagePrefix: String? = null, message: String?) {
        if (message.isNullOrEmpty()) return
        if (!BuildUtils.isDebug()) return
        if (messagePrefix.isNullOrEmpty())
            Log.d(LogConstants.DEBUG_API_TAG, message)
        else Log.d(LogConstants.DEBUG_API_TAG, messagePrefix + message)
    }

    @JvmStatic
    fun data(tag: String? = null, bundle: Bundle?) {
        if (bundle == null) return
        if (!BuildUtils.isDebug()) return
        Log.d(LogConstants.DEBUG_DATA_TAG, tag + Gson().toJson(bundle))
    }


    @JvmStatic
    fun <T> data(tag: String? = null, data: T?) {
        if (data == null) return
        if (!BuildUtils.isDebug()) return
        Log.d(LogConstants.DEBUG_DATA_TAG, tag + Gson().toJson(data))
    }

    @JvmStatic
    fun d(messagePrefix: String? = null, message: Any?) {
        if (message == null) return
        if (!BuildUtils.isDebug()) return
        if (messagePrefix.isNullOrEmpty())
            Log.d(LogConstants.DEBUG_LOG_TAG, Gson().toJson(message))
        else Log.d(LogConstants.DEBUG_LOG_TAG, messagePrefix + Gson().toJson(message))
    }

    @JvmStatic
    fun d(messagePrefix: String? = null, intent: Intent?) {
        if (intent == null) return
        if (!BuildUtils.isDebug()) return
        if (messagePrefix.isNullOrEmpty())
            Log.d(LogConstants.DEBUG_LOG_TAG, Gson().toJson(intent.extras))
        else Log.d(LogConstants.DEBUG_LOG_TAG, messagePrefix + Gson().toJson(intent.extras))
    }

    @JvmStatic
    fun d(bundle: Bundle?) {
        if (bundle == null) return
        if (!BuildUtils.isDebug()) return
        Log.d(LogConstants.DEBUG_LOG_TAG, LogConstants.BUNDLE_EXTRAS + Gson().toJson(bundle))
    }

    @JvmStatic
    fun d(activity: Activity?) {
        if (activity == null) return
        if (!BuildUtils.isDebug()) return

        Log.d(
            LogConstants.DEBUG_LOG_TAG,
            LogConstants.ACTIVITY_NAME + activity::class.java.simpleName
        )
        if (activity.intent?.extras != null)
            Log.d(
                LogConstants.DEBUG_LOG_TAG,
                LogConstants.ACTIVITY_EXTRAS + Gson().toJson(activity.intent?.extras)
            )
        else Log.d(LogConstants.DEBUG_LOG_TAG, LogConstants.ACTIVITY_EXTRAS + LogConstants.NULL)
    }

    @JvmStatic
    fun response(messagePrefix: String? = null, response: Any?) {
        if (response == null) return
        if (!BuildUtils.isDebug()) return
        if (messagePrefix.isNullOrEmpty())
            Log.d(LogConstants.DEBUG_API_RESPONSE, Gson().toJson(response))
        else Log.d(LogConstants.DEBUG_API_RESPONSE, messagePrefix + Gson().toJson(response))
    }


}