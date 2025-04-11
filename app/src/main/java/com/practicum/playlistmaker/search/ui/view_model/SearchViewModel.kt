package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.ui.view_states.HistoryState
import com.practicum.playlistmaker.search.ui.view_states.ScreenState
import com.practicum.playlistmaker.search.ui.view_states.SearchScreenState
import com.practicum.playlistmaker.sharing.domain.api.TrackInteractor
import com.practicum.playlistmaker.sharing.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SearchViewModel(private val interactor: TrackInteractor) : ViewModel() {

    private val screenStateLiveData = MutableLiveData(
        ScreenState(false, SearchScreenState.Initial)
    )

    private val historyState: HistoryState
        get() {
            return if (interactor.getHistory().isEmpty()) {
                HistoryState.Empty
            } else {
                HistoryState.Data(interactor.getHistory())
            }
        }

    private var debounceJob: Job? = null

    var listOfFoundTracks = emptyList<Track>()

    fun getScreenStateLiveData(): LiveData<ScreenState> = screenStateLiveData

    fun inputChange(request: String) {
        debounceJob?.cancel()

        screenStateLiveData.value = screenStateLiveData.value!!.copy(
            showClear = request.isNotEmpty()
        )

        if (request.isEmpty()) {
            screenStateLiveData.value = screenStateLiveData.value!!.copy(
                searchState = historyState
            )
            return
        }

        debounceJob = viewModelScope.launch {

            delay(TimeUnit.SECONDS.toMillis(1))

            screenStateLiveData.postValue(screenStateLiveData.value!!.copy(searchState = SearchScreenState.Loading))

            val foundTracks = interactor.searchTrack(request)

            if (!foundTracks.isNullOrEmpty()) {
                screenStateLiveData.postValue(
                    screenStateLiveData.value!!.copy(
                        searchState = SearchScreenState.Content(
                            foundTracks
                        )
                    )
                )
                listOfFoundTracks = foundTracks
            }
            if (foundTracks != null && foundTracks.isEmpty()) {
                screenStateLiveData.postValue(screenStateLiveData.value!!.copy(searchState = SearchScreenState.Empty))
            }

            if (foundTracks == null) {
                screenStateLiveData.postValue(screenStateLiveData.value!!.copy(searchState = SearchScreenState.Error))
            }
        }
    }

    fun enterSearch(request: String) {

        debounceJob?.cancel() //отменяет ожидаемый запрос

        viewModelScope.launch {

            val foundTracks = interactor.searchTrack(request)

            if (!foundTracks.isNullOrEmpty()) {
                screenStateLiveData.postValue(
                    screenStateLiveData.value!!.copy(
                        searchState = SearchScreenState.Content(
                            foundTracks
                        )
                    )
                )
                listOfFoundTracks = foundTracks
            }
            if (foundTracks != null && foundTracks.isEmpty()) {
                screenStateLiveData.postValue(screenStateLiveData.value!!.copy(searchState = SearchScreenState.Empty))
            }

            if (foundTracks == null) {
                screenStateLiveData.postValue(screenStateLiveData.value!!.copy(searchState = SearchScreenState.Error))
            }

        }

        screenStateLiveData.value =
            screenStateLiveData.value!!.copy(searchState = SearchScreenState.Loading)

    }

    fun onFocusedSearch() {

        screenStateLiveData.value =
            screenStateLiveData.value!!.copy(searchState = historyState)
    }

    fun saveTrack(track: Track) {
        interactor.recordTrack(track)
    }

    fun clearHistory() {

        interactor.clearHistory()
        screenStateLiveData.value = screenStateLiveData.value!!.copy(
            searchState = HistoryState.Empty
        )
    }

}

