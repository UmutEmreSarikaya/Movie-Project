package com.umut.movieproject.features.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umut.movieproject.Movie
import com.umut.movieproject.R
import com.umut.movieproject.databinding.FragmentFavoriteMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteMoviesFragment : Fragment() {

    private val viewModel: FavoriteMoviesViewModel by viewModels()
    private lateinit var binding: FragmentFavoriteMoviesBinding
    private lateinit var layoutManager: GridLayoutManager
    private var favoriteMovies: MutableList<Movie?>? = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentFavoriteMoviesBinding.inflate(layoutInflater)

        showBottomMenu()

        layoutManager = if (viewModel.getIsGrid()) {
            GridLayoutManager(activity, 2)
        } else {
            GridLayoutManager(activity, 1)
        }

        val favoriteMoviesAdapter = FavoriteMoviesAdapter(
            layoutManager,
            ::onItemClickListener,
            ::removeFromFavorite
        )

        binding.recyclerFavoriteMovies.layoutManager = layoutManager
        binding.recyclerFavoriteMovies.adapter = favoriteMoviesAdapter

        lifecycleScope.launch {
            viewModel.getFavoriteMovies()
        }


        viewModel.favoriteLiveData.observe((viewLifecycleOwner)) {
            favoriteMovies = it
            favoriteMoviesAdapter.setFavoriteMovieList(it)
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
            favoriteMoviesAdapter.notifyItemRangeChanged(0, favoriteMoviesAdapter.itemCount)
        }

        return binding.root
    }

    private fun onItemClickListener(movie: Movie?) {
        val action = FavoriteMoviesFragmentDirections.actionFavoriteMovieFragmentToMovieDetailFragment(movie)
        findNavController().navigate(action)
    }

    private fun removeFromFavorite(movie: Movie?) {
        lifecycleScope.launch{
            viewModel.deleteFavorite(movie)
        }
    }

    private fun showBottomMenu() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE
    }
}