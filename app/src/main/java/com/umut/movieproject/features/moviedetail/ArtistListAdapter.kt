package com.umut.movieproject.features.moviedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umut.movieproject.Artist
import com.umut.movieproject.databinding.ItemArtistBinding

class ArtistListAdapter: RecyclerView.Adapter<ArtistListAdapter.ArtistListHolder>() {
    private var artists: MutableList<Artist?>? = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistListHolder {
        val itemBinding = ItemArtistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArtistListHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ArtistListHolder, position: Int) {
        holder.bindItems(artists?.get(position))
    }

    override fun getItemCount() = artists?.size ?: 0

    inner class ArtistListHolder(private val itemBinding: ItemArtistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItems(item: Artist?) {
            itemBinding.textArtistName.text = item?.artistName
            itemBinding.textCharacterName.text = item?.character
            Glide.with(itemView).load("https://image.tmdb.org/t/p/original" + item?.profilePath)
                .into(itemBinding.imageArtist)
        }
    }

    fun setArtistList(artists: MutableList<Artist?>?) {
        this.artists = artists
        notifyItemRangeInserted(0, artists?.size ?: 0)
    }

}