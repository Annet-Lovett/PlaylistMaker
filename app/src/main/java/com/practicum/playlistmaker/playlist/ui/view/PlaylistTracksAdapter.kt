package com.practicum.playlistmaker.playlist.ui.view
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.search.ui.view.SearchTrackHolder
import com.practicum.playlistmaker.sharing.domain.models.Track

class PlaylistTracksAdapter: RecyclerView.Adapter<SearchTrackHolder>() {

    private var listOfTheTracks: List<Track> = emptyList()

    var onItemClick:((track: Track) -> Unit)? = null

    var onItemHold:((track: Track) -> Unit)? = null

    fun updateList(list: List<Track>) {
        listOfTheTracks = list
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTrackHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return SearchTrackHolder(TrackItemBinding.inflate(layoutInspector, parent, false), onItemClick)

    }

    override fun getItemCount(): Int {
        return listOfTheTracks.size
    }

    override fun onBindViewHolder(holder: SearchTrackHolder, position: Int) {
        val track = listOfTheTracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener{
            onItemClick?.let { it(track) }
        }

        holder.itemView.setOnLongClickListener{
            onItemHold?.let { it(track) }
            true
        }
    }

}