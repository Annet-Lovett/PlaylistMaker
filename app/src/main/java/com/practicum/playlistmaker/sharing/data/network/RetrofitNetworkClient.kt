package com.practicum.playlistmaker.sharing.data.network

import com.practicum.playlistmaker.sharing.data.NetworkClient
import com.practicum.playlistmaker.sharing.data.dto.Response
import com.practicum.playlistmaker.sharing.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private val baseTrackUrl = BASE_URL

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseTrackUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val trackService = retrofit.create(PlaylistApi::class.java)

    override suspend fun doRequest(dto: Any): Response {
        return  try {
            trackService.search((dto as TrackRequest).request)
                .apply { resultCode = 200 }

        }

        catch (t: Throwable) {
            Response().apply { resultCode = 400 }
        }
    }

    companion object {
        const val BASE_URL = "https://itunes.apple.com"
    }

}

