package com.example.movieapp.room

import androidx.room.TypeConverter
import com.example.movieapp.data.vo.MovieDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TypeConverterMovie {
    val gson: Gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): MovieDetails? {
        if(data == null) return null
        val listType: Type = object: TypeToken<MovieDetails?>() {}.type
        return gson.fromJson<MovieDetails?>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObject: MovieDetails?): String? {
        return gson.toJson(someObject)
    }
}