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
import com.example.movieapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable


class MoviePagedListRepository (private val apiService: TheMovieDBInterface, private val context: Context) {

    lateinit var moviePagedList: LiveData<PagedList<MovieDetails>>
    lateinit var moviePagedListFromDb: LiveData<PagedList<MovieDetails>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    /*fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<MovieDetails>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable,context)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()

        return moviePagedList
    }*/

    fun getNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }


    fun getMoviePagedListFromDB(movieList: List<MovieDetails>): LiveData<PagedList<MovieDetails>>{
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, movieList,context)
        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()
        moviePagedListFromDb = LivePagedListBuilder(moviesDataSourceFactory, config).build()
        return moviePagedListFromDb

    }

}