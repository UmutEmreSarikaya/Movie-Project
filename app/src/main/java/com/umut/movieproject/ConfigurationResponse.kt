package com.umut.movieproject

import com.google.gson.annotations.SerializedName

data class ConfigurationResponse(
    @SerializedName("images") val images: Images? = null,
    @SerializedName("change_keys") val changeKeys: List<String?>? = null
)

data class Images(
    @SerializedName("base_url") val baseURL: String? = "",
    @SerializedName("secure_base_url") val secureBaseURL: String? = "",
    @SerializedName("poster_sizes") val posterSizes: List<String?>? = null
)
