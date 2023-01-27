package com.umut.movieproject.features.moviedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umut.movieproject.Artist
import com.umut.movieproject.Movie
import com.umut.movieproject.Review
import com.umut.movieproject.databinding.ItemArtistBinding
import com.umut.movieproject.databinding.ItemReviewBinding

class ReviewListAdapter: RecyclerView.Adapter<ReviewListAdapter.ReviewListHolder>() {
    private var reviews: MutableList<Review?>? = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewListAdapter.ReviewListHolder {
        val itemBinding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewListHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ReviewListAdapter.ReviewListHolder, position: Int) {
        holder.bindItems(reviews?.get(position))
    }

    override fun getItemCount() = reviews?.size ?: 0

    inner class ReviewListHolder(private val itemBinding: ItemReviewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItems(item: Review?) {
            itemBinding.textAuthor.text = item?.author
            itemBinding.textReview.text = item?.content
        }
    }

    fun setReviewList(reviews: MutableList<Review?>?) {
        this.reviews = reviews
        notifyItemRangeInserted(reviews?.size ?: 0, 20)
    }

}