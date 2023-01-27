package com.umut.movieproject

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewService {
    @GET("movie/{movie_id}/reviews")
    suspend fun getReviews(
        @Path("movie_id") movieID: String,
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): ReviewResponse?
}