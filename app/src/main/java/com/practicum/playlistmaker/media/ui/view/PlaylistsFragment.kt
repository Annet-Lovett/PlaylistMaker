package com.practicum.playlistmaker.media.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.databinding.FragmentPlaylistsMediaBinding
import com.practicum.playlistmaker.media.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.playlist.ui.view.PlaylistFragment
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get


class PlaylistsFragment : Fragment() {

    companion object {

        fun newInstance() = PlaylistsFragment()
    }

    private lateinit var binding: FragmentPlaylistsMediaBinding

    private val playlistViewModel: PlaylistViewModel by viewModels()

    private val playlistAdapter = PlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlaylistsMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.playlist_create)
        }

        viewLifecycleOwner.lifecycleScope.launch{
//            playlistViewModel.flow
            val interactor: PlaylistInteractor= this@PlaylistsFragment.get()
            interactor.getAllPlaylists()
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { render(it) }
        }

        val recyclerView = binding.playlistsRecyclerView

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = playlistAdapter

    }

    private fun render(playlists: List<Playlist>) {

        playlistAdapter.subList(playlists)

        if (playlists.isEmpty()) {
            binding.nothingFoundFragment.visibility = View.VISIBLE
            binding.playlistsRecyclerView.visibility = View.GONE
        }

        else {
            binding.nothingFoundFragment.visibility = View.GONE
            binding.playlistsRecyclerView.visibility = View.VISIBLE
        }
    }

}