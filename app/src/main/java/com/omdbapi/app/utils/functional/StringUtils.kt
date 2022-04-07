package com.omdbapi.app.utils.functional

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.RawRes
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.floor
import kotlin.math.log10

object StringUtils {

    @JvmStatic
    fun toFirstCharCapital(value: String?, replaceComma: Boolean = false): String {
        if (value.isNullOrEmpty()) return ""
        val upperString: String =
            value.substring(0, 1).uppercase(Locale.getDefault()) + value.substring(1)
                .lowercase(Locale.getDefault())

        if (replaceComma)
            upperString.replace(",", "")
        return upperString
    }

}
