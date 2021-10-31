package com.example.movieapp.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.movieapp.data.api.FIRST_PAGE
import com.example.movieapp.data.api.TheMovieDBInterface
import com.example.movieapp.data.vo.MovieDetails
import com.example.movieapp.room.NowPlayingMovieDatabase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(private val apiService: TheMovieDBInterface, private val movieList: List<MovieDetails>, private val context: Context) : PageKeyedDataSource<Int, MovieDetails>() {

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    private val movieDao = NowPlayingMovieDatabase.getDBInstance(context).movieDao()

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieDetails>) {
        networkState.postValue(NetworkState.LOADING)

        //compositeDisposable.add(
            apiService.getNowPlayingMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        it.movieLists?.let { it1 -> movieDao.insertMovieList(it1) }
                        Log.i("Movie","LOAD_AFTER")
                        if(it.totalPages!! >= params.key) {
                            it.movieLists?.let { it1 -> callback.onResult(movieDao.getMovieList(),params.key+1) }
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        callback.onResult(movieDao.getMovieList(),params.key+1)
                        networkState.postValue(NetworkState.ENDOFLIST)
                        Log.e("MovieResponse",it.message)
                    }
                )
        //)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieDetails>) {
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, MovieDetails>) {
        networkState.postValue(NetworkState.LOADING)

        //compositeDisposable.add(
            apiService.getNowPlayingMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        it.movieLists?.let { it1 -> movieDao.insertMovieList(it1) }
                        Log.i("Movie","LOAD_INITIAL")
                        it.movieLists?.let { it1 -> callback.onResult(movieDao.getMovieList(),null,page+1) }
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        callback.onResult(movieDao.getMovieList(),null,page+1)
                        networkState.postValue(NetworkState.ENDOFLIST)
                        Log.e("MovieResponse",it.message)
                    }
                )
        //)
    }
}