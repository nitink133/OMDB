package com.omdbapi.app.utils.functional

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation


/**
 * @author Nitin Khanna
 * @created 18/11/2021
 */
object AnimationUtils {
    fun hideView(view: View?) {
        if (view?.visibility == View.VISIBLE) {
            val animation: Animation = AlphaAnimation(1.0f, 0.0f)
            animation.duration = 300
            view.startAnimation(animation)
            view.visibility = View.GONE
        }
    }

    fun hideView(vararg views: View?) {
        for (view in views) {
            if (view?.visibility == View.VISIBLE) {
                val animation: Animation = AlphaAnimation(1.0f, 0.0f)
                animation.duration = 300
                view.startAnimation(animation)
                view.visibility = View.GONE
            }
        }
    }

    fun showView(vararg views: View?) {
        for (view in views) {
            if (view?.visibility != View.VISIBLE) {
                val animation: Animation = AlphaAnimation(0.0f, 1.0f)
                animation.duration = 300
                view!!.startAnimation(animation)
                view.visibility = View.VISIBLE
            }
        }
    }

    fun showView(view: View?) {
        if (view?.visibility != View.VISIBLE) {
            val animation: Animation = AlphaAnimation(0.0f, 1.0f)
            animation.duration = 300
            view!!.startAnimation(animation)
            view.visibility = View.VISIBLE
        }
    }
}