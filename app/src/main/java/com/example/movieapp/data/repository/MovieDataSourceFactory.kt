package com.example.movieapp.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movieapp.data.api.TheMovieDBInterface
import com.example.movieapp.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService: TheMovieDBInterface, private val compositeDisposable: CompositeDisposable, private val context: Context) : DataSource.Factory<Int, Movie>() {
    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()
    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable,context)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}