package com.practicum.playlistmaker.createPlaylist.ui.view_state

import android.net.Uri

data class CreatePlaylistViewState (val ready: Boolean,
                                    val uri: Uri? = null,
                                    val name: String = "",
                                    val description: String = "")