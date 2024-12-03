package com.practicum.playlistmaker

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackHolder(item: View) : RecyclerView.ViewHolder(item) {

    private val fullTrack = item.findViewById<LinearLayout>(R.id.fullTrack)
    private val trackImage = item.findViewById<ImageView>(R.id.trackImage)
    private val nameOfTheTrack = item.findViewById<TextView>(R.id.nameOfTheTrack)
    private val nameOfTheArtist = item.findViewById<TextView>(R.id.nameOfTheArtist)
    private val durationOfTheTrack = item.findViewById<TextView>(R.id.durationOfTheTrack)

    fun bind(track: Track) {
        val imgUrl = track.artworkUrl100
        val trackName = track.trackName
        val duration = track.trackTimeMillis
        val artist = track.artistName

        fun dpToPx(dp: Float, context: View): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics).toInt()
        }

        Glide.with(itemView)
            .load(imgUrl)
            .transform(RoundedCorners(dpToPx(2f, itemView)))
            .fallback(R.drawable.trackplaceholder)
            .error(R.drawable.trackplaceholder)
            .into(trackImage)



        nameOfTheTrack.text = trackName
        nameOfTheArtist.text = artist
        durationOfTheTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration.toLong())

    }

}