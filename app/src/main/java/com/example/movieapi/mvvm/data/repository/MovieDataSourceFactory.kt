package com.example.movieapi.mvvm.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.movieapi.mvvm.data.vo.Movie
import androidx.paging.DataSource
import com.example.movieapi.mvvm.data.api.TheMovieDBInterface
import io.reactivex.disposables.CompositeDisposable


class MovieDataSourceFactory(private val apiService: TheMovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, Movie>() {
    val movieLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService,compositeDisposable)

        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}