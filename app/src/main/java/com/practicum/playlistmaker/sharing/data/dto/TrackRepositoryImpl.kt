package com.practicum.playlistmaker.sharing.data.dto

import com.practicum.playlistmaker.sharing.data.NetworkClient
import com.practicum.playlistmaker.sharing.domain.api.TrackRepository
import com.practicum.playlistmaker.sharing.domain.models.Track

class TrackRepositoryImpl(private  val networkClient: NetworkClient) : TrackRepository {

    override suspend fun searchTracks(request: String): List<Track>? {
        val response = networkClient.doRequest(TrackRequest(request))

            if (response.resultCode == 200 && (response as TrackResponse).results.isNotEmpty()) {

                return response.results.map {
                    Track(trackName = it.trackName,
                    artistName = it.artistName,
                    trackTimeMillis = it.trackTimeMillis.toString(),
                    artworkUrl100 = it.artworkUrl100,
                    trackId = it.trackId,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = it.previewUrl,
                    isFavourite = false)
                }
            }

            if (response.resultCode == 200 && (response as TrackResponse).results.isEmpty()) {

                return emptyList()
            }

            else {
                return null
            }

    }
}