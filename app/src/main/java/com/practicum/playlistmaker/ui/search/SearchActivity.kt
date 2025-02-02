package com.practicum.playlistmaker.ui.search

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.ui.player.PlayerActivity

class SearchActivity : AppCompatActivity() {

    private var inputValue: String = VALUE_DEF

    private lateinit var buttonBack: MaterialToolbar
    private lateinit var buttonClear: ImageButton
    private lateinit var searchInput: EditText
    lateinit var recyclerTrack: RecyclerView
    private lateinit var nothingFound: LinearLayout
    private lateinit var serverpromlems: LinearLayout
    private lateinit var refreshButton: Button
    private lateinit var trackHistoryRecycler: RecyclerView
    private lateinit var clearHistoryButton: Button
    private lateinit var searchHistoryContainer: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchProgressBar: ProgressBar

    private var isClickAllowed = true

    private val trackHistoryList by lazy {
        getSearchHistory()
    }

    private val listOfTracks = ArrayList<Track>()
    private val trackAdapter = SearchTrackListAdapter()

    private val trackHistoryAdapter = SearchTrackListAdapter()

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        buttonBack = findViewById(R.id.buttonSettingsBack)
        buttonClear = findViewById(R.id.buttonClear)
        searchInput = findViewById(R.id.searchInput)
        recyclerTrack = findViewById(R.id.recyclerTrack)
        nothingFound = findViewById(R.id.nothingFound)
        serverpromlems = findViewById(R.id.serverProblems)
        refreshButton = findViewById(R.id.refreshButtonSearch)
        trackHistoryRecycler = findViewById(R.id.historyRecycler)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        searchHistoryContainer = findViewById(R.id.searchingHistoryContainer)
        searchProgressBar = findViewById(R.id.searchProgressBar)

        sharedPreferences = getSharedPreferences(KEY_FOR_SETTINGS, MODE_PRIVATE)

        recyclerTrack.adapter = trackAdapter
        trackAdapter.subList(listOfTracks)

        trackAdapter.onItemClick = { track ->
            recordTrack(track)
            startPlayerActivity(track)
        }

        trackHistoryRecycler.adapter = trackHistoryAdapter
        trackHistoryAdapter.subList(trackHistoryList)

        trackHistoryAdapter.onItemClick = { track ->
            startPlayerActivity(track)
        }

        searchInput.addTextChangedListener(
            onTextChanged = { s: CharSequence?, _, _, _ ->
                searchDebounce()
                buttonClear.isVisible = !s.isNullOrEmpty()
                inputValue = s.toString()
            })

        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                nothingFound.visibility = View.GONE
                enreachAndViewTracks()
            }
            false
        }

        searchInput.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryContainer.isVisible =
                hasFocus && searchInput.text.isEmpty() && trackHistoryList.isNotEmpty()
        }

        refreshButton.setOnClickListener {
            serverpromlems.visibility = View.GONE
            enreachAndViewTracks()
        }

        buttonClear.setOnClickListener {
            searchInput.text.clear()
            recyclerTrack.visibility = View.GONE
        }

        buttonBack.setNavigationOnClickListener {
            finish()
        }

        clearHistoryButton.setOnClickListener {
            trackHistoryList.clear()
            searchHistoryContainer.visibility = View.GONE
            saveHistory()
        }
    }

    private fun startPlayerActivity(track: Track) {
        if (clickDebounce()) {
            sharedPreferences.edit()
                .putString(KEY_FOR_CURRENT_TRACK, Gson().toJson(track))
                .apply()
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

    private val searchRunnable = Runnable { enreachAndViewTracks() }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun getSearchHistory(): MutableList<Track> {
        val prefsHistory = sharedPreferences.getString(KEY_FOR_HISTORY_LIST_TRACK, null)

        return if (!prefsHistory.isNullOrBlank()) {
            Gson().fromJson(prefsHistory, object : TypeToken<List<Track>>() {}.type)
                ?: mutableListOf()
        } else mutableListOf()
    }


    private fun enreachAndViewTracks() {

        if (inputValue.isNotEmpty()) {

            searchProgressBar.visibility = View.VISIBLE
            searchHistoryContainer.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE
            serverpromlems.visibility = View.GONE
            nothingFound.visibility = View.GONE
            recyclerTrack.visibility = View.GONE

            val trackInteractor = Creator.provideTrackInteractor()

            trackInteractor.searchTrack(inputValue, object: TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>?) {
                    handler.post {
                        listOfTracks.clear()
                        if (!foundTracks.isNullOrEmpty()) {
                            searchProgressBar.visibility = View.GONE
                            recyclerTrack.visibility = View.VISIBLE
                            listOfTracks.addAll(foundTracks)
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (foundTracks != null && foundTracks.isEmpty() ) {
                            searchProgressBar.visibility = View.GONE
                            recyclerTrack.visibility = View.GONE
                            showMessage(NOTHING_FOUND)
                        }

                        if (foundTracks == null) {
                            searchProgressBar.visibility = View.GONE
                            recyclerTrack.visibility = View.GONE
                            showMessage(SERVER_PROBLEMS)
                        }
                    }
                }
            })

        }
    }

    private fun showMessage(text: String) {
        if (text == NOTHING_FOUND) {
            nothingFound.visibility = View.VISIBLE
            listOfTracks.clear()
        }

        if (text == SERVER_PROBLEMS) {
            serverpromlems.visibility = View.VISIBLE
            listOfTracks.clear()
        }
    }

    private fun recordTrack(track: Track) {

        trackHistoryList.apply {
            removeIf { it.trackId == track.trackId }
            add(0, track)
            if (size > 10) removeLast()
        }

        saveHistory()

    }

    private fun saveHistory() {
        sharedPreferences.edit {
            putString(KEY_FOR_HISTORY_LIST_TRACK, Gson().toJson(trackHistoryList))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_AMOUNT, inputValue)
    }

    companion object {
        const val INPUT_AMOUNT = "INPUT_AMOUNT"
        const val VALUE_DEF = ""
        const val NOTHING_FOUND = "nothing_found"
        const val SERVER_PROBLEMS = "server_problems"
        const val KEY_FOR_SETTINGS = "key_for_settings"
        const val KEY_FOR_HISTORY_LIST_TRACK = "key_for_history_list_preferences"
        const val KEY_FOR_CURRENT_TRACK = "key_for_current_track"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputValue = savedInstanceState.getString(INPUT_AMOUNT, VALUE_DEF)
    }

}

