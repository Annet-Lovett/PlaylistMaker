package com.practicum.playlistmaker.media.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.databinding.FragmentTracksBinding
import com.practicum.playlistmaker.media.ui.view_model.TracksViewModel
import com.practicum.playlistmaker.player.ui.view.PlayerActivity
import com.practicum.playlistmaker.search.ui.view.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import com.practicum.playlistmaker.search.ui.view.SearchTrackListAdapter
import com.practicum.playlistmaker.sharing.domain.api.FavouritesInteractor
import com.practicum.playlistmaker.sharing.domain.api.TrackInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel

class TracksFragment : Fragment() {

    companion object {

        fun newInstance() = TracksFragment()
    }

    private var _binding: FragmentTracksBinding? = null
    private val binding: FragmentTracksBinding
        get() = _binding!!

    private var isClickAllowed = true

    private val favouriteTrackAdapter = SearchTrackListAdapter()
    private val tracksViewModel: TracksViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.favouriteListRecycler.adapter = favouriteTrackAdapter

        favouriteTrackAdapter.onItemClick = { track ->
//            get<TrackInteractor>().recordTrack(track)
            tracksViewModel.selectTrack(track)
            startPlayerActivity()
        }

        tracksViewModel.favouritesLiveData.observe(viewLifecycleOwner){

            favouriteTrackAdapter.subList(it)

            if (it.isNotEmpty()) {
                binding.favouriteListRecycler.visibility = View.VISIBLE
                binding.nothingFoundImg.visibility = View.GONE
                binding.mediaText.visibility = View.GONE

            } else {
                binding.favouriteListRecycler.visibility = View.GONE
                binding.nothingFoundImg.visibility = View.VISIBLE
                binding.mediaText.visibility = View.VISIBLE
            }

        }

    }

    private fun startPlayerActivity() {
        if (clickDebounce()) {
            val playerActivityIntent = Intent(requireActivity(), PlayerActivity::class.java)
            startActivity(playerActivityIntent)
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


}
