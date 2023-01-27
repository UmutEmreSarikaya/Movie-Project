package com.umut.movieproject.features.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.umut.movieproject.BuildConfig
import com.umut.movieproject.Movie
import com.umut.movieproject.R
import com.umut.movieproject.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private val viewModel: MovieDetailViewModel by viewModels()
    private lateinit var binding: FragmentMovieDetailBinding
    private var movie: Movie? = null
    private var isLoading = false
    private var isFromAnotherMovieDetail: Boolean? = null
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        //movie = arguments?.getParcelable("movie_param")
        movie = args.movie
        isFromAnotherMovieDetail = arguments?.getBoolean("is_from_another_detail_param")
        binding = FragmentMovieDetailBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            viewModel.getArtists(
                movie?.movieID.toString(),
                BuildConfig.API_KEY,
                "en_US"
            )

            viewModel.getSimilarMovies(
                movie?.movieID.toString(),
                BuildConfig.API_KEY,
                "en_US",
                viewModel.getSimilarMoviePage()
            )

            viewModel.getReviews(
                movie?.movieID.toString(),
                BuildConfig.API_KEY,
                "en_US",
                1
            )

        }

        /*val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isFromAnotherMovieDetail == false) {
                        showBottomMenu()
                    }else{
                        //binding.appBarLayout.setExpanded(false)
                    }
                    //requireActivity().supportFragmentManager.popBackStack()
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)*/

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        hideBottomMenu()

        val artistLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val similarMovieLayoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val reviewLayoutManager = LinearLayoutManager(activity)

        val artistListAdapter = ArtistListAdapter()
        binding.recyclerArtists.layoutManager = artistLayoutManager
        binding.recyclerArtists.adapter = artistListAdapter

        val similarMovieListAdapter = SimilarMovieListAdapter(::onItemClickListener)
        binding.recyclerSimilarMovies.layoutManager = similarMovieLayoutManager
        binding.recyclerSimilarMovies.adapter = similarMovieListAdapter

        val reviewListAdapter = ReviewListAdapter()
        binding.recyclerReviews.layoutManager = reviewLayoutManager
        binding.recyclerReviews.adapter = reviewListAdapter

        binding.textMovieTitle.text = movie?.title
        binding.textMovieDetail.text = movie?.overview
        Glide.with(this)
            .load(BuildConfig.IMAGE_BASE_URL + BuildConfig.POSTER_SIZE + movie?.posterPath)
            .into(binding.imageMovieDetail)
        binding.textRating.text = movie?.voteAverage.toString()

        binding.recyclerSimilarMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = similarMovieLayoutManager.childCount
                val pastVisibleItem =
                    similarMovieLayoutManager.findFirstCompletelyVisibleItemPosition()
                val totalItemCount = similarMovieLayoutManager.itemCount

                if (!isLoading) {
                    if (visibleItemCount + pastVisibleItem >= totalItemCount) {
                        isLoading = true
                        viewModel.incrementSimilarMoviePage()
                        lifecycleScope.launch {
                            viewModel.getSimilarMovies(
                                movie?.movieID.toString(),
                                BuildConfig.API_KEY,
                                "en-US",
                                viewModel.getSimilarMoviePage()
                            )
                        }
                        isLoading = false
                    }
                }
            }
        })

        viewModel.artistLiveData.observe(viewLifecycleOwner) {
            artistListAdapter.setArtistList(it?.cast)
        }

        viewModel.similarMovieLiveData.observe((viewLifecycleOwner)) {
            viewModel.setSimilarMovies(it?.movies)
            similarMovieListAdapter.setSimilarMovieList(viewModel.getSimilarMovies())
        }

        viewModel.reviewLiveData.observe((viewLifecycleOwner)) {
            reviewListAdapter.setReviewList(it?.reviews)
        }

        return binding.root
    }

    /*companion object {
        @JvmStatic
        fun newInstance(movie: Movie?, isFromAnotherMovieDetail: Boolean) =
            MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("movie_param", movie)
                    putBoolean("is_from_another_detail_param", isFromAnotherMovieDetail)
                }
            }
    }*/

    private fun onItemClickListener(movie: Movie?) {
        val action = MovieDetailFragmentDirections.actionMovieDetailFragmentSelf(movie)
        findNavController().navigate(action)
    }

    private fun hideBottomMenu() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.GONE
    }

    /*private fun showBottomMenu() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE
    }*/
}