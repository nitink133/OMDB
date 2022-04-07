package com.omdbapi.app.base.fragment

import androidx.fragment.app.Fragment

/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
abstract class BaseFragment : Fragment() {
    abstract fun showProgress(msg: String?)
    abstract fun hideProgress()
    open fun onBackPressed(): Boolean {
        return true
    }
}