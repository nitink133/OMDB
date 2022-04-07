package com.omdbapi.app.ui.movie

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.omdbapi.app.utils.functional.ToastUtils
import com.omdbapi.app.R
import com.omdbapi.app.base.activity.BaseActivity
import com.omdbapi.app.base.fragment.BaseFragment
import com.omdbapi.app.databinding.ActivityMainBinding
import com.omdbapi.app.ui.movie.fragment.SearchFragment
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onBackPressed() {
        val fragment = getVisibleFragment()
        val doBack = fragment?.onBackPressed() ?: false
        if (!doBack) return
        if (supportFragmentManager.backStackEntryCount > 0 || fragment !is SearchFragment) {
            super.onBackPressed()
        } else if (!doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = true
            ToastUtils.showMessage(this, getString(R.string.msg_please_press_again_to_exit))
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            super.onBackPressed()
            return
        }
    }


    private fun getVisibleFragment(): BaseFragment? {
        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        if (navHostFragment?.childFragmentManager?.fragments.isNullOrEmpty()) return null
        return navHostFragment?.childFragmentManager?.fragments!![0] as? BaseFragment
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}