package com.umut.movieproject

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("search/movie")
        suspend fun searchMovies(
            @Query("api_key") key: String,
            @Query("language") language: String,
            @Query("query") query: String,
            @Query("page") page: Int,
            @Query("include_adult") includeAdult: Boolean
        ): Response?
}