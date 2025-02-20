package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.domain.models.Track

data class ScreenState(val showClear: Boolean,
                       val searchState: SearchScreenState)

sealed interface SearchScreenState {

    object Loading: SearchScreenState

    object Error: SearchScreenState

    object Empty: SearchScreenState

    object History: SearchScreenState

    data class Content(val trackList: List<Track>): SearchScreenState
}