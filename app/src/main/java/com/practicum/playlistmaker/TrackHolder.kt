package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackHolder(item: View): RecyclerView.ViewHolder(item) {

    private val fullTrack = item.findViewById<LinearLayout>(R.id.fullTrack)
    private val trackImage = item.findViewById<ImageView>(R.id.trackImage)
    private val nameOfTheTrack = item.findViewById<TextView>(R.id.nameOfTheTrack)
    private val nameOfTheArtist = item.findViewById<TextView>(R.id.nameOfTheArtist)
    private val durationOfTheTrack = item.findViewById<TextView>(R.id.durationOfTheTrack)

    fun bind (track: Track) {
        val imgUrl = track.artworkUrl100
        val trackName = track.trackName
        val duration = track.trackTime
        val artist = track.artistName

        Glide.with(itemView)
            .load(imgUrl)
            .into(trackImage)

        nameOfTheTrack.text = trackName
        nameOfTheArtist.text = artist
        durationOfTheTrack.text = duration

    }

}