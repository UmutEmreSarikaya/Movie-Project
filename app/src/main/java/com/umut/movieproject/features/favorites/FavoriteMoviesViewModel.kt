package com.umut.movieproject.features.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umut.movieproject.ConfigurationResponse
import com.umut.movieproject.ConfigurationService
import com.umut.movieproject.Movie
import com.umut.movieproject.MovieDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteMoviesViewModel @Inject constructor(
    private val movieDao: MovieDao
) : ViewModel() {

    val favoriteLiveData = MutableLiveData<MutableList<Movie?>?>()

    private var isGrid = true

    suspend fun getFavoriteMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val favoriteMovies = movieDao.getFavorites()

            viewModelScope.launch(Dispatchers.Main) {
                favoriteLiveData.value = favoriteMovies
            }
        }
    }

    suspend fun addToFavorite(movie: Movie?) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.insert(movie)
        }
    }

    suspend fun deleteFavorite(movie: Movie?) {
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.delete(movie)
        }
    }

    fun setIsGrid(isGrid: Boolean) {
        this.isGrid = isGrid
    }

    fun getIsGrid(): Boolean {
        return this.isGrid
    }
}