package com.umut.movieproject

import com.umut.movieproject.features.movielist.ListMovieViewModel
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(MockitoJUnitRunner::class)
class ListMovieViewModelUnitTest {

    @Mock
    lateinit var movieService: MovieService
    lateinit var configurationService: ConfigurationService
    lateinit var searchService: SearchService
    lateinit var movieDao: MovieDao

    @InjectMocks
    lateinit var viewModel: ListMovieViewModel

    @Test
    fun testAddToSearchedMovies() {
    }
}
