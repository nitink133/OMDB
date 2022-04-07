package com.omdbapi.app.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omdbapi.app.R
import com.omdbapi.app.databinding.ItemRetryPageLoadingBinding

/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
class PagingLoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PagingLoadingStateAdapter.PagingLoadingStateViewHolder>() {
    override fun onBindViewHolder(holder: PagingLoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoadingStateViewHolder {
        return PagingLoadingStateViewHolder.create(parent, retry)
    }

    class PagingLoadingStateViewHolder(
        private val binding: ItemRetryPageLoadingBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.tvError.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.btnRetry.isVisible = loadState !is LoadState.Loading
            binding.tvError.isVisible = loadState !is LoadState.Loading
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): PagingLoadingStateViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_retry_page_loading, parent, false)
                val binding = ItemRetryPageLoadingBinding.bind(view)
                return PagingLoadingStateViewHolder(binding, retry)
            }
        }
    }
}