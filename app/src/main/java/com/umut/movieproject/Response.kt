package com.umut.movieproject

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Response(
    @SerializedName("page") val page: Int? = 0,
    @SerializedName("results") val movies: MutableList<Movie?>? = null,
    @SerializedName("total_pages") val totalPages: Int? = 0,
    @SerializedName("total_results") val totalResults: Int? = 0
)

@Entity
@Parcelize
data class Movie(
    @ColumnInfo@SerializedName("adult") val adult: Boolean? = false,
    @PrimaryKey@SerializedName("id") val movieID: Int? = 0,
    @ColumnInfo@SerializedName("overview") val overview: String? = "",
    @ColumnInfo@SerializedName("poster_path") val posterPath: String? = "",
    @ColumnInfo@SerializedName("release_date") val releaseDate: String? = "",
    @ColumnInfo@SerializedName("title") val title: String? = "",
    @ColumnInfo@SerializedName("vote_average") val voteAverage: Double? = 0.0,
    @ColumnInfo@SerializedName("vote_count") val voteCount: Double? = 0.0,
    //@ColumnInfo var isFavorite: Boolean = false
): Parcelable
