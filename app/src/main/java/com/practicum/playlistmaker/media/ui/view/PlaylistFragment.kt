package com.practicum.playlistmaker.media.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.media.ui.view_model.PlaylistViewModel


class PlaylistFragment : Fragment() {

    companion object {

        fun newInstance() = PlaylistFragment()
    }

    private lateinit var binding: FragmentPlaylistBinding

    private val playlistViewModel: PlaylistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.setOnClickListener {

        }

    }

}