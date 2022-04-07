package com.omdbapi.app.ui.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.transition.TransitionInflater
import coil.load
import com.omdbapi.app.base.fragment.BaseFragment
import com.omdbapi.app.constants.BundleConstants
import com.omdbapi.app.databinding.FragmentDetailBinding
import com.omdbapi.app.network.base.model.Error
import com.omdbapi.app.network.base.model.Response
import com.omdbapi.app.ui.movie.viewmodel.DetailsViewModel
import com.omdbapi.app.utils.app_specific.DialogUtils
import com.omdbapi.app.utils.functional.AnimationUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
@AndroidEntryPoint
class DetailFragment : BaseFragment() {
    private lateinit var binding: FragmentDetailBinding
    private val viewModel by viewModels<DetailsViewModel>()
    private val args by navArgs<DetailFragmentArgs>()

    override fun showProgress(msg: String?) {
       AnimationUtils.showView(binding.layoutProgressBar.root)
       AnimationUtils.hideView(binding.viewDetail)
    }

    override fun hideProgress() {
        AnimationUtils.hideView(binding.layoutProgressBar.root)
        AnimationUtils.showView(binding.viewDetail)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = context?.let { TransitionInflater.from(it).inflateTransition(android.R.transition.move) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.ivLogo, args.transitionId)
        viewModel.getMovieDetail(args.id)
        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.responseStateForMovieDetails.collect { uiState ->
                    when (uiState) {
                        is Response.Initial -> {
                            hideProgress()
                        }
                        is Response.Loading -> {
                            showProgress("")
                        }
                        is Response.Success -> {
                            binding.tvName.text = uiState.data.title
                            binding.tvPlot.text = uiState.data.plot
                            binding.tvRating.text = uiState.data.rated
                            binding.tvGenre.text = uiState.data.genre
                            binding.tvLanguage.text = uiState.data.language
                            binding.ivLogo.load(uiState.data.poster)
                            hideProgress()
                        }
                        is Response.IsEmpty -> {
                            binding.tvName.text = "NA"
                            binding.tvPlot.text = "NA"
                            binding.tvRating.text = "NA"
                            binding.tvGenre.text = "NA"
                            binding.tvLanguage.text = "NA"
                            hideProgress()
                        }
                        is Response.Error -> {
                            showError(uiState.error)
                            viewModel.responseStateForMovieDetails.value = Response.Initial
                        }
                    }
                }
            }
        }
    }


    private fun showError(error: Error) {
        DialogUtils.showErrorDialog(childFragmentManager, Bundle().apply {
            putString(BundleConstants.ERROR_CODE, error.code)
            putString(BundleConstants.ERROR_MESSAGE, error.message)
        }, null)
    }

}