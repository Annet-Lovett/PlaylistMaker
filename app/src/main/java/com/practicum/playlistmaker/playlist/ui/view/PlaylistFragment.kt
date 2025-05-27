package com.practicum.playlistmaker.playlist.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.playlist.ui.view_states.PlaylistViewState
import com.practicum.playlistmaker.playlist.ui.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.search.ui.view.SearchTrackListAdapter
import com.practicum.playlistmaker.sharing.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding
        get() = _binding!!

    private val playlistViewModel: PlaylistViewModel by viewModel{
        val playlistId = requireArguments().getInt("id")
        parametersOf(playlistId)
    }

    private val tracksAdapter = SearchTrackListAdapter()

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

        playlistViewModel.playlistLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        tracksAdapter.onItemClick = { track ->
            playlistViewModel.selectTrack(track)
            findNavController().navigate(R.id.player)
        }

        binding.playlists.adapter = tracksAdapter

        binding.playerBackButton.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

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
        binding.playlistDescription.text = playlistViewState.description
        binding.duration.text = playlistViewState.duration
        binding.numberOfTheTracks.text = playlistViewState.numberOfTheTracks

        tracksAdapter.subList(playlistViewState.listOfTheTracks)

        val _bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)

        if (playlistViewState.listOfTheTracks.isEmpty()) {
            _bottomSheetBehavior.isHideable = true
        }

        else {
            _bottomSheetBehavior.isHideable = false
        }



    }


}
