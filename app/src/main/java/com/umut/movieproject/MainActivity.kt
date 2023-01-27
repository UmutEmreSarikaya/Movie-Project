package com.umut.movieproject

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.umut.movieproject.databinding.ActivityMainBinding
import com.umut.movieproject.features.favorites.FavoriteMoviesFragment
import com.umut.movieproject.features.movielist.ListMovieFragment
import com.umut.movieproject.features.searchmovie.SearchMovieFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        /*supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, ListMovieFragment())
            .commit()

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.listMovieFragment -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, ListMovieFragment())
                        .commit()
                    true
                }
                R.id.searchMovieFragment -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, SearchMovieFragment())
                        .commit()
                    true
                }
                R.id.favoriteMovieFragment -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, FavoriteMoviesFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }*/

        /*binding.bottomNavigation.setOnItemReselectedListener {
            when(it.itemId){
                R.id.popular_movies -> {}
                R.id.top_rated_movies -> {}
                R.id.search -> {}
                R.id.favorites ->{}
            }
        }*/

    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.fragment_container)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }*/
}
