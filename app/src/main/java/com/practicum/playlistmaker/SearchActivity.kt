package com.practicum.playlistmaker
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val baseTrackUrl = BASE_URL
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
    private val  trackHistoryList by lazy {
        getSearchHistory()
    }

    private val listOfTracks = ArrayList<Track>()
    private val trackAdapter = TrackListAdapter()

    private val trackHistoryAdapter = TrackListAdapter()


    private val retrofit = Retrofit.Builder()
        .baseUrl(baseTrackUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackService = retrofit.create(PlaylistApi::class.java)

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

        sharedPreferences = getSharedPreferences(KEY_FOR_SETTINGS, MODE_PRIVATE)

        recyclerTrack.adapter = trackAdapter
        trackAdapter.subList(listOfTracks)

        trackAdapter.onItemClick =  { track ->
            recordTrack(track)
        }


        trackHistoryRecycler.adapter = trackHistoryAdapter
        trackHistoryAdapter.subList(trackHistoryList)

        searchInput.addTextChangedListener(
            onTextChanged = {s: CharSequence?, _, _, _ ->
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
            searchHistoryContainer.isVisible = hasFocus && searchInput.text.isEmpty() && trackHistoryList.isNotEmpty()


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

        clearHistoryButton.setOnClickListener{
            trackHistoryList.clear()
            searchHistoryContainer.visibility = View.GONE
            saveHistory()
        }

    }

    private fun getSearchHistory(): MutableList<Track>{
        val prefsHistory = sharedPreferences.getString(KEY_FOR_HISTORY_LIST_TRACK, null)

        return if (!prefsHistory.isNullOrBlank()) {
            Gson().fromJson(prefsHistory, object : TypeToken<List<Track>>() {}.type) ?: mutableListOf()
        } else mutableListOf()
    }

    private fun enreachAndViewTracks() {

        if (inputValue.isNotEmpty()) {
            trackService.search(inputValue).enqueue(object :
                Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        listOfTracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            recyclerTrack.visibility = View.VISIBLE
                            listOfTracks.addAll(response.body()?.results!!.toList())
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (listOfTracks.isEmpty()) {
                            showMessage(NOTHING_FOUND)
                        }
                    } else {
                        recyclerTrack.visibility = View.GONE
                        showMessage(SERVER_PROBLEMS)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    recyclerTrack.visibility = View.GONE
                    showMessage(SERVER_PROBLEMS)
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
            removeIf{ it.trackId == track.trackId}
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
        const val BASE_URL = "https://itunes.apple.com"
        const val NOTHING_FOUND = "nothing_found"
        const val SERVER_PROBLEMS = "server_problems"
        const val KEY_FOR_SETTINGS = "key_for_settings"
        const val KEY_FOR_HISTORY_LIST_TRACK = "key_for_history_list_preferences"
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputValue = savedInstanceState.getString(INPUT_AMOUNT, VALUE_DEF)
    }

}