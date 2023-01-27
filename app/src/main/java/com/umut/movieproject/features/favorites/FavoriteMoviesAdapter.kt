package com.umut.movieproject.features.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umut.movieproject.BuildConfig
import com.umut.movieproject.Movie
import com.umut.movieproject.R
import com.umut.movieproject.databinding.ItemMovieBinding
import com.umut.movieproject.databinding.LinearItemMovieBinding
import com.umut.movieproject.features.movielist.MovieDiffUtil
import okhttp3.internal.notify

class FavoriteMoviesAdapter(
    private val layoutManager: GridLayoutManager,
    val onItemClickListener: (Movie?) -> Unit,
    val removeFromFavorite: (Movie?) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType {
        LINEAR,
        GRID
    }

    private var favoriteMovies: MutableList<Movie?>? = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == ViewType.GRID.ordinal) {
            val itemBinding =
                ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MovieListHolder(itemBinding)
        } else {
            val linearItemBinding =
                LinearItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LinearMovieListHolder(linearItemBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieListHolder) {
            holder.bindItems(favoriteMovies?.get(position))

        } else if (holder is LinearMovieListHolder) {
            holder.bindItems(favoriteMovies?.get(position))
        }
    }

    override fun getItemCount() = favoriteMovies?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return if (layoutManager.spanCount == 2) {
            ViewType.GRID.ordinal
        } else {
            ViewType.LINEAR.ordinal
        }
    }

    inner class MovieListHolder(private val itemBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickListener(favoriteMovies?.get(adapterPosition))
            }
            itemBinding.imageButtonFavorite.setOnClickListener {
                removeFromFavorite(favoriteMovies?.get(adapterPosition))
                favoriteMovies?.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }

        fun bindItems(item: Movie?) {
            itemBinding.textMovieName.text = item?.title
            itemBinding.textVoteAverage.text = item?.voteAverage.toString()
            Glide.with(itemView).load(BuildConfig.IMAGE_BASE_URL + BuildConfig.POSTER_SIZE + item?.posterPath)
                .into(itemBinding.imageMovie)

            itemBinding.imageButtonFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)

        }
    }

    inner class LinearMovieListHolder(private val itemBinding: LinearItemMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickListener(favoriteMovies?.get(adapterPosition))
            }
        }

        fun bindItems(item: Movie?) {
            itemBinding.textMovieName.text = item?.title

            Glide.with(itemView).load(BuildConfig.IMAGE_BASE_URL + BuildConfig.POSTER_SIZE + item?.posterPath)
                .into(itemBinding.imageMovie)
        }
    }

    /*fun setFavoriteMovieList(favoriteMovies: MutableList<Movie?>?) {
        this.favoriteMovies = favoriteMovies
        notifyItemRangeInserted( 0, this.favoriteMovies?.size ?: 0)
    }*/

    fun setFavoriteMovieList(newMovies: MutableList<Movie?>?) {
        val movieDiffUtil = MovieDiffUtil(favoriteMovies, newMovies)
        val diffResult = DiffUtil.calculateDiff(movieDiffUtil)
        this.favoriteMovies = newMovies
        diffResult.dispatchUpdatesTo(this)
    }

}