package com.practicum.playlistmaker.sharing.data

import com.practicum.playlistmaker.sharing.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}
