package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {

    private lateinit var switchTheme: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<MaterialToolbar>(R.id.buttonSettingsBack)

        backButton.setNavigationOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }

        switchTheme=findViewById(R.id.theme_switcher)

        switchTheme.isChecked=getSavedThemeState()

        switchTheme.setOnCheckedChangeListener {_, isCheked ->
            if(isCheked){
                setDarkTheme()
            }
            else {
                setLightTheme()
            }
            saveThemeState(isCheked)
        }

        if (switchTheme.isChecked){
            setDarkTheme()
        }

        else {
            setLightTheme()
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


    private fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun getSavedThemeState(): Boolean {
        val sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
        return  sharedPreferences.getBoolean(DARK_THEME_STATE_ATTR,false)
    }



    private fun saveThemeState(isDarkTheme:Boolean){
        val sharedPreferences = getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit() {
            putBoolean(DARK_THEME_STATE_ATTR, isDarkTheme)
        }
    }

    companion object {
        val DARK_THEME_STATE_ATTR = "isDarkTheme"
    }

}