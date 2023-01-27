package com.umut.movieproject.features.movielist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umut.movieproject.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListMovieViewModel @Inject constructor(
    private val movieService: MovieService,
    private val movieDao: MovieDao
) : ViewModel() {

    val movieLiveData = MutableLiveData<Response?>()
    val favoriteLiveData = MutableLiveData<MutableList<Movie?>?>()

    private var page = 1
    private var loadFirstPage = true
    private var movies: MutableList<Movie?>? = mutableListOf()
    private var isGrid = true

    suspend fun getMovies(key: String, language: String, page: Int, region: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = movieService.listPopularMovies(key, language, page, region)

            viewModelScope.launch(Dispatchers.Main) {
                movieLiveData.value = response
            }
        }
    }

    suspend fun getFavoriteMovies(){
        viewModelScope.launch(Dispatchers.IO){
            val favoriteMovies = movieDao.getFavorites()

            viewModelScope.launch(Dispatchers.Main) {
                favoriteLiveData.value = favoriteMovies
            }
        }
    }

    suspend fun addToFavorite(movie: Movie?){
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.insert(movie)
        }
    }

    suspend fun deleteFavorite(movie: Movie?){
        viewModelScope.launch(Dispatchers.IO) {
            movieDao.delete(movie)
        }
    }


    fun incrementPage(){
        this.page++
    }

    fun getPage(): Int{
        return this.page
    }

    fun setLoadFirstPage(loadFirstPage: Boolean){
        this.loadFirstPage = loadFirstPage
    }

    fun getLoadFirstPage(): Boolean{
        return this.loadFirstPage
    }

    fun setIsGrid(isGrid: Boolean){
        this.isGrid = isGrid
    }

    fun getIsGrid(): Boolean{
        return this.isGrid
    }

    fun setMovieList(movies: MutableList<Movie?>?){
        movies?.let { this.movies?.addAll(it) }
    }

    fun getMovieList(): MutableList<Movie?>?{
        return this.movies
    }

}
