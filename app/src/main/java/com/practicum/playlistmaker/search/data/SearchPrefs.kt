package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.MyApplication
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.search.ui.SearchActivity.Companion.KEY_FOR_HISTORY_LIST_TRACK

class SearchPrefs {

    private val prefs: SharedPreferences = MyApplication.sharedPreferences

    fun recordTrack(track: Track) {

        val currentHistoryList = getHistory().toMutableList()

        currentHistoryList.apply {
            removeIf { it.trackId == track.trackId }
            add(0, track)
            if (size > 10) take(10)
        }

        prefs.edit { putString(KEY_FOR_HISTORY_LIST_TRACK, Gson().toJson(currentHistoryList)) }

    }

    fun getHistory(): List<Track> {
        val prefsHistory = prefs.getString(KEY_FOR_HISTORY_LIST_TRACK, null)

        return if (!prefsHistory.isNullOrBlank()) {
            Gson().fromJson(prefsHistory, object : TypeToken<List<Track>>() {}.type)
                ?: listOf()
        } else listOf()
    }

    fun clearHistory() {
        prefs.edit (true){ putString(KEY_FOR_HISTORY_LIST_TRACK, Gson().toJson(listOf<Track>())) }
    }
}