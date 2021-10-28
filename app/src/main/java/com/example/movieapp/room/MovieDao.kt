package com.example.movieapp.room

import androidx.room.*
import com.example.movieapp.data.vo.MovieDetails

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieList(movieDetails: List<MovieDetails>)

    //@Update
    //fun updateMovie(movieDetails: MovieDetails)

    @Query("SELECT * FROM NowPlayingMovies WHERE id = :id_")
    fun getMovie(id_: Int): MovieDetails

    //@Query("SELECT * FROM NowPlayingMovies WHERE page = :page_")
    //fun getMovieList(page_:Int): List<Movie>

    @Query("SELECT * FROM NowPlayingMovies")
    fun getMovieList(): List<MovieDetails>

    @Query("DELETE FROM NowPlayingMovies")
    fun deleteAll()

}