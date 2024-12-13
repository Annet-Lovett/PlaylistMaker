package com.practicum.playlistmaker
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackListAdapter(): RecyclerView.Adapter<TrackHolder>() {

    var listOfTheTracks = ArrayList<Track>()

    var onItemClick:((track: Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackHolder(view, onItemClick)
    }

    override fun getItemCount(): Int {
        return listOfTheTracks.size
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        val track = listOfTheTracks[position]
        holder.bind(listOfTheTracks[position])
        holder.itemView.setOnClickListener{
            onItemClick?.let { it1 -> it1(track) }
        }
    }

}