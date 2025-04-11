package com.practicum.playlistmaker.sharing.data.network

import com.practicum.playlistmaker.sharing.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaylistApi {

    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String)
            : TrackResponse

}