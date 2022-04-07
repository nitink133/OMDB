package com.omdbapi.app.utils.app_specific

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.FragmentManager
import com.omdbapi.app.base.dialog.DialogDefaultError


object DialogUtils {
    private var errorDialogDefaultError: DialogDefaultError? = null

    @JvmStatic
    fun showErrorDialog(
        fragmentManager: FragmentManager?,
        bundle: Bundle? = null,
        callBack: (() -> Unit)?
    ) {
        if (fragmentManager == null) return
        //TODO: Replace this with navigation component
        try {
            errorDialogDefaultError?.dismiss()

            errorDialogDefaultError = DialogDefaultError.newInstance(bundle)
            errorDialogDefaultError?.isCancelable = false
            errorDialogDefaultError?.show(fragmentManager, errorDialogDefaultError!!.tag)
            errorDialogDefaultError?.callBack = {
                errorDialogDefaultError?.dismiss()
                errorDialogDefaultError = null
                callBack?.invoke()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO: Need to track this
        }
    }

}