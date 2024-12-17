package com.practicum.playlistmaker
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class TrackListAdapter(): RecyclerView.Adapter<TrackHolder>() {

    private var listOfTheTracks: List<Track> = emptyList()

    var onItemClick:((track: Track) -> Unit)? = null

    fun subList(list: List<Track>) {
        listOfTheTracks = list
        notifyDataSetChanged()

    }

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
            onItemClick?.let { it(track) }
        }
    }

}