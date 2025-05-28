package com.practicum.playlistmaker.sharing.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.playlist.data.db.PlaylistEntity
import com.practicum.playlistmaker.playlist.data.db.PlaylistTracksEntity
import com.practicum.playlistmaker.playlist.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.playlist.data.db.dao.PlaylistTracksDao
import com.practicum.playlistmaker.player.data.db.TrackFavouriteEntity
import com.practicum.playlistmaker.player.data.db.dao.TrackFavouriteDao

@Database(version = 3, entities = [TrackFavouriteEntity::class, PlaylistEntity::class, PlaylistTracksEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackFavouriteDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistTracksDao(): PlaylistTracksDao



}