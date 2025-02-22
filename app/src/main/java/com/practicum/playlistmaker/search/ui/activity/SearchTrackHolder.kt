package com.practicum.playlistmaker.search.ui.activity

import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackItemBinding
import com.practicum.playlistmaker.sharing.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class SearchTrackHolder(private val binding: TrackItemBinding,
                        onItemClick: ((track: Track) -> Unit)?)
    : RecyclerView.ViewHolder(binding.root) {

    private val fullTrack = binding.fullTrack
    private val trackImage = binding.trackImage
    private val nameOfTheTrack = binding.nameOfTheTrack
    private val nameOfTheArtist = binding.nameOfTheArtist
    private val durationOfTheTrack = binding.durationOfTheTrack

    fun bind(track: Track) {
        val imgUrl = track.artworkUrl100
        val trackName = track.trackName
        val duration = track.trackTimeMillis
        val artist = track.artistName
        val trackId = track.trackId

        fun dpToPx(dp: Float, context: View): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics).toInt()
        }

        Glide.with(binding.trackImage.context)
            .load(imgUrl)
            .fitCenter()
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, itemView)))
            .fallback(R.drawable.trackplaceholder)
            .error(R.drawable.trackplaceholder)
            .into(trackImage)



        nameOfTheTrack.text = trackName
        nameOfTheArtist.text = artist
        durationOfTheTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration.toLong())

    }

}