package com.practicum.playlistmaker.playlist.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_tracks_table")
data class PlaylistTracksEntity(
    @PrimaryKey (autoGenerate = true)
    val trackId: Int,
    val artworkUrl100: String,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: String,
    val previewUrl: String,
    val addedAt : Long = System.currentTimeMillis()
)