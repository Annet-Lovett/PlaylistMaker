package com.practicum.playlistmaker.playlist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.playlist.data.db.PlaylistTracksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(playlistTrack: PlaylistTracksEntity)

    @Query("SELECT * FROM playlist_tracks_table")
    fun getAllTracksFlow(): Flow<List<PlaylistTracksEntity>>

    @Query("SELECT * FROM playlist_tracks_table")
    suspend fun getAllTracks(): List<PlaylistTracksEntity>

}