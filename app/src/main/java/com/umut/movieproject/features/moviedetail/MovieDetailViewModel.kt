package com.umut.movieproject.features.moviedetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umut.movieproject.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val artistService: ArtistService,
    private val similarMovieService: SimilarMovieService,
    private val reviewService: ReviewService
) : ViewModel() {
    val artistLiveData = MutableLiveData<ArtistResponse?>()
    val similarMovieLiveData = MutableLiveData<Response?>()
    val reviewLiveData = MutableLiveData<ReviewResponse?>()

    private var similarMovies: MutableList<Movie?>? = mutableListOf()
    private var similarMoviePage = 1

    suspend fun getArtists(movieID: String, key: String, language: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = artistService.getArtists(movieID, key, language)

            viewModelScope.launch(Dispatchers.Main) {
                artistLiveData.value = response
            }
        }
    }

    suspend fun getSimilarMovies(movieID: String, key: String, language: String, page: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val similarMovieResponse = similarMovieService.getSimilarMovies(movieID, key, language, page)

            viewModelScope.launch(Dispatchers.Main) {
                similarMovieLiveData.value = similarMovieResponse
            }
        }
    }

    suspend fun getReviews(movieID: String, key: String, language: String, page: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val reviewResponse = reviewService.getReviews(movieID, key, language, page)

            viewModelScope.launch(Dispatchers.Main) {
                reviewLiveData.value = reviewResponse
            }
        }
    }

    fun incrementSimilarMoviePage(){
        this.similarMoviePage++
    }

    fun getSimilarMoviePage(): Int{
        return this.similarMoviePage
    }

    fun getSimilarMovies(): MutableList<Movie?>?{
        return this.similarMovies
    }

    fun setSimilarMovies(similarMovies: MutableList<Movie?>?){
        similarMovies?.let { this.similarMovies?.addAll(it) }
    }

}