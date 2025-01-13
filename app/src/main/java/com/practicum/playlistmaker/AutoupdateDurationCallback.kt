package com.practicum.playlistmaker

import android.widget.TextView
import com.practicum.playlistmaker.PlayerActivity.Companion.DURATION_FORMAT
import java.text.SimpleDateFormat
import java.util.Locale

class AutoupdateDurationCallback(
    private var duration: Long,
    private val durationView: TextView,
) : Runnable {
    private var timemillis: Long = 0

    override fun run() {
        if (this.duration > 0L) {
            this.timemillis =  this.duration - 300
        }
        durationView.text = SimpleDateFormat(
            DURATION_FORMAT,
            Locale.getDefault()
        ).format(this.timemillis)
    }
}
