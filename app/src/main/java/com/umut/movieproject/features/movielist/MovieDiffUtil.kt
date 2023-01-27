package com.umut.movieproject.features.movielist

import androidx.recyclerview.widget.DiffUtil
import com.umut.movieproject.Movie

class MovieDiffUtil(
    private val oldList: MutableList<Movie?>?,
    private val newList: MutableList<Movie?>?
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList?.size ?: 0

    override fun getNewListSize() = newList?.size ?: 0

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList?.get(oldItemPosition)?.movieID == newList?.get(newItemPosition)?.movieID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            /*oldList?.get(oldItemPosition)?.adult != newList?.get(newItemPosition)?.adult -> {
                false
            }
            oldList?.get(oldItemPosition)?.movieID != newList?.get(newItemPosition)?.movieID -> {
                false
            }
            oldList?.get(oldItemPosition)?.overview != newList?.get(newItemPosition)?.overview -> {
                false
            }
            oldList?.get(oldItemPosition)?.posterPath != newList?.get(newItemPosition)?.posterPath -> {
                false
            }
            oldList?.get(oldItemPosition)?.releaseDate != newList?.get(newItemPosition)?.releaseDate -> {
                false
            }
            oldList?.get(oldItemPosition)?.title != newList?.get(newItemPosition)?.title -> {
                false
            }
            oldList?.get(oldItemPosition)?.voteAverage != newList?.get(newItemPosition)?.voteAverage -> {
                false
            }
            oldList?.get(oldItemPosition)?.voteCount != newList?.get(newItemPosition)?.voteCount -> {
                false
            }*/
            oldList?.get(oldItemPosition)?.equals(newList?.get(newItemPosition)) == false -> {
                false
            }
            else -> {
                true
            }
        }
    }
}