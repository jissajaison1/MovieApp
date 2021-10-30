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
    lateinit var moviePagedListFromRoom: LiveData<PagedList<MovieDetails>>
    lateinit var movieListFromDB: List<MovieDetails>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable): LiveData<PagedList<MovieDetails>> {
        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable,context)

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

/*   fun getMoviePagedListFromDB(): LiveData<PagedList<Movie>>{
        val movieDao = NowPlayingMovieDatabase.getDBInstance(context).movieDao()
        runBlocking {
            withContext(Dispatchers.Default) {
                val config = PagedList.Config.Builder()
                    .setPageSize(POST_PER_PAGE)
                    .setEnablePlaceholders(false)
                    .build()
                movieListFromDB = movieDao.getMovieList()
                //moviePagedListFromRoom = movieDao.getMovieList()//need to convert movieListFromDB to Paged List.
            }
        }
        return moviePagedListFromRoom
    }*/

    /*fun insertMoviePagedListToDB(){
        val movieDao = NowPlayingMovieDatabase.getDBInstance(context).movieDao()
        runBlocking {
            withContext(Dispatchers.Default) {
                val config = PagedList.Config.Builder()
                    .setPageSize(POST_PER_PAGE)
                    .setEnablePlaceholders(false)
                    .build()
                movieDao.insertMovieList(moviePagedList)
            }
        }
    }*/

}