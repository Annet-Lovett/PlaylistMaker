package com.practicum.playlistmaker.search.domain

//import com.practicum.playlistmaker.search.ui.SearchActivity.Companion.SEARCH_DEBOUNCE_DELAY
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.search.data.HistoryState
import com.practicum.playlistmaker.search.data.ScreenState
import com.practicum.playlistmaker.search.data.SearchPrefs
import com.practicum.playlistmaker.search.data.SearchScreenState
import com.practicum.playlistmaker.sharing.domain.api.TrackInteractor
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class SearchViewModel : ViewModel() {

    private val searchPrefs = SearchPrefs()

    private val screenStateLiveData = MutableLiveData(
        ScreenState(false, SearchScreenState.Initial)
    )

    private val historyState: HistoryState
        get() {
            return if (searchPrefs.getHistory().isEmpty()) {
                HistoryState.Empty
            } else {
                HistoryState.Data(searchPrefs.getHistory())
            }
        }

    private val interactor = Creator.provideTrackInteractor()

    private val debounceExecutor = Executors.newSingleThreadScheduledExecutor()
    private var debounceFuture: Future<*>? = null

    var listOfFoundTracks = emptyList<Track>()

    fun getScreenStateLiveData(): LiveData<ScreenState> = screenStateLiveData

    fun inputChange(request: String) {

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
        searchPrefs.recordTrack(track)
    }

    fun clearHistory() {

        searchPrefs.clearHistory()
        screenStateLiveData.value = screenStateLiveData.value!!.copy(
            searchState = HistoryState.Empty
        )
    }

}

