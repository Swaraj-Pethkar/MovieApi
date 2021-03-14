package com.example.movieapi.mvvm.data.api

import com.example.movieapi.mvvm.data.vo.MovieDetails
import com.example.movieapi.mvvm.data.vo.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {

//    https://api.themoviedb.org/3/movie/popular?api_key=6e485bfa20de70f7dabcf2ca8b4260cf&language=en-US&page=1
//    https://api.themoviedb.org/3/
//    https://image.tmdb.org/t/p/w342/6KErczPBROQty7QoIsaa6wJYXZi.jpg

    @GET("movie/popular")
    fun getPopularMovie(@Query("page")page:Int):Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id")id:Int): Single<MovieDetails>


}