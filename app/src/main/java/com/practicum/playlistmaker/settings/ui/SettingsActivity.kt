package com.practicum.playlistmaker.settings.ui


import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.MyApplication
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.player.data.PlayerState
import com.practicum.playlistmaker.player.domain.PlayerViewModel
import com.practicum.playlistmaker.player.ui.PlayerActivity.Companion.START_TIME
import com.practicum.playlistmaker.settings.data.SettingsScreenState
import com.practicum.playlistmaker.settings.domain.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

//    private lateinit var switchTheme: Switch
//    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by lazy { ViewModelProvider(this)[SettingsViewModel::class] }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val backButton = binding.buttonSettingsBack

        binding.buttonSettingsBack.setNavigationOnClickListener {
            finish()
        }

//        sharedPreferences = getSharedPreferences(KEY_FOR_SETTINGS, MODE_PRIVATE)

//        switchTheme=findViewById(R.id.theme_switcher)

//        binding.switchTheme.isChecked = sharedPreferences.getBoolean(KEY_FOR_THE_CURRENT_THEME_STATE, false)

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->

            viewModel.changeTheme(checked)
//            sharedPreferences.edit()
//                .putBoolean(KEY_FOR_THE_CURRENT_THEME_STATE, checked)
//                .apply()
//            (applicationContext as MyApplication).switchTheme(sharedPreferences.getBoolean(
//                KEY_FOR_THE_CURRENT_THEME_STATE, false))
        }


//        val shareBtn = binding.btnShareSettings

        binding.btnShareSettings.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }

//        val writeBtn = binding.btnWriteToSupportSettings

        binding.btnWriteToSupportSettings.setOnClickListener {
            val message = getString(R.string.message_to_support)
            val subject = getString(R.string.subject_support)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("annamarkova143@gmail.com"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

//        val btnTermsOfUse = binding.btnTermsOfUseSettings

        binding.btnShareSettings.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.link_to_offer))
            }

            startActivity(intent)
        }

        viewModel.getScreenStateLiveData().observe(this) {
            render(it)
        }
    }

    private fun render (state: SettingsScreenState) {
        binding.themeSwitcher.isChecked = state.isDarkTheme
    }


//    companion object {
//        const val KEY_FOR_SETTINGS = "key_for_settings"
//        const val KEY_FOR_THE_CURRENT_THEME_STATE = "KEY_FOR_THE_CURRENT_THEME_STATE"
//    }



}