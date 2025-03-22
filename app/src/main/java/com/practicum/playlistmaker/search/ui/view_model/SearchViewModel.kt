package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.ui.view_states.HistoryState
import com.practicum.playlistmaker.search.ui.view_states.ScreenState
import com.practicum.playlistmaker.search.ui.view_states.SearchScreenState
import com.practicum.playlistmaker.sharing.domain.api.TrackInteractor
import com.practicum.playlistmaker.sharing.domain.models.Track
import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class SearchViewModel(private val interactor: TrackInteractor,
                      private val debounceExecutor: ScheduledExecutorService) : ViewModel() {

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

    private var debounceFuture: Future<*>? = null

    var listOfFoundTracks = emptyList<Track>()

    init {
        println("viewmodel init")
    }

    fun getScreenStateLiveData(): LiveData<ScreenState> = screenStateLiveData

    fun inputChange(request: String) {
        println("input change: $request")
        debounceFuture?.cancel(true)

        screenStateLiveData.value = screenStateLiveData.value!!.copy(
            showClear = request.isNotEmpty()
        )

        if (request.isEmpty()) {
            screenStateLiveData.value = screenStateLiveData.value!!.copy(
                searchState = historyState
            )
            return
        }

        debounceFuture = debounceExecutor.schedule({
            screenStateLiveData.postValue(screenStateLiveData.value!!.copy(searchState = SearchScreenState.Loading))

            interactor.searchTrack(request, object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>?) {
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
            })
        }, 1, TimeUnit.SECONDS)

    }

    fun enterSearch(request: String) {

        debounceFuture?.cancel(true) //отменяет ожидаемый запрос

        debounceFuture = debounceExecutor.submit {
            interactor.searchTrack(request, object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>?) {
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
            })
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

