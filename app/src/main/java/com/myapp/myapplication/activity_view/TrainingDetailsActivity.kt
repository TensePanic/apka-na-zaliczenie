package com.myapp.myapplication.activity_view

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.myapp.myapplication.view_model.ExerciseViewModel
import com.myapp.myapplication.view_model.ExerciseViewModelFactory
import com.myapp.myapplication.R
import com.myapp.myapplication.TrainingsApplication

class TrainingDetailsActivity : AppCompatActivity() {
    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModelFactory((application as TrainingsApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_view)

        // Pobieranie danych przekazanych z poprzedniej aktywności
        val selectedItem = intent.getStringExtra("currentName")

        // Wykorzystanie danych w nowej aktywności
        // Na przykład, ustawianie tekstu na TextView
        val textView: TextView = findViewById(R.id.textView2)
        textView.text = selectedItem

        //getExercisesForTraining


    }
}