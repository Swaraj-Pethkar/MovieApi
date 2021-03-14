package com.example.movieapi.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapi.R
import com.example.movieapi.mvvm.data.api.TheMovieDBClient
import com.example.movieapi.mvvm.data.api.TheMovieDBInterface
import com.example.movieapi.mvvm.data.repository.NetworkState
import com.example.movieapi.mvvm.popular_movie.MainActivityViewModel
import com.example.movieapi.mvvm.popular_movie.MoviePageListRepository
import com.example.movieapi.mvvm.popular_movie.PopularMoviePagedListAdapter

class MvvmActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel

    lateinit var movieRepository: MoviePageListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvvm)
        val recyclerMovie = findViewById<RecyclerView>(R.id.rv_movie_list)
        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        val progressBarPopular = findViewById<ProgressBar>(R.id.progress_bar_popular)
        val textErrorPopular = findViewById<TextView>(R.id.txt_error_popular)

        movieRepository = MoviePageListRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = PopularMoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType:Int = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.Movie_View_Type)
                    return 1
                else
                    return 3
            }
        }
        recyclerMovie.layoutManager = gridLayoutManager
        recyclerMovie.setHasFixedSize(true)
        recyclerMovie.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progressBarPopular.visibility = if(viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            textErrorPopular.visibility = if(viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if(!viewModel.listIsEmpty()){
                movieAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this,object : ViewModelProvider.Factory{
            override fun <T:ViewModel?> create(modelClass: Class<T>):T{
                @Suppress("Unchecked Cast")
                return MainActivityViewModel(movieRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }
}