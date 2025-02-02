package com.practicum.playlistmaker.ui.settings


import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.data.dto.App
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var switchTheme: Switch
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<MaterialToolbar>(R.id.buttonSettingsBack)

        backButton.setNavigationOnClickListener {
            finish()
        }

        sharedPreferences = getSharedPreferences(KEY_FOR_SETTINGS, MODE_PRIVATE)

        switchTheme=findViewById(R.id.theme_switcher)

        switchTheme.isChecked = sharedPreferences.getBoolean(KEY_FOR_THE_CURRENT_THEME_STATE, false)

        switchTheme.setOnCheckedChangeListener { _, checked ->
            sharedPreferences.edit()
                .putBoolean(KEY_FOR_THE_CURRENT_THEME_STATE, checked)
                .apply()
            (applicationContext as App).switchTheme(sharedPreferences.getBoolean(
                KEY_FOR_THE_CURRENT_THEME_STATE, false))
        }


        val shareBtn = findViewById<Button>(R.id.btnShareSettings)

        shareBtn.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }

        val writeBtn = findViewById<Button>(R.id.btnWriteToSupportSettings)

        writeBtn.setOnClickListener {
            val message = getString(R.string.message_to_support)
            val subject = getString(R.string.subject_support)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("annamarkova143@gmail.com"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        val btnTermsOfUse = findViewById<Button>(R.id.btnTermsOfUseSettings)

        btnTermsOfUse.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.link_to_offer))
            }

            startActivity(intent)
        }
    }

    companion object {
        const val KEY_FOR_SETTINGS = "key_for_settings"
        const val KEY_FOR_THE_CURRENT_THEME_STATE = "KEY_FOR_THE_CURRENT_THEME_STATE"
    }



}