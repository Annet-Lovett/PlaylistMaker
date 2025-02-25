package com.practicum.playlistmaker.sharing.data.dto

import com.google.gson.annotations.SerializedName

class TrackDto(@SerializedName("trackName") val trackName: String,
               @SerializedName("artistName") val artistName: String,
               @SerializedName("trackTimeMillis") val trackTimeMillis: Long,
               @SerializedName("artworkUrl100") val artworkUrl100: String,
               @SerializedName("trackId") val trackId: Int,
               @SerializedName("collectionName") val collectionName: String,
               @SerializedName("releaseDate") val releaseDate: String,
               @SerializedName("primaryGenreName") val primaryGenreName: String,
               @SerializedName("country") val country: String,
               @SerializedName("previewUrl") val previewUrl: String)