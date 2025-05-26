package com.practicum.playlistmaker.player.ui.view

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.databinding.PlaylistLineBinding

class PlayerPlaylistsHolder(
    private val binding: PlaylistLineBinding,
) : RecyclerView.ViewHolder(binding.root) {

    private val cover: ImageView = binding.playlistCover
    private val title: TextView = binding.title
    private val description: TextView = binding.number

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