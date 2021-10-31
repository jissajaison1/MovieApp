package com.example.movieapp.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.movieapp.data.api.TheMovieDBInterface
import com.example.movieapp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService: TheMovieDBInterface, private val movieList: List<MovieDetails>, private val context: Context) : DataSource.Factory<Int, MovieDetails>() {
    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()
    override fun create(): DataSource<Int, MovieDetails> {
        val movieDataSource = MovieDataSource(apiService, movieList,context)
        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}