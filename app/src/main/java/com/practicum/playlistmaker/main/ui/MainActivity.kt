package com.practicum.playlistmaker.main.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navController = Navigation.findNavController(this, R.id.content)
        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, dest, args ->

            val withoutNavigation = listOf(R.id.playlist_create, R.id.player, R.id.playlist)

            if(dest.id in withoutNavigation) {
                binding.bottomNav.visibility = View.GONE
                binding.navBottomLine.visibility = View.GONE
            }

            else {
                binding.bottomNav.visibility = View.VISIBLE
                binding.navBottomLine.visibility = View.VISIBLE
            }

            true
        }
    }


}