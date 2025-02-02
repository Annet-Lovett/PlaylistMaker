package com.practicum.playlistmaker.ui.search
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track

class SearchTrackListAdapter(): RecyclerView.Adapter<SearchTrackHolder>() {

    private var listOfTheTracks: List<Track> = emptyList()

    var onItemClick:((track: Track) -> Unit)? = null

    fun subList(list: List<Track>) {
        listOfTheTracks = list
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return SearchTrackHolder(view, onItemClick)
    }

    override fun getItemCount(): Int {
        return listOfTheTracks.size
    }

    override fun onBindViewHolder(holder: SearchTrackHolder, position: Int) {
        val track = listOfTheTracks[position]
        holder.bind(listOfTheTracks[position])
        holder.itemView.setOnClickListener{
            onItemClick?.let { it(track) }
        }
    }

}