package com.omdbapi.app.base.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.omdbapi.app.utils.functional.ToastUtils
import com.omdbapi.app.network.base.helper.NetworkHelper
import coil.load
import com.omdbapi.app.R
import com.omdbapi.app.constants.BundleConstants
import com.omdbapi.app.databinding.DialogErrorBinding
import com.omdbapi.app.network.base.ApiConstants
import com.omdbapi.app.utils.functional.SafeArgs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
@AndroidEntryPoint
class DialogDefaultError : DialogFragment() {
    private lateinit var binding: DialogErrorBinding
    var callBack: (() -> Unit)? = null

    @Inject
    lateinit var networkHelper: NetworkHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen_SlideBottomToTop)
    }

    override fun onStart() {
        super.onStart()
        val d: Dialog? = dialog
        if (d != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            d.window?.setLayout(width, height)
            d.window?.setWindowAnimations(R.style.Dialog_FullScreen_SlideBottomToTop)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogErrorBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!networkHelper.isNetworkAvailable)
            arguments?.putString(
                BundleConstants.ERROR_CODE,
                ApiConstants.ErrorCode.NO_INTERNET_ERROR
            )

        binding.tvError.text =
            if (!SafeArgs.getString(arguments, BundleConstants.ERROR_MESSAGE)
                    .isNullOrEmpty()
            )
                SafeArgs.getString(arguments, BundleConstants.ERROR_MESSAGE)
            else getString(R.string.err_something_went_wrong)
        binding.btnGoBack.text = getString(R.string.cta_go_back)
        binding.ivErrorImage.load(R.drawable.ic_error_500)

        binding.btnGoBack.setOnClickListener {
            if (!networkHelper.isNetworkAvailable) {
                ToastUtils.showMessage(context, getString(R.string.err_no_internet_network))
                return@setOnClickListener
            }
            callBack?.invoke()
            dismiss()
        }
    }

    companion object {

        fun newInstance(bundle: Bundle? = null): DialogDefaultError {
            return DialogDefaultError().apply {
                arguments = bundle ?: Bundle()
            }
        }
    }

}