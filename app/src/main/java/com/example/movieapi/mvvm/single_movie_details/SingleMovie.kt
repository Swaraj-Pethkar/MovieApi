package com.example.movieapi.mvvm.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.movieapi.R
import com.example.movieapi.mvvm.data.api.POSTER_BASE_URL
import com.example.movieapi.mvvm.data.api.TheMovieDBClient
import com.example.movieapi.mvvm.data.api.TheMovieDBInterface
import com.example.movieapi.mvvm.data.repository.NetworkState
import com.example.movieapi.mvvm.data.vo.MovieDetails
import retrofit2.http.POST

class SingleMovie : AppCompatActivity() {
    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id",1)
        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)
        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
            val txtError = findViewById<TextView>(R.id.txt_error)
            progressBar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txtError.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE

        })
    }
    fun bindUI(it:MovieDetails){
        val movieTitle = findViewById<TextView>(R.id.movie_title)
        val movieRating = findViewById<TextView>(R.id.movie_rating)
        val ivMoviePoster = findViewById<ImageView>(R.id.iv_movie_poster)
        val releaseDate = findViewById<TextView>(R.id.movie_release_date)
        val overView = findViewById<TextView>(R.id.movie_overview)
        movieTitle.text = it.title
        releaseDate.text = it.releaseDate
        movieRating.text = it.rating.toString()
        overView.text = it.overview


        val moviePosterURL = POSTER_BASE_URL+it.posterPath
        Glide.with(this)
                .load(moviePosterURL)
                .into(ivMoviePoster)
    }

    private fun getViewModel(movieId:Int):SingleMovieViewModel{
        return ViewModelProviders.of(this,object : ViewModelProvider.Factory{
            override fun <T: ViewModel?> create(modelClass: Class<T>):T {
                @Suppress("UNCHECKED_CAST")
                return SingleMovieViewModel(movieRepository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}