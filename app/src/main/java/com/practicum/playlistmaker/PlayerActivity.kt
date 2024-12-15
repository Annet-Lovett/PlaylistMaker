package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var buttonBack :com.google.android.material.appbar.MaterialToolbar
    private lateinit var trackImage :ImageView
    private lateinit var nameOfTheTrack :TextView
    private lateinit var nameOfTheArtist :TextView
    private lateinit var currentDuration :TextView
    private lateinit var durationOfTheTrack :TextView
    private  lateinit var nameOfTheAlbumText :TextView
    private lateinit var nameOfTheAlbum :TextView
    private lateinit var yearOfTheTrack :TextView
    private lateinit var genreOfTheTrack:TextView
    private lateinit var countryOfTheTrack :TextView
    private lateinit var likeButton :Button
    private lateinit var addButton :Button

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


        if (newTrack != null) {
            Glide.with(trackImage)
                .load(newTrack.artworkUrl100.replaceAfterLast("/", "512x512bb.jpg" ))
                .transform(RoundedCorners(dpToPx(2f, trackImage)))
                .fallback(R.drawable.trackplaceholder)
                .error(R.drawable.trackplaceholder)
                .fitCenter()
                .centerCrop()
                .into(trackImage)

            nameOfTheTrack.text = newTrack.trackName
            nameOfTheArtist.text = newTrack.artistName
            currentDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(newTrack.trackTimeMillis.toLong())
            durationOfTheTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(newTrack.trackTimeMillis.toLong())

            yearOfTheTrack.text = newTrack.releaseDate.substring(0, 4)
            genreOfTheTrack.text = newTrack.primaryGenreName
            countryOfTheTrack.text = newTrack.country

            if (newTrack.collectionName != null) {
                nameOfTheAlbum.text = newTrack.collectionName
                nameOfTheAlbumText.visibility = View.VISIBLE
                nameOfTheAlbum.visibility = View.VISIBLE
            }
            else {
                nameOfTheAlbumText.visibility = View.GONE
                nameOfTheAlbum.visibility = View.GONE
            }

        }

        buttonBack.setOnClickListener{
            finish()
        }

    }

    private fun createFactFromJson(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
    }

    fun dpToPx(dp: Float, context: View): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }

    companion object {
        const val KEY_FOR_SETTINGS = "key_for_settings"
        const val KEY_FOR_CURRENT_TRACK = "key_for_current_track"
    }


}