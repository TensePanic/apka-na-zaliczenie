package com.myapp.myapplication.activity_view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.myapp.myapplication.R
import com.myapp.myapplication.TrainingsApplication
import com.myapp.myapplication.data_access_layer.model.TrainingExerciseSet
import com.myapp.myapplication.infrastructure.ExerciseType
import com.myapp.myapplication.view_model.AddingSetViewModel
import com.myapp.myapplication.view_model.AddingSetViewModelFactory
import kotlinx.coroutines.launch

class AddingSetActivity : AppCompatActivity() {

    private val addingSetViewModel: AddingSetViewModel by viewModels {
        AddingSetViewModelFactory((application as TrainingsApplication).repository)
    }
    private var exerciseSet : TrainingExerciseSet? = null
    private var exerciseType: String? = null
    private var exerciseNextOrderNum: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_set) // ← Twój layout XML

        // Set the text in the TextViews
        val nameTextView: TextView = findViewById(R.id.exerciseNameText)
        val repsSuffix: TextView = findViewById(R.id.repsSuffix)
        val descTextView: TextView = findViewById(R.id.exerciseDescText)
        val repsInput: EditText = findViewById(R.id.repsInput)
        val saveButton: Button = findViewById(R.id.saveButton)
        val weightInput = findViewById<EditText>(R.id.weightInput)
        val weightSuffix = findViewById<TextView>(R.id.weightSuffix)

        // Get the exercise details from the intent
        val setId = intent.getIntExtra("currentItemId", -1)
        exerciseNextOrderNum = intent.getIntExtra("nextOrderNum", 0)

        if (setId != -1) {
            lifecycleScope.launch {
                exerciseSet = addingSetViewModel.getExerciseSetById(setId)
                val exercise = addingSetViewModel.getExerciseById(exerciseSet!!.exerciseId)
                exerciseType = exercise.exerciseType

                nameTextView.text = exercise.exerciseName
                repsSuffix.text = typeMainValueSuffix(exercise.exerciseType)
                weightSuffix.text = typeSecondValueSuffix(exercise.exerciseType)
                descTextView.text = exercise.exerciseDesc
                repsInput.text = setReps(exerciseSet, exercise.exerciseType)
                weightInput.text = setWeight(exerciseSet, exercise.exerciseType)

                if (exerciseType == ExerciseType.WITH_WEIGHTS.displayName
                    || exerciseType == ExerciseType.TIME.displayName
                    || exerciseType == ExerciseType.DISTANCE.displayName)  {
                    weightInput.visibility = View.VISIBLE
                    weightSuffix.visibility = View.VISIBLE
                } else {
                    weightInput.visibility = View.GONE
                    weightSuffix.visibility = View.GONE
                }
            }
        }
        else{
            exerciseType = intent.getStringExtra("currentType")
            val exerciseName = intent.getStringExtra("currentName")
            val exerciseDesc = intent.getStringExtra("currentDesc")
            val exerciseID = intent.getIntExtra("exerciseID", -1)
            val trainingId = intent.getIntExtra("trainingId", -1)

            exerciseSet = addingSetViewModel.buildSet(exerciseType,
                exerciseID,
                trainingId,
                exerciseNextOrderNum,
                0,
                0)

            nameTextView.text = exerciseName
            repsSuffix.text = typeMainValueSuffix(exerciseType)
            weightSuffix.text = typeSecondValueSuffix(exerciseType)
            descTextView.text = exerciseDesc

            if (exerciseType == ExerciseType.WITH_WEIGHTS.displayName
                || exerciseType == ExerciseType.TIME.displayName
                || exerciseType == ExerciseType.DISTANCE.displayName) {
                weightInput.visibility = View.VISIBLE
                weightSuffix.visibility = View.VISIBLE
            } else {
                weightInput.visibility = View.GONE
                weightSuffix.visibility = View.GONE
            }
        }

        saveButton.setOnClickListener {
            val mainValueText = repsInput.text.toString()
            val weightValueText = weightInput.text.toString()

            val mainVal = mainValueText.toIntOrNull()
            if ((mainVal == null || mainVal <= 0)
                && (exerciseType == ExerciseType.WITH_WEIGHTS.displayName
                        || exerciseType == ExerciseType.WITHOUT_WEIGHTS.displayName)) {
                Toast.makeText(this, "Niepoprawna liczba powtórzeń!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if ((exerciseType == ExerciseType.WITH_WEIGHTS.displayName) && weightValueText.isBlank()) {
                Toast.makeText(this, "Podaj obciążenie!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if ((exerciseType == ExerciseType.TIME.displayName
                        || exerciseType == ExerciseType.DISTANCE.displayName)
                && (mainValueText.isBlank() && weightValueText.isBlank())) {
                Toast.makeText(this, "Podaj wartość!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val weightVal = weightValueText.toIntOrNull()
            if ((exerciseType == ExerciseType.WITH_WEIGHTS.displayName) && (weightVal == null || weightVal <= 0)) {
                Toast.makeText(this, "Niepoprawna liczba!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            exerciseSet = addingSetViewModel.updateSetValue(exerciseSet!!, exerciseType, mainVal ?: 0, weightVal ?: 0)

            // Dodajesz do bazy
            if (exerciseSet != null) {
            Thread {
                if (setId != -1)
                    addingSetViewModel.updateSet(exerciseSet!!)
                else
                    addingSetViewModel.insert(exerciseSet!!)

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

    private fun setReps(set: TrainingExerciseSet?, type: String): Editable? {
        return when (type) {
            ExerciseType.WITHOUT_WEIGHTS.displayName -> Editable.Factory.getInstance().newEditable(set?.reps.toString())
            ExerciseType.WITH_WEIGHTS.displayName -> Editable.Factory.getInstance().newEditable(set?.reps.toString())
            ExerciseType.TIME.displayName -> Editable.Factory.getInstance().newEditable((set?.time?.div(60)).toString())
            ExerciseType.DISTANCE.displayName -> Editable.Factory.getInstance().newEditable((set?.distance?.div(1000)).toString())
            else -> Editable.Factory.getInstance().newEditable("błąd")
        }
    }

    private fun setWeight(set: TrainingExerciseSet?, type: String): Editable? {
        return when (type) {
            ExerciseType.WITHOUT_WEIGHTS.displayName -> Editable.Factory.getInstance().newEditable(set?.reps.toString())
            ExerciseType.WITH_WEIGHTS.displayName -> Editable.Factory.getInstance().newEditable(set?.reps.toString())
            ExerciseType.TIME.displayName -> Editable.Factory.getInstance().newEditable((set?.time?.rem(60)).toString())
            ExerciseType.DISTANCE.displayName -> Editable.Factory.getInstance().newEditable((set?.distance?.rem(1000)).toString())
            else -> Editable.Factory.getInstance().newEditable("błąd")
        }
    }

    private fun typeMainValueSuffix(name: String?): String {
        return when (name) {
            ExerciseType.WITHOUT_WEIGHTS.displayName -> "powtórzeń"
            ExerciseType.WITH_WEIGHTS.displayName -> "powtórzeń"
            ExerciseType.TIME.displayName -> "min"
            ExerciseType.DISTANCE.displayName -> "km"
            else -> "błąd"
        }
    }
    private fun typeSecondValueSuffix(name: String?): String {
        return when (name) {
            ExerciseType.WITH_WEIGHTS.displayName -> "kg"
            ExerciseType.TIME.displayName -> "s"
            ExerciseType.DISTANCE.displayName -> "m"
            else -> ""
        }
    }
}