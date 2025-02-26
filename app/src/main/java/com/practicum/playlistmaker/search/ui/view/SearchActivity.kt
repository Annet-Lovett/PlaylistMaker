package com.practicum.playlistmaker.search.ui.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.ui.view.PlayerActivity
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.search.ui.view_states.HistoryState
import com.practicum.playlistmaker.search.ui.view_states.ScreenState
import com.practicum.playlistmaker.search.ui.view_states.SearchScreenState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private var _binding: ActivitySearchBinding? = null
    private val binding: ActivitySearchBinding
        get() = _binding!!

    private var isClickAllowed = true
    private val inputValue: String get() = binding.searchInput.text.toString()

    private val searchViewModel by viewModel<SearchViewModel>()

    private val trackAdapter = SearchTrackListAdapter()

    private val trackHistoryAdapter = SearchTrackListAdapter()

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initInput()

        binding.recyclerTrack.adapter = trackAdapter

        trackAdapter.onItemClick = { track ->
            searchViewModel.saveTrack(track)
            startPlayerActivity()
        }

        binding.historyRecycler.adapter = trackHistoryAdapter

        trackHistoryAdapter.onItemClick = { track ->
            searchViewModel.saveTrack(track)
            startPlayerActivity()
        }

        binding.refreshButtonSearch.setOnClickListener {
            searchViewModel.enterSearch(inputValue)
        }

        binding.buttonClear.setOnClickListener {
            binding.searchInput.text.clear()
            binding.recyclerTrack.isVisible = false
        }

        binding.buttonSettingsBack.setNavigationOnClickListener {
            finish()
        }

        binding.clearHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
        }

        searchViewModel.getScreenStateLiveData().observe(this) {
            renderState(it)
        }
    }

    private fun renderState(screenState: ScreenState) {

        binding.buttonClear.isVisible = screenState.showClear

        when (screenState.searchState) {
            is SearchScreenState.Initial -> {
                binding.searchProgressBar.isVisible = false
                binding.searchingHistoryContainer.isVisible = false
                binding.clearHistoryButton.isVisible = false
                binding.serverProblems.isVisible = false
                binding.nothingFound.isVisible = false
                binding.recyclerTrack.isVisible = false
            }

            is SearchScreenState.Content -> {
                binding.searchProgressBar.isVisible = false
                binding.searchingHistoryContainer.isVisible = false
                binding.clearHistoryButton.isVisible = false
                binding.serverProblems.isVisible = false
                binding.nothingFound.isVisible = false
                binding.recyclerTrack.isVisible = true
                trackAdapter.subList(screenState.searchState.trackList)
            }

            is SearchScreenState.Loading -> {
                binding.searchProgressBar.isVisible = true
                binding.searchingHistoryContainer.isVisible = false
                binding.clearHistoryButton.isVisible = false
                binding.serverProblems.isVisible = false
                binding.nothingFound.isVisible = false
                binding.recyclerTrack.isVisible = false
            }

            is SearchScreenState.Empty -> {
                binding.searchProgressBar.isVisible = false
                binding.searchingHistoryContainer.isVisible = false
                binding.clearHistoryButton.isVisible = false
                binding.serverProblems.isVisible = false
                binding.nothingFound.isVisible = true
                binding.recyclerTrack.isVisible = false
            }

            is SearchScreenState.Error -> {
                binding.searchProgressBar.isVisible = false
                binding.searchingHistoryContainer.isVisible = false
                binding.clearHistoryButton.isVisible = false
                binding.serverProblems.isVisible = true
                binding.nothingFound.isVisible = false
                binding.recyclerTrack.isVisible = false
            }

            is HistoryState -> {
                binding.searchProgressBar.isVisible = false
                binding.searchingHistoryContainer.isVisible = true
                binding.serverProblems.isVisible = false
                binding.nothingFound.isVisible = false
                binding.recyclerTrack.isVisible = false

                when(screenState.searchState) {
                    is HistoryState.Empty -> {
                        binding.yourSearchingHistory.isVisible = false
                        binding.clearHistoryButton.isVisible = false
                        trackHistoryAdapter.subList(listOf())
                    }
                    is HistoryState.Data -> {
                        binding.clearHistoryButton.isVisible = true
                        trackHistoryAdapter.subList(screenState.searchState.trackHistoryList)

                    }
                }
            }

        }

    }

    private fun initInput() {
        binding.searchInput.addTextChangedListener {
            searchViewModel.inputChange(it.toString())
        }
      binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchViewModel.enterSearch(inputValue)
            }
            true
        }

        binding.searchInput.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus) searchViewModel.onFocusedSearch()
        }

    }

    private fun startPlayerActivity() {
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


    companion object {
        const val KEY_FOR_HISTORY_LIST_TRACK = "key_for_history_list_preferences"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}

