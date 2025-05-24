package com.practicum.playlistmaker.search.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.search.ui.view_states.HistoryState
import com.practicum.playlistmaker.search.ui.view_states.ScreenState
import com.practicum.playlistmaker.search.ui.view_states.SearchScreenState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    private var isClickAllowed = true
    private val inputValue: String get() = binding.searchInput.text.toString()

    private val searchViewModel by activityViewModel<SearchViewModel>()

    private val trackAdapter = SearchTrackListAdapter()

    private val trackHistoryAdapter = SearchTrackListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.clearHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
        }

        searchViewModel.getScreenStateLiveData().observe(viewLifecycleOwner) {
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

                when (screenState.searchState) {
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
            if (hasFocus) searchViewModel.onFocusedSearch()
        }

    }

    private fun startPlayerActivity() {
        if (clickDebounce()) {
            findNavController().navigate(R.id.player)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }

        }
        return current
    }


    companion object {
        const val KEY_FOR_HISTORY_LIST_TRACK = "key_for_history_list_preferences"
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}