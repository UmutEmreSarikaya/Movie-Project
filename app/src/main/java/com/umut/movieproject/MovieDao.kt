package com.umut.movieproject

import androidx.room.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie")
    fun getFavorites(): MutableList<Movie?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie?)

    @Delete
    fun delete(movie: Movie?)
}