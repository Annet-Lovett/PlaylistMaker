package com.practicum.playlistmaker.playlist.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practicum.playlistmaker.sharing.domain.models.Track

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey (autoGenerate = true)
    val playlistId: Int,
    val playlistName: String,
    val playlistDescription: String,
    val cover: String?,
    val tracksIdList: String,
    val numberTracks: Int,
)