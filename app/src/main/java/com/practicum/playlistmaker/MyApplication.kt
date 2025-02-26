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
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module

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
                    single { PlayerPrefs(get(), get()) }
                    single <PlayerInteractor> { PlayerInteractorImpl(get()) }
                    factory { MediaPlayer() }
                    viewModelOf(::PlayerViewModel)
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




