package com.practicum.playlistmaker.sharing.domain.models

data class Track(val trackName: String,
                 val artistName: String,
                 val trackTimeMillis: String,
                 val artworkUrl100: String,
                 val trackId: Int,
                 val collectionName: String,
                 val releaseDate: String,
                 val primaryGenreName: String,
                 val country: String,
                 val previewUrl: String,
                var isFavourite: Boolean,) {

    override fun equals(other: Any?): Boolean {
        return other is Track &&  trackName == other.trackName &&
         artistName == other.artistName &&
         trackTimeMillis == other.trackTimeMillis &&
         artworkUrl100 == other.artworkUrl100 &&
         trackId == other.trackId &&
         collectionName == other.collectionName &&
         releaseDate == other.releaseDate &&
         primaryGenreName == other.primaryGenreName &&
         country == other.country &&
         previewUrl == other.previewUrl

    }
}
