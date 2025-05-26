package com.practicum.playlistmaker.media.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentTracksBinding
import com.practicum.playlistmaker.media.ui.view_model.TracksViewModel
import com.practicum.playlistmaker.search.ui.view.SearchTrackListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteFragment : Fragment() {

    companion object {

        fun newInstance() = FavouriteFragment()
    }

    private var _binding: FragmentTracksBinding? = null
    private val binding: FragmentTracksBinding
        get() = _binding!!

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
            findNavController().navigate(R.id.player)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
