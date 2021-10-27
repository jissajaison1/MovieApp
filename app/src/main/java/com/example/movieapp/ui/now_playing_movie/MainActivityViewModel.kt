package com.example.movieapp.ui.now_playing_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.movieapp.data.repository.NetworkState
import com.example.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable


class MainActivityViewModel (private val movieRepository: MoviePagedListRepository): ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviePagedList: LiveData<PagedList<Movie>> by lazy {
        movieRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

 /*   val moviePagedListFromRoom: LiveData<PagedList<Movie>> by lazy {
        movieRepository.getMoviePagedListFromDB()
    }*/

    val networkState: LiveData<NetworkState> by lazy {
        movieRepository.getNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}