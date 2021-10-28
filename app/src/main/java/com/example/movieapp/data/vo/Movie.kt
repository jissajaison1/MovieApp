package com.example.movieapp.data.vo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Movie(
    val page: Int,
    @PrimaryKey
    var id: Int,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String
)

/*
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "NowPlayingMovies")
data class Movie(
    @PrimaryKey
    val id: Int,
    val posterPath: String,
    val releaseDate: String,
    val title: String
): Parcelable {
}*/
