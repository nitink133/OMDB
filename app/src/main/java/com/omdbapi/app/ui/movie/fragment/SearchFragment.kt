package com.omdbapi.app.ui.movie.fragment

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.omdbapi.app.R
import com.omdbapi.app.base.activity.BaseActivity
import com.omdbapi.app.base.adapter.PagingLoadingStateAdapter
import com.omdbapi.app.base.fragment.BaseFragment
import com.omdbapi.app.constants.BundleConstants
import com.omdbapi.app.databinding.FragmentSearchBinding
import com.omdbapi.app.network.base.ApiConstants
import com.omdbapi.app.network.base.exception.NetworkLayerException
import com.omdbapi.app.network.base.model.Error
import com.omdbapi.app.network.base.model.Response
import com.omdbapi.app.network.movie.model.response.MovieDetail
import com.omdbapi.app.ui.movie.adapter.MovieListAdapter
import com.omdbapi.app.ui.movie.viewmodel.SearchViewModel
import com.omdbapi.app.utils.app_specific.DialogUtils
import com.omdbapi.app.utils.functional.AnimationUtils
import com.omdbapi.app.widget.VerticalSpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch


/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
@AndroidEntryPoint
class SearchFragment : BaseFragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    private var searchView: EditText? = null

    private val mAdapter: MovieListAdapter by lazy {
        MovieListAdapter(
            mAdapterSelectionCallback
        )
    }

    private val mAdapterSelectionCallback: ((imageView: View, item: MovieDetail, transitionId: String) -> Unit) =
        let@{ imageView, movieDetail, transitionId ->
            val direction = SearchFragmentDirections.moveToDetails(
                movieDetail.title!!,
                transitionId
            )
            val extras = FragmentNavigatorExtras(
                imageView to transitionId
            )
            findNavController().navigate(direction, extras)
        }

    override fun showProgress(msg: String?) {
        AnimationUtils.showView(binding.layoutProgressBar.progressBar)
        AnimationUtils.hideView(binding.recyclerView)
        AnimationUtils.hideView(binding.tvNoData)
    }

    override fun hideProgress() {
        AnimationUtils.hideView(binding.layoutProgressBar.progressBar)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        (activity as BaseActivity?)?.setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            mAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    binding.recyclerView.scrollToPosition(0)

                    val refreshState = loadState.refresh
                    if (refreshState is LoadState.Error)
                        viewModel.responseStateForAllMovies.value = Response.Error(
                            (refreshState.error as NetworkLayerException).errorModel ?: Error(
                                code = ApiConstants.ErrorCode.DEFAULT_ERROR
                            )
                        )
                    else if (refreshState is LoadState.NotLoading && mAdapter.itemCount == 0) {
                        viewModel.responseStateForAllMovies.value = Response.IsEmpty
                    } else if (refreshState is LoadState.NotLoading) {
                        if (mAdapter.itemCount == 0) {
                            AnimationUtils.showView(binding.tvNoData)
                            AnimationUtils.hideView(binding.recyclerView)
                        } else {
                            AnimationUtils.showView(binding.recyclerView)
                            AnimationUtils.hideView(binding.tvNoData)
                        }
                        viewModel.responseStateForAllMovies.value = Response.Initial
                    } else if (refreshState is LoadState.Loading) {
                        viewModel.responseStateForAllMovies.value = Response.Loading
                    }
                }
        }


    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.responseStateForAllMovies.collect { uiState ->
                    when (uiState) {
                        is Response.Initial -> {
                            hideProgress()
                        }
                        is Response.Loading -> {
                            showProgress("")
                        }
                        is Response.Success -> {
                            AnimationUtils.showView(binding.recyclerView)
                            AnimationUtils.hideView(binding.tvNoData)
                            viewLifecycleOwner.lifecycleScope.launch {
                                mAdapter.submitData(uiState.data)
                            }
                        }
                        is Response.IsEmpty -> {
                            viewLifecycleOwner.lifecycleScope.launch {
                                mAdapter.submitData(PagingData.empty())
                            }
                            AnimationUtils.showView(binding.tvNoData)
                            AnimationUtils.hideView(binding.recyclerView)
                            hideProgress()
                        }
                        is Response.Error -> {
                            showError(uiState.error)
                            viewModel.responseStateForAllMovies.value = Response.Initial
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

    private fun initUi() {
        binding.toolbar.apply {
            title = getString(R.string.app_name)
        }

        val mLayoutManger = LinearLayoutManager(context)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            layoutManager = mLayoutManger
            addItemDecoration(VerticalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_small)))
        }
        mAdapter.addLoadStateListener {
            val concatAdapter =
                mAdapter.withLoadStateFooter(footer = PagingLoadingStateAdapter { mAdapter.retry() })
            binding.recyclerView.adapter = concatAdapter
        }
        binding.recyclerView.adapter = mAdapter

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.menu_search)
        val searchView = searchViewItem.actionView as SearchView
        val searchBar =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_bar) as LinearLayout
        searchView.maxWidth = Integer.MAX_VALUE
        val searchEditText =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
        searchEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        searchView.queryHint = getString(R.string.lbl_search)
        searchEditText.setHintTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.extra_hint_text_color
            )
        )
        searchBar.layoutTransition = LayoutTransition()
        this.searchView = searchEditText
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                if (query.isNullOrEmpty()) {
                    viewModel.responseStateForAllMovies.value = Response.IsEmpty
                } else if (query.length > 2)
                    viewModel.searchMovie(
                        query = query
                    )
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.responseStateForAllMovies.value = Response.IsEmpty
                } else if (newText.length > 2)
                    viewModel.searchMovie(
                        query = newText
                    )
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }
}