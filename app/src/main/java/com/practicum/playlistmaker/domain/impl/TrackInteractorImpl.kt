package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.sharing.domain.api.TrackInteractor
import com.practicum.playlistmaker.sharing.domain.api.TrackRepository

class TrackInteractorImpl(private  val repository: TrackRepository) : TrackInteractor {

    override fun searchTrack(request: String, consumer: TrackInteractor.TrackConsumer) {

        val  thread = Thread {
            try {
                consumer.consume(repository.searchTracks(request))
            }

            catch (ex: Exception) {
                consumer.consume(null)
            }

        }

        thread.start()

    }

}