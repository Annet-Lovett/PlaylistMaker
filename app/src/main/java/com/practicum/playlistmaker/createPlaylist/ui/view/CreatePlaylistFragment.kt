package com.practicum.playlistmaker.createPlaylist.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.createPlaylist.ui.view_model.CreatePlaylistViewModel
import com.practicum.playlistmaker.createPlaylist.ui.view_state.CreatePlaylistViewState
import com.practicum.playlistmaker.databinding.FragmentPlaylistCreateBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class CreatePlaylistFragment: Fragment(R.layout.fragment_playlist_create) {

    private var _binding: FragmentPlaylistCreateBinding? = null
    private val binding: FragmentPlaylistCreateBinding
        get() = _binding!!

    private val createPlaylistViewModel: CreatePlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaylistCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        createPlaylistViewModel.createPlaylistLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        binding.playlistNameField.addTextChangedListener {
            createPlaylistViewModel.onNameChanged(it.toString())
        }

    }

    fun render (state: CreatePlaylistViewState) {

        binding.playlistCreate.isEnabled = state.ready

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}