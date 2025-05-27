package com.practicum.playlistmaker.playlist.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.playlist.ui.view_states.PlaylistViewState
import com.practicum.playlistmaker.playlist.ui.viewmodel.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class PlaylistFragment : Fragment(R.layout.fragment_playlist) {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding
        get() = _binding!!

    private val playlistMediaViewModel: PlaylistViewModel by viewModel{
        val playlistId = requireArguments().getInt("id")
        parametersOf(playlistId)

    }

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

        playlistMediaViewModel.playlistLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        Log.d("args", arguments.toString())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render (playlistViewState: PlaylistViewState) {
        Glide.with(binding.playlistCover)
            .load(playlistViewState.cover)
            .fitCenter()
            .centerCrop()
            .fallback(R.drawable.trackplaceholder)
            .error(R.drawable.trackplaceholder)
            .into(binding.playlistCover)

        binding.playerNameOfThePlaylist.text = playlistViewState.nameOfThePlaylist
        binding.playlistYear.text = playlistViewState.year
        binding.duration.text = playlistViewState.duration
        binding.numberOfTheTracks.text = playlistViewState.numberOfTheTracks


    }


}