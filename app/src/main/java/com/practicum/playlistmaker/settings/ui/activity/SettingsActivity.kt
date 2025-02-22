package com.practicum.playlistmaker.settings.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.ui.view_states.SettingsScreenState
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    private val viewModel by lazy { ViewModelProvider(this)[SettingsViewModel::class] }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSettingsBack.setNavigationOnClickListener {
            finish()
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->

            viewModel.changeTheme(checked)
        }


        binding.btnShareSettings.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }

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

        binding.btnTermsOfUseSettings.setOnClickListener{
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

}