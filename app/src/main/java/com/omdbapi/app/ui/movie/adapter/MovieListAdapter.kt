package com.omdbapi.app.ui.movie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.omdbapi.app.databinding.ItemMovieDetailsBinding
import com.omdbapi.app.network.movie.model.response.MovieDetail
import com.omdbapi.app.utils.functional.AnimationUtils
import com.omdbapi.app.utils.functional.StringUtils


/**
 * @author Nitin Khanna
 * @date 07/04/2022
 */
class MovieListAdapter(
    private val selectionCallbacks: ((imageView: View, item: MovieDetail, transitionId: String) -> Unit)?
) : PagingDataAdapter<MovieDetail, MovieListAdapter.Holder>(MOVIE_COMPARATOR) {

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<MovieDetail>() {
            override fun areItemsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean =
                oldItem.imdbID == newItem.imdbID

            override fun areContentsTheSame(oldItem: MovieDetail, newItem: MovieDetail): Boolean =
                oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListAdapter.Holder {
        val binding =
            ItemMovieDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(viewHolder: MovieListAdapter.Holder, position: Int) {
        val context = viewHolder.binding.root.context
        var isImageLoaded = false
        getItem(position)?.let { item ->
            with(viewHolder.binding) {
                AnimationUtils.showView(progressBar)

                ivLogo.load(item.poster) {
                    listener(
                        onError = { _, _ ->
                            AnimationUtils.hideView(viewHolder.binding.progressBar)
                        },
                        onSuccess = { _, _ ->
                            isImageLoaded = true
                            ViewCompat.setTransitionName(ivLogo, "ivLogoTransition_${position}_${item.title}")
                            AnimationUtils.hideView(viewHolder.binding.progressBar)
                        }
                    )
                }
                ivLogo.scaleType = ImageView.ScaleType.CENTER_CROP

                tvName.text = if (item.title.isNullOrEmpty()) "NULL" else item.title
                tvReleased.text = if (item.year.isNullOrEmpty()) "NULL" else item.year
                tvImdbId.text = if (item.imdbID.isNullOrEmpty()) "NULL" else item.imdbID
                tvType.text =
                    if (item.type.isNullOrEmpty()) "NULL" else StringUtils.toFirstCharCapital(item.type)

                root.setOnClickListener {
                    if(!isImageLoaded)return@setOnClickListener
                    selectionCallbacks?.invoke(ivLogo, item, "ivLogoTransition_${position}_${item.title}")
                }
            }
        }
    }

    inner class Holder(var binding: ItemMovieDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)


}