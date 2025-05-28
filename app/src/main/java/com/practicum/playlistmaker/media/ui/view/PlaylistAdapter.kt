package com.practicum.playlistmaker.media.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistItemBinding
import com.practicum.playlistmaker.databinding.PlaylistLineBinding
import com.practicum.playlistmaker.player.ui.view.PlayerPlaylistsHolder
import com.practicum.playlistmaker.playlist.domain.models.Playlist

class PlaylistAdapter (private val callback:(Playlist) -> Unit): RecyclerView.Adapter<PlaylistHolder>() {

    private var playlists: List<Playlist> = listOf()

    fun subList(list: List<Playlist>) {
        playlists = list
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)

        val viewHolder = PlaylistHolder(view)
        viewHolder.itemView.setOnClickListener {

            callback.invoke(playlists[viewHolder.adapterPosition])

        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        holder.bind(playlists[position])
    }
}