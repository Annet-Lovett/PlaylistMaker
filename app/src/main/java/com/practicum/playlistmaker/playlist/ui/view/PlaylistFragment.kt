package com.practicum.playlistmaker.playlist.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.playlist.ui.view_states.PlaylistViewState
import com.practicum.playlistmaker.playlist.ui.viewmodel.PlaylistViewModel
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

    private var _bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private val bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
        get() = _bottomSheetBehavior!!

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

        _bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistMenu.menuPlaylistBottomSheet)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistViewModel.playlistLiveData.observe(viewLifecycleOwner) {
            render(it)
            inflateOptions(it)
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

        bottomSheetBehavior.state = STATE_HIDDEN

        binding.playlistButtonDetails.setOnClickListener {
            bottomSheetBehavior.state = STATE_COLLAPSED
        }

        binding.playlistButtonShare.setOnClickListener{

            if (playlistViewModel.playlistLiveData.value!!.listOfTheTracks.isEmpty()){
                emptyTracksError()
            }
            else {
                sharePlaylist(playlistViewModel.playlistLiveData.value!!)
            }

        }

        playlistViewModel.updateState()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _bottomSheetBehavior = null
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

        tracksAdapter.updateList(playlistViewState.listOfTheTracks)

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
            .setMessage(getString(R.string.sure_to_delete_track)) // Описание диалога

            .setNegativeButton(getString(R.string.cancel)) { dialog, which -> // Добавляет кнопку «Нет»
            }
            .setPositiveButton(getString(R.string.delete)) { dialog, which -> // Добавляет кнопку «Да»
                playlistViewModel.deleteTrack(track)
            }
            .show()
    }

    private fun emptyTracksError() {

        Toast.makeText(requireContext(),
            getString(R.string.no_tracks_in_playlist),
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

        lateinit var nameNumber : String

        when (viewState.listOfTheTracks.size % 10) {
            0, 5, 6, 7, 8, 9 -> nameNumber = getString(R.string.very_very_many_tracks)
            1 -> nameNumber = getString(R.string.track)
            2, 3, 4 -> nameNumber = getString(R.string.many_tracks)
        }

        var playlistText = viewState.nameOfThePlaylist + "\n"
        playlistText += viewState.description + "\n"
        playlistText +=  viewState.numberOfTheTracks.padStart(2, '0') + " $nameNumber" + "\n"

        viewState.listOfTheTracks.forEachIndexed { index, track ->
            playlistText += "${index+1}. " + getTrackAsText(track)  + "\n"
        }

        return playlistText
    }

    private fun getTrackAsText(track: Track): String {
        val duration = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toLong())
        return "${track.artistName} - ${track.trackName} ($duration)"
    }

    private fun inflateOptions (playlistViewState: PlaylistViewState) {

        val optionsBinding = binding.playlistMenu

        Glide.with(optionsBinding.playlistCover)
            .load(playlistViewState.cover)
            .fitCenter()
            .centerCrop()
            .fallback(R.drawable.trackplaceholder)
            .error(R.drawable.trackplaceholder)
            .into(optionsBinding.playlistCover)

        optionsBinding.title.text = playlistViewState.nameOfThePlaylist
        optionsBinding.number.text = playlistViewState.numberOfTheTracks

        optionsBinding.share.setOnClickListener {

            if (playlistViewState.listOfTheTracks.isEmpty()){
                emptyTracksError()
            }
            else {
                sharePlaylist(playlistViewState)
            }
        }

        optionsBinding.editInformation.setOnClickListener {

            val args = bundleOf("id" to playlistViewModel.playlistId)

            findNavController().navigate(R.id.playlist_create, args)
        }

        optionsBinding.deletePlaylist.setOnClickListener {
            confirmDelete()
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {

                    STATE_HIDDEN -> {
                        binding.menuOverlay.visibility = View.GONE
                    }
                    else -> {
                        binding.menuOverlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                binding.menuOverlay.alpha = (slideOffset + 1)/2
            }
        })



    }

    fun confirmDelete() {

        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.are_you_sure_to_delete_playlist)) // Описание диалога

            .setNegativeButton(getString(R.string.cancel)) { dialog, which -> // Добавляет кнопку «Нет»
            }
            .setPositiveButton(getString(R.string.delete)) { dialog, which -> // Добавляет кнопку «Да»
                playlistViewModel.deletePlaylist()
                findNavController().popBackStack()
            }
            .show()
    }



}


