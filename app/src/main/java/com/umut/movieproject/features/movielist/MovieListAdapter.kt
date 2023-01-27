package com.umut.movieproject.features.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umut.movieproject.BuildConfig
import com.umut.movieproject.Movie
import com.umut.movieproject.R
import com.umut.movieproject.databinding.ItemMovieBinding
import com.umut.movieproject.databinding.LinearItemMovieBinding

class MovieListAdapter(
    private val layoutManager: GridLayoutManager,
    val onItemClickListener: (Movie?) -> Unit,
    val addMovieAsFavorite: (Movie?) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType {
        LINEAR,
        GRID
    }

    private var movies: MutableList<Movie?>? = mutableListOf()
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
            holder.bindItems(movies?.get(position))

        } else if (holder is LinearMovieListHolder) {
            holder.bindItems(movies?.get(position))
        }
    }

    override fun getItemCount() = movies?.size ?: 0

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
                onItemClickListener(movies?.get(adapterPosition))
            }
            itemBinding.imageButtonFavorite.setOnClickListener {
                addMovieAsFavorite(movies?.get(adapterPosition))
                if (itemBinding.imageButtonFavorite.drawable.constantState == ContextCompat.getDrawable(
                        itemView.context,
                        R.drawable.ic_baseline_favorite_24
                    )?.constantState
                ) {
                    itemBinding.imageButtonFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                } else {
                    itemBinding.imageButtonFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
            }
        }

        fun bindItems(item: Movie?) {
            itemBinding.textMovieName.text = item?.title
            itemBinding.textVoteAverage.text = item?.voteAverage.toString()
            Glide.with(itemView).load(BuildConfig.IMAGE_BASE_URL + BuildConfig.POSTER_SIZE  + item?.posterPath)
                .into(itemBinding.imageMovie)

            if (favoriteMovies?.contains(item) == true) {
                itemBinding.imageButtonFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                itemBinding.imageButtonFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }

        }
    }

    inner class LinearMovieListHolder(private val itemBinding: LinearItemMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickListener(movies?.get(adapterPosition))
            }
        }

        fun bindItems(item: Movie?) {
            itemBinding.textMovieName.text = item?.title

            Glide.with(itemView).load(BuildConfig.IMAGE_BASE_URL + BuildConfig.POSTER_SIZE + item?.posterPath)
                .into(itemBinding.imageMovie)
        }
    }

    /*fun setMovieList(movies: MutableList<Movie?>?) {
        this.movies = movies
        this.movies?.size?.let { notifyItemRangeChanged(0, it) }

        notifyDataSetChanged()
    }*/

    fun setMovieList(newMovies: MutableList<Movie?>?) {
        val movieDiffUtil = MovieDiffUtil(movies, newMovies)
        val diffResult = DiffUtil.calculateDiff(movieDiffUtil)
        this.movies = newMovies
        diffResult.dispatchUpdatesTo(this)
    }


    fun setFavoriteMovieList(favoriteMovies: MutableList<Movie?>?) {
        this.favoriteMovies = favoriteMovies
    }
}