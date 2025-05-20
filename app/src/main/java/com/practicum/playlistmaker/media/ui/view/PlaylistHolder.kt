package com.practicum.playlistmaker.media.ui.view

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.createPlaylist.domain.models.Playlist

class PlaylistHolder(view: View): RecyclerView.ViewHolder(view) {

    private val cover: ImageView = itemView.findViewById<ImageView>(R.id.playlist_cover)
    private val title: TextView = itemView.findViewById(R.id.nameOfThePlaylist)
    private val description: TextView = itemView.findViewById(R.id.numberOfTheTracks)

    init {
        cover.clipToOutline = true
    }

    fun bind(playlist: Playlist) {

        title.text = playlist.playlistName

        val tracksPlural = playlist.numberTracks % 10

        val single = arrayOf(0, 5, 6 ,7, 8, 9)
        val few = arrayOf(2, 3, 4)

        if (single.contains(tracksPlural)) {
            val track = itemView.context.getString(R.string.very_very_many_tracks)
            description.text = "${playlist.numberTracks} $track"
        }

        else if (tracksPlural == 1) {
            val track = itemView.context.getString(R.string.track)
            description.text = " ${playlist.numberTracks} $track"
        }

        else if (few.contains(tracksPlural)) {
            val track = itemView.context.getString(R.string.many_tracks)
            description.text = " ${playlist.numberTracks} $track"
        }

        if (playlist.cover != null) {
            cover.setImageURI(Uri.parse(playlist.cover))
        }

        else {
            cover.setImageResource(R.drawable.trackplaceholder)
        }


    }
}

