package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.domain.models.Track

data class ScreenState(val showClear: Boolean,
                       val searchState: SearchScreenState)

sealed interface SearchScreenState {

    object Loading: SearchScreenState

    object Error: SearchScreenState

    object Empty: SearchScreenState

//    class History: HistoryState

    data class Content(val trackList: List<Track>): SearchScreenState
}

sealed interface HistoryState: SearchScreenState {

    object Empty: HistoryState

    data class Data(val trackHistoryList: List<Track>): HistoryState
}