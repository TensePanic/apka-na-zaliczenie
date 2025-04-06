package com.myapp.myapplication.activity_view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.viewModels
import com.myapp.myapplication.R
import com.myapp.myapplication.TrainingsApplication
import com.myapp.myapplication.data_access_layer.model.TrainingExerciseSet
import com.myapp.myapplication.view_model.AddingSetViewModel
import com.myapp.myapplication.view_model.AddingSetViewModelFactory

class AddingSetActivity : AppCompatActivity() {
    private val addingSetViewModel: AddingSetViewModel by viewModels {
        AddingSetViewModelFactory((application as TrainingsApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_set) // ← Twój layout XML_new) // ← Twój layout XML

        // Get the exercise details from the intent
        val exerciseName = intent.getStringExtra("currentName")
        val exerciseType = intent.getStringExtra("currentType")
        val exerciseDesc = intent.getStringExtra("currentDesc")
        val exerciseID = intent.getIntExtra("exerciseID", -1)
        val trainingId = intent.getIntExtra("trainingId", -1)

        // Set the text in the TextViews
        val nameTextView: TextView = findViewById(R.id.exerciseNameText)
        val typeTextView: TextView = findViewById(R.id.repsSuffix)
        val descTextView: TextView = findViewById(R.id.exerciseDescText)

        nameTextView.text = exerciseName
        typeTextView.text = typeStringMatch(exerciseType)
        descTextView.text = exerciseDesc

        val repsInput: EditText = findViewById(R.id.repsInput)
        val saveButton: Button = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val mainValueText = repsInput.text.toString()

            if (mainValueText.isBlank()) {
                Toast.makeText(this, "Podaj liczbę powtórzeń!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mainVal = mainValueText.toIntOrNull()
            if (mainVal == null || mainVal <= 0) {
                Toast.makeText(this, "Niepoprawna liczba!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newSet = buildSet(exerciseType, exerciseID, trainingId, mainVal)
            // Tu tworzysz obiekt nowej serii
            /*val newSet = TrainingExerciseSet(
                exerciseId = exerciseID,
                trainingId = trainingId,
                reps = reps
            )*/

            // Dodajesz do bazy
            if (newSet != null) {
            Thread {
                addingSetViewModel.insert(newSet)

                runOnUiThread {
                    Toast.makeText(this, "Dodano serię!", Toast.LENGTH_SHORT).show()
                    finish() // Zamyka activity
                }
            }.start()
                val replyIntent = Intent()
                setResult(Activity.RESULT_OK, replyIntent)
            }
            else
                Toast.makeText(this, "Niepoprawny typ ćwiczenia!", Toast.LENGTH_SHORT).show()


        }

    }

    private fun buildSet(
        exerciseType: String?,
        exerciseID: Int,
        trainingId: Int,
        mainValue: Int?
    ): TrainingExerciseSet? {
        when (exerciseType) {
            "Without weights" -> {
                return TrainingExerciseSet(
                    exerciseId = exerciseID,
                    trainingId = trainingId,
                    reps = mainValue
                )
            }

            "With weights" -> {
                return TrainingExerciseSet(
                    exerciseId = exerciseID,
                    trainingId = trainingId,
                    reps = mainValue,
                    weight = 0
                )
            }

            "Time" -> {
                return TrainingExerciseSet(
                    exerciseId = exerciseID,
                    trainingId = trainingId,
                    time = mainValue
                )
            }

            "Distance" -> {
                return TrainingExerciseSet(
                    exerciseId = exerciseID,
                    trainingId = trainingId,
                    distance = mainValue
                )
            }

            else -> return null
        }
    }

    private fun typeStringMatch(name: String?): String {
        return when (name) {
            "Without weights" -> "powtórzeń"
            "With weights" -> "powtórzeń"
            "Time" -> "sekund(y)"
            "Distance" -> "m"
            else -> "błąd"
        }
    }
}