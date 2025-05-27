package com.practicum.playlistmaker.playlist.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.playlist.ui.view_states.PlaylistViewState
import com.practicum.playlistmaker.playlist.ui.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.search.ui.view.SearchTrackListAdapter
import com.practicum.playlistmaker.sharing.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding: FragmentPlaylistBinding
        get() = _binding!!

    private val playlistViewModel: PlaylistViewModel by viewModel{
        val playlistId = requireArguments().getInt("id")
        parametersOf(playlistId)
    }

    private val tracksAdapter = PlaylistTracksAdapter()

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

        tracksAdapter.onItemHold = { track ->
            confirmDelete(track)
        }

        binding.playlists.adapter = tracksAdapter

        binding.playerBackButton.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.playlistButtonShare.setOnClickListener{

            if (playlistViewModel.playlistLiveData.value!!.listOfTheTracks.isEmpty()){
                emptyTracksError()
            }
            else {
                sharePlaylist(playlistViewModel.playlistLiveData.value!!)
            }

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


    private fun confirmDelete(track: Track) {

        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Вы уверены, что хотите удалить трек из плейлиста?") // Описание диалога

            .setNegativeButton("Отмена") { dialog, which -> // Добавляет кнопку «Нет»
            }
            .setPositiveButton("Удалить") { dialog, which -> // Добавляет кнопку «Да»
                playlistViewModel.deleteTrack(track)
            }
            .show()
    }

    private fun emptyTracksError() {

        Toast.makeText(requireContext(),
            "В этом плейлисте нет списка треков, которым можно поделиться",
            Toast.LENGTH_SHORT).show()
    }

    private fun sharePlaylist(viewState: PlaylistViewState) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getPlaylistAsText(viewState))
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun getPlaylistAsText(viewState: PlaylistViewState): String {
        var playlistText = viewState.nameOfThePlaylist + "\n"
        playlistText += viewState.description + "\n"
        playlistText +=  viewState.numberOfTheTracks.padStart(2, '0') + " треков" + "\n"

        viewState.listOfTheTracks.forEachIndexed { index, track ->
            playlistText += "${index+1}. " + getTrackAsText(track)  + "\n"
        }

        return playlistText
    }

    private fun getTrackAsText(track: Track): String {
        val duration = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
        return "${track.artistName} - ${track.trackName} ($duration)"
    }

}

