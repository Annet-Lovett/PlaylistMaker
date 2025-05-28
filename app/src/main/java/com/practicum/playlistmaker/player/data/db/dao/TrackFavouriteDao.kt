package com.practicum.playlistmaker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.player.data.db.TrackFavouriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackFavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackFavouriteEntity)

    @Delete
    suspend fun deleteTrack(track: TrackFavouriteEntity)

    @Query("SELECT * FROM track_table ORDER BY addedAt DESC")
    fun getTracks(): Flow<List<TrackFavouriteEntity>>

//    @Query("SELECT * FROM track_table")
//    suspend fun getTracksId(): List<Int>
}