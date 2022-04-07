package com.omdbapi.app.utils.functional

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.util.*

/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
object SafeArgs {
    @JvmStatic
    fun getString(bundle: Bundle?, key: String, default: String? = ""): String? {
        if (bundle == null || key.isEmpty()) return default
        if (bundle.getString(key) == null) return default
        return bundle.getString(key)!!
    }

    @JvmStatic
    fun getDouble(bundle: Bundle?, key: String): Double {
        if (bundle == null || key.isEmpty()) return 0.0
        if (bundle.getString(key) == null) return 0.0
        return bundle.getDouble(key)
    }

    @JvmStatic
    fun getInt(bundle: Bundle?, key: String): Int {
        if (bundle == null || key.isEmpty()) return 0
        return bundle.getInt(key)
    }

    @JvmStatic
    fun getBoolean(bundle: Bundle?, key: String, default: Boolean = false): Boolean {
        if (bundle == null || key.isEmpty()) return default
        return bundle.getBoolean(key, default)
    }

    @JvmStatic
    fun getString(intent: Intent?, key: String): String {
        if (intent == null || key.isEmpty()) return ""
        return try {
            val value = intent.getStringExtra(key)
            value ?: ""
        } catch (e: Exception) {
            LogUtils.e(message = e.localizedMessage)
            ""
        }
    }

    @JvmStatic
    fun getInt(intent: Intent?, key: String): Int {
        if (intent == null || key.isEmpty()) return 0
        return try {
            intent.getIntExtra(key, 0)
        } catch (e: Exception) {
            LogUtils.e(message = e.localizedMessage)
            0
        }
    }

    @JvmStatic
    fun getBoolean(intent: Intent?, key: String): Boolean {
        if (intent == null || key.isEmpty()) return false
        return intent.getBooleanExtra(key, false)
    }

    @JvmStatic
    fun getBundleBoolean(intent: Intent?, key: String): Boolean {
        if (intent == null || key.isEmpty()) return false
        if (intent.extras == null) return false
        return intent.extras!!.getBoolean(key, false)
    }

    @JvmStatic
    fun <T : Parcelable> getParcelableList(bundle: Bundle?, key: String): List<T>? {
        if (bundle == null || key.isEmpty()) return null
        return bundle.getParcelableArrayList(key)
    }

    @JvmStatic
    fun getStringList(bundle: Bundle?, key: String): List<String>? {
        if (bundle == null || key.isEmpty()) return null
        return bundle.getStringArrayList(key)
    }

    @JvmStatic
    fun <T : Parcelable> getParcelable(bundle: Bundle?, key: String): T? {
        if (bundle == null || key.isEmpty()) return null
        if (bundle.getParcelable<T>(key) == null) return null
        return bundle.getParcelable(key)!!
    }

    @JvmStatic
    fun bundleToMap(extras: Bundle): Map<String, Any?> {
        val map: MutableMap<String, Any?> = HashMap()
        val ks = extras.keySet()
        val iterator: Iterator<String> = ks.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            map[key] = extras[key]
        }
        return map
    }

}