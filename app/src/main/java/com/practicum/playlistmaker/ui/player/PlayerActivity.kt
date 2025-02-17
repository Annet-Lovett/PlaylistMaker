package com.practicum.playlistmaker.ui.player


import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.properties.Delegates

class PlayerActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var buttonBack: MaterialToolbar
    private lateinit var trackImage: ImageView
    private lateinit var nameOfTheTrack: TextView
    private lateinit var nameOfTheArtist: TextView
    private lateinit var currentDuration: TextView
    private lateinit var durationOfTheTrack: TextView
    private lateinit var nameOfTheAlbumText: TextView
    private lateinit var nameOfTheAlbum: TextView
    private lateinit var yearOfTheTrack: TextView
    private lateinit var genreOfTheTrack: TextView
    private lateinit var countryOfTheTrack: TextView
    private lateinit var likeButton: Button
    private lateinit var addButton: Button
    private lateinit var playRunnable: Runnable
    private var playerState by Delegates.notNull<Int>()
    private var url: String = ""
    private var mediaPlayer = MediaPlayer()

    private val dateFormat by lazy { SimpleDateFormat(DURATION_FORMAT, Locale.getDefault())}


    private lateinit var playButton: Button

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        sharedPreferences = getSharedPreferences(KEY_FOR_SETTINGS, MODE_PRIVATE)

        val newTrack = sharedPreferences.getString(KEY_FOR_CURRENT_TRACK, "")
            ?.let { createFactFromJson(it) }

        buttonBack = findViewById(R.id.playerBackButton)
        trackImage = findViewById(R.id.playerTrackImg)
        nameOfTheTrack = findViewById(R.id.playerNameOfTheTrack)
        nameOfTheArtist = findViewById(R.id.playerNameOfTheArtist)
        currentDuration = findViewById(R.id.playerDurationOfTheTrackNearThePlay)
        durationOfTheTrack = findViewById(R.id.playerDurationOfTheTrackNumbers)
        nameOfTheAlbumText = findViewById(R.id.playerAlbumText)
        nameOfTheAlbum = findViewById(R.id.playerAlbumName)
        yearOfTheTrack = findViewById(R.id.playerYearNumber)
        genreOfTheTrack = findViewById(R.id.playerGenreName)
        countryOfTheTrack = findViewById(R.id.playerCountryName)
        likeButton = findViewById(R.id.playerButtonLike)
        addButton = findViewById(R.id.playerButtonPlus)
        playButton = findViewById(R.id.playerButtonPlay)

        playerState = STATE_DEFAULT

        if (newTrack != null) {

            url = newTrack.previewUrl

            Glide.with(trackImage)
                .load(newTrack.artworkUrl100.replaceAfterLast("/", "512x512bb.jpg"))
                .fitCenter()
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8f, trackImage)))
                .fallback(R.drawable.trackplaceholder)
                .error(R.drawable.trackplaceholder)
                .into(trackImage)

            nameOfTheTrack.text = newTrack.trackName
            nameOfTheArtist.text = newTrack.artistName
            durationOfTheTrack.text = SimpleDateFormat(
                DURATION_FORMAT,
                Locale.getDefault()
            ).format(newTrack.trackTimeMillis.toLong())

            yearOfTheTrack.text = newTrack.releaseDate.substring(0, 4)
            genreOfTheTrack.text = newTrack.primaryGenreName
            countryOfTheTrack.text = newTrack.country
            currentDuration.text = START_TIME


            if (newTrack.collectionName != null) {
                nameOfTheAlbum.text = newTrack.collectionName
                nameOfTheAlbumText.visibility = View.VISIBLE
                nameOfTheAlbum.visibility = View.VISIBLE
            } else {
                nameOfTheAlbumText.visibility = View.GONE
                nameOfTheAlbum.visibility = View.GONE
            }

        }

        preparePlayer(mediaPlayer, playButton)


        playRunnable = Runnable {
            if (newTrack != null && playerState == STATE_PLAYING) {
                autoupdateDurationCallback(mediaPlayer, currentDuration)
                handler.postDelayed(this.playRunnable, REFRESHING_DURATION_TIME)
            }
        }


        playButton.setOnClickListener {
            playbackControl(playerState, playButton)
        }


        mediaPlayer.setOnCompletionListener {
            handler.removeCallbacks(playRunnable)
            currentDuration.text = START_TIME
            playButton.setBackgroundResource(R.drawable.button_play)
        }

        buttonBack.setOnClickListener {
            finish()
        }

    }

    override fun onPause() {
        super.onPause()
        pausePlayer(playButton)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(playRunnable)
    }

    private fun playbackControl(playerState: Int, play: Button) {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer(play)
                handler.removeCallbacks(playRunnable)
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer(play)
                runAutoupdateDurationView()
            }
        }
    }

    private fun runAutoupdateDurationView() {
        handler.removeCallbacks(playRunnable)
        handler.postDelayed(playRunnable, 300)
    }

    private fun preparePlayer(mediaPlayer: MediaPlayer, play: Button) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer(play: Button) {
        mediaPlayer.start()
        play.setBackgroundResource(R.drawable.pause_light)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer(play: Button) {
        mediaPlayer.pause()
        play.setBackgroundResource(R.drawable.button_play)
        playerState = STATE_PAUSED
    }

    private fun autoupdateDurationCallback(player: MediaPlayer, durationView: TextView) {
        mediaPlayer = player
        durationView.text = dateFormat.format(mediaPlayer.currentPosition)
    }


    private fun createFactFromJson(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
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
}