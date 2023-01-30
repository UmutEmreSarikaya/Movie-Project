package com.umut.movieproject.features.searchmovie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umut.movieproject.BuildConfig
import com.umut.movieproject.Movie
import com.umut.movieproject.R
import com.umut.movieproject.databinding.FragmentSearchMovieBinding
import com.umut.movieproject.features.movielist.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMovieFragment : Fragment() {
    private val viewModel: SearchMovieViewModel by viewModels()
    private lateinit var binding: FragmentSearchMovieBinding
    private lateinit var layoutManager: GridLayoutManager
    private var searchingSameQuery = false
    private var isLoading = false
    private var favoriteMovies: MutableList<Movie?>? = mutableListOf()
    private var queryIsEmpty = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSearchMovieBinding.inflate(layoutInflater)

        showBottomMenu()

        layoutManager = if (viewModel.getIsGrid()) {
            GridLayoutManager(activity, 2)
        } else {
            GridLayoutManager(activity, 1)
        }

        val movieListAdapter = MovieListAdapter(
            layoutManager,
            ::onItemClickListener,
            ::addMovieAsFavorite
        )

        binding.recyclerMovie.layoutManager = layoutManager
        binding.recyclerMovie.adapter = movieListAdapter

        binding.recyclerMovie.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && !queryIsEmpty) {
                    if (lastVisibleItem > totalItemCount - 10) {
                        isLoading = true
                        searchingSameQuery = true
                        viewModel.incrementSearchPage()
                        lifecycleScope.launch {
                            viewModel.searchMovies(
                                BuildConfig.API_KEY,
                                "en_US",
                                binding.editTextSearch.text.toString(),
                                viewModel.getSearchPage(),
                                false
                            )
                        }
                        isLoading = false
                    }
                }
            }
        })

        viewModel.searchLiveData.observe(viewLifecycleOwner) {
            if (searchingSameQuery) {
                viewModel.addToSearchedMovies(it?.movies)
            } else {
                viewModel.setSearchedMovies(it?.movies)
            }

            movieListAdapter.movies = viewModel.getSearchedMovies()
        }

        viewModel.favoriteLiveData.observe((viewLifecycleOwner)) {
            movieListAdapter.setFavoriteMovieList(it)
        }

        binding.editTextSearch.tag = false
        binding.editTextSearch.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
            binding.editTextSearch.tag = true
        }

        binding.editTextSearch.addTextChangedListener {
            if (binding.editTextSearch.text.isNotEmpty() && binding.editTextSearch.tag == true) {
                searchingSameQuery = false
                viewModel.resetSearchPage()
                val query = binding.editTextSearch.text.toString()
                lifecycleScope.launch {
                    delay(500)
                    viewModel.searchMovies(
                        BuildConfig.API_KEY,
                        "en_US",
                        query,
                        viewModel.getSearchPage(),
                        false
                    )
                    viewModel.getFavoriteMovies()
                }
                queryIsEmpty = false
            } else if (binding.editTextSearch.tag == true) {
                movieListAdapter.movies = mutableListOf()
                queryIsEmpty = true
            }
        }
        return binding.root
    }

    private fun onItemClickListener(movie: Movie?) {
        val action = SearchMovieFragmentDirections.actionSearchMovieFragmentToMovieDetailFragment(movie)
        findNavController().navigate(action)
    }

    private fun addMovieAsFavorite(movie: Movie?) {
        if (favoriteMovies?.contains(movie) == true) {
            favoriteMovies?.remove(movie)
            lifecycleScope.launch {
                viewModel.deleteFavorite(movie)
            }
        } else {
            favoriteMovies?.add(movie)
            lifecycleScope.launch {
                viewModel.addToFavorite(movie)
            }
        }
    }

    private fun showBottomMenu() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE
    }
}