package com.practicum.playlistmaker.playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.practicum.playlistmaker.playlist.data.db.PlaylistTrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(playlistTrack: PlaylistTrackEntity)

}