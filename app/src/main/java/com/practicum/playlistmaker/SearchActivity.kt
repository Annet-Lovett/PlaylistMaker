package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val baseTrackUrl = BASE_URL
    private var inputValue: String = VALUE_DEF

    lateinit var buttonBack: MaterialToolbar
    lateinit var buttonClear: ImageButton
    lateinit var searchInput: EditText
    lateinit var recyclerTrack: RecyclerView
    lateinit var nothingFound: LinearLayout
    lateinit var serverpromlems: LinearLayout
    lateinit var refreshButton: Button

    private val listOfTracks = ArrayList<Track>()
    private val trackAdapter = TrackListAdapter()

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

        recyclerTrack.adapter = trackAdapter

        trackAdapter.listOfTheTracks = listOfTracks

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

        refreshButton.setOnClickListener {
            serverpromlems.visibility = View.GONE
            enreachAndViewTracks()

        }

        buttonClear.setOnClickListener {
            searchInput.text.clear()
            recyclerTrack.visibility = View.GONE
        }

        buttonBack.setNavigationOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
            finish()
        }

    }

    private fun enreachAndViewTracks() {
//        listOfTracks.clear();
//        trackService.search(inputValue)
//            .enqueue(object : Callback<TrackResponse> {
//                override fun onResponse(
//                    call: Call<TrackResponse>,
//                    response: Response<TrackResponse>,
//                ) {
//                    if (response.body() != null && response.code() == 200) {
//                        listOfTracks.addAll(response.body()?.results!!.toList())
//                        trackAdapter.notifyDataSetChanged()
//                    } else {
//                        TODO("")
//                    }
//                }
//
//                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
//                    TODO("")
//                }
//            })

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
                            showMessage(NOTHING_FOUND, "")
                        }
                    } else {
                        recyclerTrack.visibility = View.GONE
                        showMessage(SERVER_PROBLEMS, response.code().toString())


                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    recyclerTrack.visibility = View.GONE
                    showMessage(SERVER_PROBLEMS, t.message.toString())
                }

            })
        }
    }

    private fun showMessage(text: String, additionalMessage: String) {
        if (text == NOTHING_FOUND) {
            nothingFound.visibility = View.VISIBLE
            listOfTracks.clear()
            trackAdapter.notifyDataSetChanged()

            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }

        if (text == SERVER_PROBLEMS) {
            serverpromlems.visibility = View.VISIBLE
            listOfTracks.clear()
            trackAdapter.notifyDataSetChanged()

            if (additionalMessage.isNotEmpty()) {
                Toast.makeText(applicationContext, additionalMessage, Toast.LENGTH_LONG)
                    .show()
            } else {
                nothingFound.visibility = View.GONE
                serverpromlems.visibility = View.GONE
            }
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
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputValue = savedInstanceState.getString(INPUT_AMOUNT, VALUE_DEF)
    }

}