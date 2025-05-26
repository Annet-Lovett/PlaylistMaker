package com.practicum.playlistmaker.player.ui.view

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.databinding.ScreenPlayerBinding
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.player.ui.view_states.PlayerState
import com.practicum.playlistmaker.player.ui.view_states.TrackState
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.getValue

class PlayerFragment: Fragment() {

    private val playerViewModel by viewModel<PlayerViewModel>()

    private var _binding_wrap: FragmentPlayerBinding? = null
    private val binding_wrap: FragmentPlayerBinding
        get() = _binding_wrap!!

    private var _binding: ScreenPlayerBinding? = null
    private val binding: ScreenPlayerBinding
        get() = _binding!!

    private val playlistsAdapter = PlayerPlaylistsAdapter({playerViewModel.addTrackToPlaylist(it)})

    private var _bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null
    private val bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
        get() = _bottomSheetBehavior!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding_wrap = FragmentPlayerBinding.inflate(layoutInflater)
        _binding = binding_wrap.player
        _bottomSheetBehavior = BottomSheetBehavior.from(binding_wrap.playlistsBottomSheet)
        return binding_wrap.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding_wrap.playlists.adapter = playlistsAdapter

        binding.playerButtonPlay.setOnClickListener {
            playerViewModel.toggle()
        }

        binding.playerBackButton.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.playerButtonLike.setOnClickListener{
            playerViewModel.onFavoriteClicked()
        }

        playerViewModel.playerLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        binding_wrap.newPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.playlist_create)
        }


        playerViewModel.playlistsLiveData.observe(viewLifecycleOwner) {

            playlistsAdapter.subList(it)

        }

        binding.playerButtonPlus.setOnClickListener {

            bottomSheetBehavior.apply {
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }

        }

        configBottomSheet()

        viewLifecycleOwner.lifecycleScope.launch {

            playerViewModel.eventChannel
                .receiveAsFlow()
                .flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect {

                    val messageSucsess = "Добавлен в плейлист ${it.first.playlistName}"
                    val messageFail = "Трек уже добавлен ${it.first.playlistName}"

                    if (it.second) {
                        Toast.makeText(requireContext(), messageSucsess, Toast.LENGTH_SHORT).show()
                        bottomSheetBehavior.state = STATE_HIDDEN
                    }

                    else {
                        Toast.makeText(requireContext(), messageFail, Toast.LENGTH_SHORT).show()
                    }

                }
        }

    }

    private fun configBottomSheet() {

        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding_wrap.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding_wrap.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                binding_wrap.overlay.alpha = (slideOffset + 1)/2
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _binding_wrap = null
        _bottomSheetBehavior = null
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    companion object {
        const val KEY_FOR_CURRENT_TRACK = "key_for_current_track"
        const val DURATION_FORMAT = "mm:ss"
        const val START_TIME = "00:00"
    }

    private fun dpToPx(dp: Float, context: View): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }


    private fun render(playerState: PlayerState) {
        when (playerState) {
            is PlayerState.Initial -> {
                binding.playerDurationOfTheTrackNearThePlay.text = START_TIME
            }

            is TrackState -> {

                Glide.with(binding.playerTrackImg)
                    .load(playerState.track.artworkUrl100.replaceAfterLast("/", "512x512bb.jpg"))
                    .fitCenter()
                    .centerCrop()
                    .transform(RoundedCorners(dpToPx(8f, binding.playerTrackImg)))
                    .fallback(R.drawable.trackplaceholder)
                    .error(R.drawable.trackplaceholder)
                    .into(binding.playerTrackImg)

                binding.playerNameOfTheTrack.text = playerState.track.trackName
                binding.playerNameOfTheArtist.text = playerState.track.artistName
                binding.playerYearNumber.text = playerState.track.releaseDate.substring(0, 4)
                binding.playerGenreName.text = playerState.track.primaryGenreName
                binding.playerCountryName.text = playerState.track.country
                binding.playerDurationOfTheTrackNumbers.text = SimpleDateFormat(
                    DURATION_FORMAT,
                    Locale.getDefault()
                ).format(playerState.track.trackTimeMillis.toLong())


                if (playerState.track.collectionName != null) {
                    binding.playerAlbumName.text = playerState.track.collectionName
                    binding.playerAlbumText.isVisible = true
                    binding.playerAlbumName.isVisible = true
                } else {
                    binding.playerAlbumText.isVisible = false
                    binding.playerAlbumName.isVisible = false
                }

                binding.playerDurationOfTheTrackNearThePlay.text = playerState.progress

                if (playerState.isPlaying) {
                    binding.playerButtonPlay.setBackgroundResource(R.drawable.pause_light)


                }

                else {
                    binding.playerButtonPlay.setBackgroundResource(R.drawable.button_play)
                }

                if (playerState.isFavourite) {
                    binding.playerButtonLike.setImageResource(R.drawable.like_red)
                }

                else {
                    binding.playerButtonLike.setImageResource(R.drawable.playlist_like_button)
                }

            }


        }
    }

}