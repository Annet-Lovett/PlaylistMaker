package com.practicum.playlistmaker.playlist.data.dto

import com.google.gson.annotations.SerializedName

class PlaylistDTO(@SerializedName("playlistName") val playlistName: String,
                  @SerializedName("playlistId") val playlistId: Int,
                  @SerializedName("playlistDescription") val playlistDescription: String,
                  @SerializedName("cover") val cover: String? = null,
                  @SerializedName("tracksIdList") val tracksIdList: String,
                  @SerializedName("numberTracks") val numberTracks: Int = 0, )