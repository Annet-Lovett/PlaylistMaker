package com.practicum.playlistmaker.main.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.NavigationRail
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = Navigation.findNavController(this, R.id.content)
        binding.bottomNav.setupWithNavController(navController)
    }


}