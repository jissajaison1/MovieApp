package com.example.movieapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.movieapp.data.vo.Movie
import com.example.movieapp.data.vo.MovieDetails

@Database(entities = [MovieDetails::class, Movie::class], version = 2)
@TypeConverters(TypeConverterMovie::class)
abstract class NowPlayingMovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object{
        private var DB_INSTANCE: NowPlayingMovieDatabase? = null

        fun getDBInstance(context: Context): NowPlayingMovieDatabase {
            if(DB_INSTANCE == null) {
                DB_INSTANCE = Room.databaseBuilder(context.applicationContext, NowPlayingMovieDatabase::class.java,"Movie_DB")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return DB_INSTANCE!!
        }
    }
}