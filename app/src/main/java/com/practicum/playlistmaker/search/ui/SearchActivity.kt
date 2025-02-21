package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.ComponentActivity
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.search.data.HistoryState
import com.practicum.playlistmaker.search.data.ScreenState
import com.practicum.playlistmaker.search.data.SearchScreenState
import com.practicum.playlistmaker.search.domain.SearchViewModel
import com.practicum.playlistmaker.sharing.domain.api.TrackInteractor

class SearchActivity : ComponentActivity() {

//    private lateinit var buttonBack: MaterialToolbar //TODO аналагично убрать все поля
    private lateinit var buttonClear: ImageButton
    lateinit var recyclerTrack: RecyclerView
    private lateinit var nothingFound: LinearLayout
    private lateinit var serverpromlems: LinearLayout
    private lateinit var refreshButton: Button
    private lateinit var trackHistoryRecycler: RecyclerView
    private lateinit var clearHistoryButton: Button
    private lateinit var searchHistoryContainer: LinearLayout
    private lateinit var searchProgressBar: ProgressBar
    private lateinit var binding: ActivitySearchBinding

    private var isClickAllowed = true
    private val inputValue: String get() = binding.searchInput.text.toString()

//    private fun getInputValue2 (): String {
//        return binding.searchInput.text.toString()
//    }

//    private val trackHistoryList by lazy {
//        getSearchHistory()
//    }
    private val viewModel by lazy { ViewModelProvider(this)[SearchViewModel::class] }


//  private val listOfTracks = ArrayList<Track>()
    private val trackAdapter = SearchTrackListAdapter()

    private val trackHistoryAdapter = SearchTrackListAdapter()

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        buttonBack = binding.buttonSettingsBack
        buttonClear = binding.buttonClear
        recyclerTrack = binding.recyclerTrack
        nothingFound = binding.nothingFound
        serverpromlems = binding.serverProblems
        refreshButton = binding.refreshButtonSearch
        trackHistoryRecycler = binding.historyRecycler
        clearHistoryButton = binding.clearHistoryButton
        searchHistoryContainer = binding.searchingHistoryContainer
        searchProgressBar = binding.searchProgressBar

        initInput()

        recyclerTrack.adapter = trackAdapter

//        trackAdapter.subList(listOfTracks)

        trackAdapter.onItemClick = { track ->
            viewModel.saveTrack(track)
            startPlayerActivity(track)
        }

        trackHistoryRecycler.adapter = trackHistoryAdapter
//        trackHistoryAdapter.subList(trackHistoryList)

        trackHistoryAdapter.onItemClick = { track ->
            viewModel.saveTrack(track)
            startPlayerActivity(track)
        }



        refreshButton.setOnClickListener {
//            serverpromlems.isVisible = false
//            enreachAndViewTracks()
            viewModel.enterSearch(inputValue)
//            if (viewModel.listOfFoundTracks.isNullOrEmpty()) {
//                listOfTracks.addAll(viewModel.listOfFoundTracks)
//            }
        }

        buttonClear.setOnClickListener {
            binding.searchInput.text.clear()
            recyclerTrack.isVisible = false
        }

        binding.buttonSettingsBack.setNavigationOnClickListener {
            finish()
        }

        clearHistoryButton.setOnClickListener {
//            trackHistoryList.clear()
//            searchHistoryContainer.isVisible = false
//            saveHistory()
            viewModel.clearHistory()
        }

        viewModel.getScreenStateLiveData().observe(this) {
            renderState(it)
        }
    }

    private fun renderState(screenState: ScreenState) {

        buttonClear.isVisible = screenState.showClear

        when (screenState.searchState) {
            is SearchScreenState.Initial -> {
                searchProgressBar.isVisible = false
                searchHistoryContainer.isVisible = false
                clearHistoryButton.isVisible = false
                serverpromlems.isVisible = false
                nothingFound.isVisible = false
                recyclerTrack.isVisible = false
            }

            is SearchScreenState.Content -> {
                searchProgressBar.isVisible = false
                searchHistoryContainer.isVisible = false
                clearHistoryButton.isVisible = false
                serverpromlems.isVisible = false
                nothingFound.isVisible = false
                recyclerTrack.isVisible = true
                trackAdapter.subList(screenState.searchState.trackList)
            }

            is SearchScreenState.Loading -> {
                searchProgressBar.isVisible = true
                searchHistoryContainer.isVisible = false
                clearHistoryButton.isVisible = false
                serverpromlems.isVisible = false
                nothingFound.isVisible = false
                recyclerTrack.isVisible = false
            }

            is SearchScreenState.Empty -> {
                searchProgressBar.isVisible = false
                searchHistoryContainer.isVisible = false
                clearHistoryButton.isVisible = false
                serverpromlems.isVisible = false
                nothingFound.isVisible = true
                recyclerTrack.isVisible = false
            }

            is SearchScreenState.Error -> {
                searchProgressBar.isVisible = false
                searchHistoryContainer.isVisible = false
                clearHistoryButton.isVisible = false
                serverpromlems.isVisible = true
                nothingFound.isVisible = false
                recyclerTrack.isVisible = false
            }

            is HistoryState -> {
                searchProgressBar.isVisible = false
                searchHistoryContainer.isVisible = true
                serverpromlems.isVisible = false
                nothingFound.isVisible = false
                recyclerTrack.isVisible = false

                when(screenState.searchState) {
                    is HistoryState.Empty -> {
                        clearHistoryButton.isVisible = false
                        trackHistoryAdapter.subList(listOf())
                    }
                    is HistoryState.Data -> {
                        clearHistoryButton.isVisible = true
                        trackHistoryAdapter.subList(screenState.searchState.trackHistoryList)

                    }
                }
            }

        }

    }

    private fun initInput() {
        binding.searchInput.addTextChangedListener {
            viewModel.inputChange(it.toString())
        }
      binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.enterSearch(inputValue)
            }
            true
        }

        binding.searchInput.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus) viewModel.onFocusedSearch()
        }

    }

    private fun startPlayerActivity(track: Track) {
        if (clickDebounce()) {
            val playerActivityIntent = Intent(this, PlayerActivity::class.java)
            startActivity(playerActivityIntent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


//    private fun searchDebounce() {
//        handler.removeCallbacks(searchRunnable)
//        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
//    }
//
//    private fun getSearchHistory(): MutableList<Track> {
//        val prefsHistory = sharedPreferences.getString(KEY_FOR_HISTORY_LIST_TRACK, null)
//
//        return if (!prefsHistory.isNullOrBlank()) {
//            Gson().fromJson(prefsHistory, object : TypeToken<List<Track>>() {}.type)
//                ?: mutableListOf()
//        } else mutableListOf()
//    }


//    private fun enreachAndViewTracks() {
//
//        if (inputValue.isNotEmpty()) {
//
//            searchProgressBar.isVisible = true
//            searchHistoryContainer.isVisible = false
//            clearHistoryButton.isVisible = false
//            serverpromlems.isVisible = false
//            nothingFound.isVisible = false
//            recyclerTrack.isVisible = false
//
//            val trackInteractor = Creator.provideTrackInteractor()
//
//            trackInteractor.searchTrack(inputValue, object : TrackInteractor.TrackConsumer {
//                override fun consume(foundTracks: List<Track>?) {
//                    handler.post {
//                        listOfTracks.clear()
//                        if (!foundTracks.isNullOrEmpty()) {
//                            searchProgressBar.isVisible = false
//                            recyclerTrack.isVisible = true
//                            listOfTracks.addAll(foundTracks)
//                            trackAdapter.notifyDataSetChanged()
//                        }
//                        if (foundTracks != null && foundTracks.isEmpty()) {
//                            searchProgressBar.isVisible = false
//                            recyclerTrack.isVisible = false
//                            showMessage(NOTHING_FOUND)
//                        }
//
//                        if (foundTracks == null) {
//                            searchProgressBar.isVisible = false
//                            recyclerTrack.isVisible = false
//                            showMessage(SERVER_PROBLEMS)
//                        }
//                    }
//                }
//            })
//
//        }
//    }

//    private fun showMessage(text: String) {
//        if (text == NOTHING_FOUND) {
//            nothingFound.isVisible = true
//            listOfTracks.clear()
//        }
//
//        if (text == SERVER_PROBLEMS) {
//            serverpromlems.isVisible = true
//            listOfTracks.clear()
//        }
//    }

//    private fun recordTrack(track: Track) {
//
//        trackHistoryList.apply {
//            removeIf { it.trackId == track.trackId }
//            add(0, track)
//            if (size > 10) removeLast()
//        }
//
//        saveHistory()
//
//    }

//    private fun saveHistory() {
//        sharedPreferences.edit {
//            putString(KEY_FOR_HISTORY_LIST_TRACK, Gson().toJson(trackHistoryList))
//        }
//    }

    companion object {
        const val INPUT_AMOUNT = "INPUT_AMOUNT"
        const val VALUE_DEF = ""
//        const val NOTHING_FOUND = "nothing_found"
//        const val SERVER_PROBLEMS = "server_problems"
        const val KEY_FOR_SETTINGS = "key_for_settings"
        const val KEY_FOR_HISTORY_LIST_TRACK = "key_for_history_list_preferences"
        const val KEY_FOR_CURRENT_TRACK = "key_for_current_track"
//        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}

