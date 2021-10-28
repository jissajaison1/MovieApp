package com.example.movieapp.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.movieapp.data.api.FIRST_PAGE
import com.example.movieapp.data.api.TheMovieDBInterface
import com.example.movieapp.data.vo.Movie
import com.example.movieapp.room.NowPlayingMovieDatabase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(private val apiService: TheMovieDBInterface, private val compositeDisposable: CompositeDisposable, private val context: Context) : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    val movieDao = NowPlayingMovieDatabase.getDBInstance(context).movieDao()

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getNowPlayingMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        it.movieLists?.let { it1 -> movieDao.insertMovieList(it1) }
                        if(it.totalPages!! >= params.key) {
                            it.movieLists?.let { it1 -> callback.onResult(it1,params.key+1) }
                            //Log.i("Movie","loadAfter insert started")
                            //movieDao.insertMovieList(it.movieLists)
                            //Log.i("Movie","loadAfter insert finished")
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieResponse",it.message)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getNowPlayingMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        it.movieLists?.let { it1 -> callback.onResult(it1,null,page+1) }
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieResponse",it.message)
                    }
                )
        )
    }
}