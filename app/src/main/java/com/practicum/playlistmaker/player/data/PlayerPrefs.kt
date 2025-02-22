package com.practicum.playlistmaker.player.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.sharing.domain.models.Track
import com.practicum.playlistmaker.player.ui.view.PlayerActivity.Companion.KEY_FOR_CURRENT_TRACK

class PlayerPrefs(private val prefs: SharedPreferences) {

    fun getTrack(): Track {
        return prefs.getString(KEY_FOR_CURRENT_TRACK, "")!!.let { createFactFromJson(it) }
    }

    private fun createFactFromJson(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
    }
}