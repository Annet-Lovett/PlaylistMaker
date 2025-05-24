package com.practicum.playlistmaker.sharing.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.createPlaylist.data.db.PlaylistEntity
import com.practicum.playlistmaker.createPlaylist.data.db.PlaylistTrackEntity
import com.practicum.playlistmaker.createPlaylist.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.createPlaylist.data.db.dao.PlaylistTracksDao
import com.practicum.playlistmaker.createPlaylist.domain.models.Playlist
import com.practicum.playlistmaker.player.data.db.TrackEntity
import com.practicum.playlistmaker.player.data.db.dao.TrackDao

@Database(version = 3, entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistTracksDao(): PlaylistTracksDao



}