package com.practicum.playlistmaker.player.ui


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.data.PlayerState
import com.practicum.playlistmaker.player.data.TrackState
import com.practicum.playlistmaker.player.domain.PlayerViewModel
import java.text.FieldPosition
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

  private lateinit var playRunnable: Runnable

  private val handler = Handler(Looper.getMainLooper())


    private val viewModel by lazy { ViewModelProvider(this)[PlayerViewModel::class] }


private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.playerButtonPlay.setOnClickListener {
            viewModel.toggle()
        }

        playRunnable = Runnable {
                handler.post{changeCurrentPosition(viewModel.getCurrentPosition())}
                handler.postDelayed(this.playRunnable, REFRESHING_DURATION_TIME)
        }


        binding.playerBackButton.setOnClickListener {
            finish()
        }

        viewModel.getScreenStateLiveData().observe(this) {
            render(it)
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun runAutoupdateDurationView() {
        handler.removeCallbacks(playRunnable)
        handler.postDelayed(playRunnable, 300)
    }

    private fun changeCurrentPosition (position: String) {
        binding.playerDurationOfTheTrackNearThePlay.text = position
    }

    private fun dpToPx(dp: Float, context: View): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }

    companion object {
        const val KEY_FOR_SETTINGS = "key_for_settings"
        const val KEY_FOR_CURRENT_TRACK = "key_for_current_track"
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val DURATION_FORMAT = "mm:ss"
        const val REFRESHING_DURATION_TIME = 300L
        const val START_TIME = "00:00"

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

                if (playerState.isPlaying) {
                    binding.playerButtonPlay.setBackgroundResource(R.drawable.pause_light)
                    handler.post{
                        runAutoupdateDurationView()
                    }



                } else {
                    binding.playerButtonPlay.setBackgroundResource(R.drawable.button_play)
                }

            }


        }
    }
}

