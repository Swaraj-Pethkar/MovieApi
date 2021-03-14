package com.example.movieapi.mvvm.data.vo


import com.google.gson.annotations.SerializedName

data class MovieResponse(
        @SerializedName("page")
        val page: Int,
        @SerializedName("results")
        val movieList: List<Movie>,
        @SerializedName("total_pages")
        val totalPages: Int,
        @SerializedName("total_results")
        val totalResults: Int

)