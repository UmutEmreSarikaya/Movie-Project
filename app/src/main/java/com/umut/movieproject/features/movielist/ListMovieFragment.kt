package com.umut.movieproject.features.movielist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.umut.movieproject.databinding.FragmentListMovieBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListMovieFragment : Fragment() {

    private val viewModel: ListMovieViewModel by viewModels()
    private lateinit var binding: FragmentListMovieBinding
    private var isLoading = false
    private lateinit var layoutManager: GridLayoutManager
    private var favoriteMovies: MutableList<Movie?>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.getMovies(
                BuildConfig.API_KEY,
                "en-US",
                viewModel.getPage(),
                ""
            )

            viewModel.getFavoriteMovies()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentListMovieBinding.inflate(layoutInflater)

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

                if (!isLoading) {

                    if (lastVisibleItem >= totalItemCount - 10) {
                        Log.d("mlog,", "lastVisible:${lastVisibleItem} total:${totalItemCount}")
                        // if (!recyclerView.canScrollVertically(1)){
                        isLoading = true
                        viewModel.incrementPage()
                        lifecycleScope.launch {
                            viewModel.getMovies(
                                BuildConfig.API_KEY,
                                "en-US",
                                viewModel.getPage(),
                                ""
                            )
                        }
                        isLoading = false
                    }
                }
            }
        })

        lifecycleScope.launch {
            viewModel.getFavoriteMovies()
        }

        viewModel.movieLiveData.observe(viewLifecycleOwner) {
            if (viewModel.getLoadFirstPage() || it?.page != 1) {
                viewModel.setMovieList(it?.movies)
                viewModel.setLoadFirstPage(false)
            }
            movieListAdapter.movies = viewModel.getMovieList()
        }

        viewModel.favoriteLiveData.observe((viewLifecycleOwner)) {
            movieListAdapter.setFavoriteMovieList(it)
        }

        binding.buttonListType.setOnClickListener {
            if (viewModel.getIsGrid()) {
                viewModel.setIsGrid(false)
                layoutManager.spanCount = 1
                binding.buttonListType.setImageResource(R.drawable.ic_baseline_grid_view_24)
            } else {
                viewModel.setIsGrid(true)
                layoutManager.spanCount = 2
                binding.buttonListType.setImageResource(R.drawable.ic_baseline_list_24)
            }
            movieListAdapter.notifyItemRangeChanged(0, movieListAdapter.itemCount)
        }

        return binding.root
    }

    private fun onItemClickListener(movie: Movie?) {
        val action = ListMovieFragmentDirections.actionListMovieFragmentToMovieDetailFragment(movie)
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

    override fun onResume() {
        showBottomMenu()
        super.onResume()
    }

    private fun showBottomMenu() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE
    }

}
