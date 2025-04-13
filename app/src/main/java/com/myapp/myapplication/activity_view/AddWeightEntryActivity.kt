package com.myapp.myapplication.activity_view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.myapp.myapplication.R

class AddWeightEntryActivity : AppCompatActivity() {

    private lateinit var weightInput: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_weight_entry) // ← Twój layout XML

        // Set the text in the TextViews
        weightInput = findViewById(R.id.weightInput)
        saveButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val replyIntent = Intent()

            //Walidacja czy aby wartość nie jest pusta
            if (TextUtils.isEmpty(weightInput.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                // Pobieranie danych z pól
                val weight = weightInput.text.toString()

                // Dodanie danych do Intent
                replyIntent.putExtra(EXTRA_WEIGHT, weight)

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }


    }

    companion object {
        const val EXTRA_WEIGHT = "com.myapp.myapplication.EXTRA_WEIGHT"
    }
}