package com.practicum.playlistmaker.playlist.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.media.ui.view_model.PlaylistViewModel
import kotlin.getValue

class PlaylistFragment : Fragment(R.layout.fragment_playlist) {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding
        get() = _binding!!

    private val playlistViewModel: PlaylistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("args", arguments.toString())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}