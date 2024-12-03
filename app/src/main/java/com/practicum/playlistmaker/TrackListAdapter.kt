package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackListAdapter(): RecyclerView.Adapter<TrackHolder>() {

    var listOfTheTracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfTheTracks.size
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(listOfTheTracks[position])
    }

}