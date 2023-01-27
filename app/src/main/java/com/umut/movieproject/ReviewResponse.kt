package com.umut.movieproject

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("page") val page: Int? = 0,
    @SerializedName("results") val reviews: MutableList<Review?>? = null,
    @SerializedName("total_pages") val totalPages: Int? = 0,
    @SerializedName("total_results") val totalResults: Int? = 0
)

data class Review(
    @SerializedName("author") val author: String? = "",
    @SerializedName("content") val content: String? = ""
)
