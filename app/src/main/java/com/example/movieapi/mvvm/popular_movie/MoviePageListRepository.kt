package com.example.movieapi.mvvm.popular_movie

import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movieapi.mvvm.data.api.POST_PER_PAGE
import com.example.movieapi.mvvm.data.api.TheMovieDBInterface
import com.example.movieapi.mvvm.data.repository.MovieDataSource
import com.example.movieapi.mvvm.data.repository.MovieDataSourceFactory
import com.example.movieapi.mvvm.data.repository.NetworkState
import com.example.movieapi.mvvm.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePageListRepository (private val apiService: TheMovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory : MovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService,compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()
        return moviePagedList
    }
fun getNetworkState():LiveData<NetworkState>{
    return Transformations.switchMap<MovieDataSource, NetworkState>(
        moviesDataSourceFactory.movieLiveDataSource, MovieDataSource::networkState
    )
}
}