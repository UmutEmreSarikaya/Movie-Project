package com.umut.movieproject

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SimilarMovieService {
    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieID: String,
        @Query("api_key") key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ) : Response?
}