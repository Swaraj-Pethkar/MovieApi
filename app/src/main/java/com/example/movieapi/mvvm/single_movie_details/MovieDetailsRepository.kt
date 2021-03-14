package com.example.movieapi.mvvm.single_movie_details

import androidx.lifecycle.LiveData
import com.example.movieapi.mvvm.data.api.TheMovieDBInterface
import com.example.movieapi.mvvm.data.repository.MovieDetailsNetworkDataSource
import com.example.movieapi.mvvm.data.repository.NetworkState
import com.example.movieapi.mvvm.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource:MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable,movieId:Int):LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadMovieResponse
    }

    fun getMovieDetailsNetworkState():LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }
}