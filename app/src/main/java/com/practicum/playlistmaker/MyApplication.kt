package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.practicum.playlistmaker.player.data.PlayerPrefs
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.PlayerInteractorImpl
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.data.SearchPrefs
import com.practicum.playlistmaker.sharing.data.dto.TrackRepositoryImpl
import com.practicum.playlistmaker.sharing.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.sharing.domain.api.TrackInteractor
import com.practicum.playlistmaker.sharing.domain.api.TrackRepository
import com.practicum.playlistmaker.sharing.domain.impl.TrackInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.util.concurrent.Executors
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.data.SettingsPrefs
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.data.NetworkClient
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(KEY_FOR_SETTINGS, MODE_PRIVATE)
        switchTheme(sharedPreferences.getBoolean(KEY_FOR_THE_CURRENT_THEME_STATE, false))
        instance = this

        startKoin {

            androidContext(this@MyApplication)

            modules(
                module {
                    single { Gson() }
                    single { getSharedPreferences(KEY_FOR_SETTINGS, MODE_PRIVATE) }
                },

                module {


                    single { PlayerPrefs(get(), get()) }
                    single <PlayerInteractor> { PlayerInteractorImpl(get()) }
                    factory { MediaPlayer() }
                    viewModelOf(::PlayerViewModel)
                },

                module {
                    single<NetworkClient> { RetrofitNetworkClient() }
                    single<TrackRepository> { TrackRepositoryImpl(get()) }
                    single {SearchPrefs(get(), get())}
                    single <TrackInteractor> { TrackInteractorImpl(get(), get()) }
                    factory { Executors.newSingleThreadScheduledExecutor() }
                    viewModelOf(::SearchViewModel)
                },

                module { //укороченная запись верхних модулей
                    singleOf(::SettingsPrefs)
                    singleOf(::SettingsInteractorImpl){bind<SettingsInteractor>()}
                    viewModelOf(::SettingsViewModel)
                }
            )

        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES

            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        lateinit var sharedPreferences: SharedPreferences
        lateinit var instance: MyApplication
        const val KEY_FOR_SETTINGS = "key_for_settings"
        const val KEY_FOR_THE_CURRENT_THEME_STATE = "KEY_FOR_THE_CURRENT_THEME_STATE"
    }

}




