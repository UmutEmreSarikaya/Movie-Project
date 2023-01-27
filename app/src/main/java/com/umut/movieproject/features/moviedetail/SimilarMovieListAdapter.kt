package com.umut.movieproject.features.moviedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umut.movieproject.Artist
import com.umut.movieproject.BuildConfig
import com.umut.movieproject.Movie
import com.umut.movieproject.R
import com.umut.movieproject.databinding.ItemMovieBinding
import com.umut.movieproject.databinding.ItemSimilarMovieBinding

class SimilarMovieListAdapter(val onItemClickListener: (Movie?) -> Unit) :
    RecyclerView.Adapter<SimilarMovieListAdapter.SimilarMovieListHolder>() {
    private var similarMovies: MutableList<Movie?>? = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieListHolder {
        val itemBinding =
            ItemSimilarMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimilarMovieListHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SimilarMovieListHolder, position: Int) {
        holder.bindItems(similarMovies?.get(position))
    }

    override fun getItemCount() = similarMovies?.size ?: 0

    inner class SimilarMovieListHolder(private val itemBinding: ItemSimilarMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemView.setOnClickListener {
                onItemClickListener(similarMovies?.get(adapterPosition))
            }
        }

        fun bindItems(item: Movie?) {
            itemBinding.textMovieName.text = item?.title
            itemBinding.textVoteAverage.text = item?.voteAverage.toString()
            Glide.with(itemView)
                .load(BuildConfig.IMAGE_BASE_URL + BuildConfig.POSTER_SIZE + item?.posterPath)
                .into(itemBinding.imageMovie)
            itemBinding.imageButtonFavorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    fun setSimilarMovieList(similarMovies: MutableList<Movie?>?) {
        this.similarMovies = similarMovies
        notifyItemRangeInserted(similarMovies?.size ?: 0, 20)
    }

}