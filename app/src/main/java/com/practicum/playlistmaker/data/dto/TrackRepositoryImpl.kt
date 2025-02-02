package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private  val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(request: String): List<Track>? {
        val response: Response = networkClient.doRequest(TrackRequest(request))

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
                    previewUrl = it.previewUrl)
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