package com.myapp.myapplication.activity_view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.myapp.myapplication.R

class ExerciseDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_details)

        // Get the exercise details from the intent
        val exerciseName = intent.getStringExtra("currentName")
        val exerciseType = intent.getStringExtra("currentType")
        val exerciseDesc = intent.getStringExtra("currentDesc")

        // Set the text in the TextViews
        val nameTextView: TextView = findViewById(R.id.exerciseNameTextView)
        val typeTextView: TextView = findViewById(R.id.exerciseTypeTextView)
        val descTextView: TextView = findViewById(R.id.exerciseDescTextView)

        nameTextView.text = exerciseName
        typeTextView.text = exerciseType
        descTextView.text = exerciseDesc
    }
}