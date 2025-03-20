package com.practicum.playlistmaker.media.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.practicum.playlistmaker.databinding.FragmentTracksBinding
import com.practicum.playlistmaker.media.ui.view_model.TracksViewModel

class TracksFragment : Fragment() {

    companion object {

        fun newInstance() = TracksFragment()
    }

    private lateinit var binding: FragmentTracksBinding

    private val tracksViewModel: TracksViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

}