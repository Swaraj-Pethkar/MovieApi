package com.example.movieapi.mvvm.popular_movie

import android.content.Context
import android.content.Intent
import android.net.Network
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapi.R
import com.example.movieapi.mvvm.data.api.POSTER_BASE_URL
import com.example.movieapi.mvvm.data.repository.NetworkState
import com.example.movieapi.mvvm.data.vo.Movie
import com.example.movieapi.mvvm.single_movie_details.SingleMovie

class PopularMoviePagedListAdapter(public val context: Context) : PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val Movie_View_Type = 1
    val Network_View_Type = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view:View

        if(viewType == Movie_View_Type){
            view = layoutInflater.inflate(R.layout.movie_list_item,parent,false)
            return MovieItemViewHolder(view)
        }
        else {
            view = layoutInflater.inflate(R.layout.network_state_item,parent,false)
            return MovieItemViewHolder.NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == Movie_View_Type){
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }
        else{
            (holder as MovieItemViewHolder.NetworkStateItemViewHolder).bind(networkState)
        }
    }

    class MovieDiffCallback: DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    private fun hasExtraRow() : Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position == itemCount - 1){
            Network_View_Type
        }else{
            Movie_View_Type
        }
    }

    class MovieItemViewHolder (view : View):RecyclerView.ViewHolder(view){
        fun bind(movie: Movie?,context: Context){
            val cvTitle = itemView.findViewById<TextView>(R.id.cv_movie_title)
            val cvReleaseData = itemView.findViewById<TextView>(R.id.cv_movie_release_date)
            val cvMoviePoster = itemView.findViewById<ImageView>(R.id.cv_iv_movie_poster)
            cvTitle.text  = movie?.title
            cvReleaseData.text = movie?.releaseDate
            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(cvMoviePoster)

            itemView.setOnClickListener{
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id",movie?.id)
                context.startActivity(intent)
            }
        }

        class NetworkStateItemViewHolder (view: View): RecyclerView.ViewHolder(view){
            fun bind(networkState: NetworkState?){
                val progressBarItem = itemView.findViewById<ProgressBar>(R.id.progress_bar_item)
                val errorMsgItem = itemView.findViewById<TextView>(R.id.error_msg_item)
                if(networkState != null && networkState == NetworkState.LOADING){
                    progressBarItem.visibility = View.VISIBLE
                }
                else{
                    progressBarItem.visibility = View.GONE
                }

                if(networkState != null && networkState == NetworkState.ERROR){
                    errorMsgItem.visibility = View.VISIBLE
                    errorMsgItem.text = networkState.msg
                }
                else{
                    errorMsgItem.visibility = View.GONE
                }
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState){
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow){
            if (hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }else{
                notifyItemInserted(super.getItemCount())
            }
        }
        else if (hasExtraRow && previousState != newNetworkState){
            notifyItemChanged(itemCount-1)
        }
    }
}