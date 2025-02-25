package com.practicum.playlistmaker.search.ui.view_states

import com.practicum.playlistmaker.sharing.domain.models.Track

data class ScreenState(val showClear: Boolean,
                       val searchState: SearchScreenState
)

sealed interface SearchScreenState {

    object  Initial: SearchScreenState

    object Loading: SearchScreenState

    object Error: SearchScreenState

    object Empty: SearchScreenState

    data class Content(val trackList: List<Track>): SearchScreenState
}

sealed interface HistoryState: SearchScreenState {

    object Empty: HistoryState

    data class Data(val trackHistoryList: List<Track>): HistoryState
}