package com.myapp.myapplication.activity_view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.myapp.myapplication.R

class AddingSetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_set) // ← Twój layout XML_new) // ← Twój layout XML

        // Get the exercise details from the intent
        val exerciseName = intent.getStringExtra("currentName")
        val exerciseType = intent.getStringExtra("currentType")
        val exerciseDesc = intent.getStringExtra("currentDesc")
        val exerciseID = intent.getIntExtra("exerciseID", -1)

        // Set the text in the TextViews
        val nameTextView: TextView = findViewById(R.id.exerciseNameText)
        val typeTextView: TextView = findViewById(R.id.repsSuffix)
        val descTextView: TextView = findViewById(R.id.exerciseDescText)

        nameTextView.text = exerciseName
        typeTextView.text = typeStringMatch(exerciseType)
        descTextView.text = exerciseDesc

        // Przykład użycia
        Toast.makeText(this, "Dostałem: $exerciseName", Toast.LENGTH_SHORT).show()

        // Tutaj dodaj kolejne funkcje, np. setupRecyclerView(), initListeners() itd.
    }
// "Without weights", "With weights", "Time", "Distance"
    fun typeStringMatch(name: String?): String {
        when (name) {
            "Without weights" -> return "powtórzeń"
            "With weights" -> return "powtórzeń"
            "Time" -> return "sekund(y)"
            "Distance" -> return "m"
            else -> return "błąd"
        }
    }
}