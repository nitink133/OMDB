package com.omdbapi.app.utils.functional

import android.content.Context
import android.widget.Toast


object ToastUtils {

    fun showMessage(context: Context?, message: String?, duration: Int = Toast.LENGTH_SHORT) {
        if (context == null || message.isNullOrEmpty()) return
        Toast.makeText(context, message, duration).show()
    }
}