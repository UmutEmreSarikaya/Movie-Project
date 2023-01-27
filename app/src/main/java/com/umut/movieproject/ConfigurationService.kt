package com.umut.movieproject

import retrofit2.http.GET
import retrofit2.http.Query

interface ConfigurationService {
    @GET("configuration")
    suspend fun getConfiguration(@Query("api_key") key: String): ConfigurationResponse?
}