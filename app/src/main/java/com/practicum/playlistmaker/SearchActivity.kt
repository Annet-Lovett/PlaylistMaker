package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {

    private var inputValue: String = VALUE_DEF

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

        searchInput.addTextChangedListener(

            onTextChanged = {s: CharSequence?, _, _, _ ->
                    buttonClear.isVisible = !s.isNullOrEmpty()
                    inputValue = s.toString()
        })

        buttonClear.setOnClickListener {
            searchInput.text.clear()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_AMOUNT, inputValue)
    }

    companion object {
        const val INPUT_AMOUNT = "INPUT_AMOUNT"
        const val VALUE_DEF = ""
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputValue = savedInstanceState.getString(INPUT_AMOUNT, VALUE_DEF)
    }

}