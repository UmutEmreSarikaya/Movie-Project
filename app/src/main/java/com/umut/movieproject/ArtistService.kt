package com.umut.movieproject

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ArtistService {
    @GET("movie/{movieID}/credits")
    suspend fun getArtists(
        @Path("movieID") movieID: String,
        @Query("api_key") key: String,
        @Query("language") language: String
    ) : ArtistResponse?
}