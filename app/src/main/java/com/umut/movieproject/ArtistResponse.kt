package com.umut.movieproject

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ArtistResponse(
    @SerializedName("id") val movieID: Int? = 0,
    @SerializedName("cast") val cast: MutableList<Artist?>? = null
)

data class Artist(
    @SerializedName("name") val artistName: String? = "",
    @SerializedName("profile_path") val profilePath: String? = "",
    @SerializedName("character") val character: String? = ""
)
