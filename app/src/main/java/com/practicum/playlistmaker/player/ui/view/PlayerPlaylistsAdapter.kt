package com.practicum.playlistmaker.player.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.createPlaylist.domain.models.Playlist
import com.practicum.playlistmaker.databinding.PlaylistLineBinding

class PlayerPlaylistsAdapter(): RecyclerView.Adapter<PlayerPlaylistsHolder>() {

    private var playlists: List<Playlist> = listOf()

    var onItemClick:((playlist: Playlist) -> Unit)? = null

    fun subList(list: List<Playlist>) {
        playlists = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerPlaylistsHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlayerPlaylistsHolder(PlaylistLineBinding.inflate(layoutInspector, parent, false), onItemClick)
    }

    override fun onBindViewHolder(
        holder: PlayerPlaylistsHolder,
        position: Int
    ) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener {
            onItemClick?.let {it(playlist)}
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}