package com.example.movieapp.ui.now_playing_movie

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.MovieDetails
import com.example.movieapp.room.NowPlayingMovieDatabase
import io.reactivex.disposables.CompositeDisposable


class MainActivityViewModel (private val movieRepository: MoviePagedListRepository,private val context: Context): ViewModel() {

    //private val compositeDisposable = CompositeDisposable()

    /*val moviePagedList: LiveData<PagedList<MovieDetails>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }*/

/*    val moviePagedListFromRoom: LiveData<PagedList<Movie>> by lazy {
        movieRepository.getMoviePagedListFromDB()
    }*/

    val pagedListLiveData : LiveData<PagedList<MovieDetails>> by lazy {
        val movieDao = NowPlayingMovieDatabase.getDBInstance(context).movieDao()
        movieRepository.getMoviePagedListFromDB(movieDao.getMovieList())
    }

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return pagedListLiveData.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
    }

}