package com.example.movieapp.ui.now_playing_movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.R
import com.example.movieapp.data.api.POST_PER_PAGE
import com.example.movieapp.data.api.TheMovieDBClient
import com.example.movieapp.data.api.TheMovieDBInterface
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.Movie
import com.example.movieapp.data.vo.MovieDetails
import com.example.movieapp.room.NowPlayingMovieDatabase
import com.example.movieapp.ui.single_movie_details.SingleMovie
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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

        val movieDao = NowPlayingMovieDatabase.getDBInstance(this).movieDao()


        //if (viewModel.moviePagedListFromRoom == null){
            viewModel.moviePagedList.observe(this, Observer { it ->
                movieAdapter.submitList(it)
                Log.i("Movie","Movie List Insertion Started!")
                //var movies: List<Movie>
               /* it.forEach {
                    var movie = Movie(it.page,it.id,it.posterPath,it.releaseDate,it.title)
                    Log.i("Movie","ID: ${movie.toString()}")
                    movieDao.insertMovieList(movie)

                }*/
                movieDao.insertMovieList(it)
                Log.i("Movie","Movie List Inserted!")
                Log.i("Movie","Movie List from DB")
                movieDao.getMovieList().forEach {
                    Log.i("Movie","Title: ${it.title}")
                    Log.i("Movie","Poster Path: ${it.posterPath}")
                    Log.i("Movie","Release Date: ${it.releaseDate}")
                }
                Log.i("Movie","Movie List from DB finished!")
                // need to insert this list to room db
                //insertMoviePagedListToDB()
                /*val movieDao = NowPlayingMovieDatabase.getDBInstance(this).movieDao()
                runBlocking {
                    withContext(Dispatchers.Default) {
                        val config = PagedList.Config.Builder()
                            .setPageSize(POST_PER_PAGE)
                            .setEnablePlaceholders(false)
                            .build()
                        movieDao.insertMovieList(it)
                    }
                }
*/
            })
        //}
        /*else {
            viewModel.moviePagedListFromRoom.observe(this, Observer {
                movieAdapter.submitList(it)
            })
        }*/


        /* runBlocking {
                       withContext(Dispatchers.Default) {
                           val config = PagedList.Config.Builder()
                               .setPageSize(POST_PER_PAGE)
                               .setEnablePlaceholders(false)
                               .build()
                           movieDao.getMovieList(it)
                   }*/
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