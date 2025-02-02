package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.api.TrackRepository

class TrackInteractorImpl(private  val repository: TrackRepository) : TrackInteractor {

    override fun searchTrack(request: String, consumer: TrackInteractor.TrackConsumer) {

        val  t = Thread {
            consumer.consume(repository.searchTracks(request))
        }

        t.start()

    }
}