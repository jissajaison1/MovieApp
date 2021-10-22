package com.example.movieapp.ui.now_playing_movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.data.api.TheMovieDBClient
import com.example.movieapp.data.api.TheMovieDBInterface
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.ui.single_movie_details.SingleMovie
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    lateinit var  movieRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService: TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MoviePagedListRepository(apiService,this)
        viewModel = getViewModel()

        val movieAdapter = NowPlayingMoviePagedListAdapter(this)

        val gridLayoutManager = GridLayoutManager(this,3)

        gridLayoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = movieAdapter.getItemViewType(position)
                return if(viewType == movieAdapter.MOVIE_VIEW_TYPE) 1
                else 3
            }
        }

        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_now_playing.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_now_playing.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(movieRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }
}