package com.example.movieapp.ui.now_playing_movie

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movieapp.data.api.POST_PER_PAGE
import com.example.movieapp.data.api.TheMovieDBInterface
import com.example.movieapp.data.repository.MovieDataSource
import com.example.movieapp.data.repository.MovieDataSourceFactory
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.Movie
import com.example.movieapp.data.vo.MovieDetails
import com.example.movieapp.room.MovieDao
import com.example.movieapp.room.NowPlayingMovieDatabase
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MoviePagedListRepository (private val apiService: TheMovieDBInterface, private val context: Context) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<Movie>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }

    /*fun getMoviePagedListFromDB(): LiveData<PagedList<MovieDetails>>{
        val movieDao = NowPlayingMovieDatabase.getDBInstance(context).movieDao()
        runBlocking {
            withContext(Dispatchers.Default) {
                val config = PagedList.Config.Builder()
                    .setPageSize(POST_PER_PAGE)
                    .setEnablePlaceholders(false)
                    .build()
                movieDao.getMovieList()
            }
        }
        
    }*/

}