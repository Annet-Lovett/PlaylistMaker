package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBack = findViewById<MaterialToolbar>(R.id.buttonSettingsBack)
        val buttonClear = findViewById<ImageButton>(R.id.buttonClear)
        val searchInput = findViewById<EditText>(R.id.searchInput)

        buttonBack.setNavigationOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
            finish()
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                buttonClear.visibility = if (s.isNullOrEmpty()) {
                    ImageButton.GONE
                } else {
                    ImageButton.VISIBLE
                }
            }
        })

        buttonClear.setOnClickListener {
            searchInput.text.clear()
        }
    }
}