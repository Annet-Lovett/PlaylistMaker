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
    private lateinit var searchInput: EditText
    private lateinit var buttonClear: ImageButton
    private lateinit var buttonBack: MaterialToolbar
    private var searchText: String? = null
    
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchInput = findViewById(R.id.searchInput)
        buttonClear = findViewById(R.id.buttonClear)
        buttonBack = findViewById(R.id.buttonSettingsBack)

        buttonBack.setNavigationOnClickListener {
            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
            finish()
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchText = s?.toString()
                buttonClear.visibility = if (s.isNullOrEmpty()) ImageButton.GONE else ImageButton.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        buttonClear.setOnClickListener {
            searchInput.text.clear()  // Очищаем текст в EditText
        }

        // Восстанавливаем сохранённое состояние, если оно существует
        savedInstanceState?.let {
            searchText = it.getString("SEARCH_TEXT")
            searchInput.setText(searchText)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем значение переменной с текстом поискового запроса
        outState.putString("SEARCH_TEXT", searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Восстанавливаем значение переменной с текстом поискового запроса
        searchText = savedInstanceState.getString("SEARCH_TEXT")
        searchInput.setText(searchText)
    }
}