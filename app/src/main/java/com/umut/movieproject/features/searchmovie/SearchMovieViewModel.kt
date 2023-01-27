package com.umut.movieproject.features.searchmovie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umut.movieproject.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchMovieViewModel @Inject constructor(
    private val searchService: SearchService,
    private val movieDao: MovieDao
) : ViewModel() {
    private var searchedMovies: MutableList<Movie?>? = mutableListOf()
    private var searchPage = 1
    private var isGrid = true

    val favoriteLiveData = MutableLiveData<MutableList<Movie?>?>()
    val searchLiveData = MutableLiveData<Response?>()

    suspend fun searchMovies(
        key: String,
        language: String,
        query: String,
        page: Int,
        includeAdult: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = searchService.searchMovies(key, language, query, page, includeAdult)

            viewModelScope.launch(Dispatchers.Main) {
                searchLiveData.value = response
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

    fun addToSearchedMovies(searchedMovies: MutableList<Movie?>?){
        searchedMovies?.let { this.searchedMovies?.addAll(it) }
    }

    fun setSearchedMovies(searchedMovies: MutableList<Movie?>?){
        this.searchedMovies = searchedMovies
    }

    fun getSearchedMovies(): MutableList<Movie?>?{
        return this.searchedMovies
    }

    fun incrementSearchPage(){
        this.searchPage++
    }

    fun resetSearchPage(){
        this.searchPage = 1
    }

    fun getSearchPage(): Int{
        return this.searchPage
    }

    fun setIsGrid(isGrid: Boolean){
        this.isGrid = isGrid
    }

    fun getIsGrid(): Boolean{
        return this.isGrid
    }
}


