package com.practicum.playlistmaker
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class TrackListAdapter(): RecyclerView.Adapter<TrackHolder>() {

    var listOfTheTracks = ArrayList<Track>()

    var onItemClick:((track: Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfTheTracks.size
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.onItemClick = onItemClick
        holder.bind(listOfTheTracks[position])
    }

}