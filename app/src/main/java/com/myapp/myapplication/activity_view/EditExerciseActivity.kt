package com.myapp.myapplication.activity_view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.myapp.myapplication.R
import com.myapp.myapplication.activity_view.AddExerciseActivity.Companion.EXTRA_DESCRIPTION
import com.myapp.myapplication.activity_view.AddExerciseActivity.Companion.EXTRA_ID
import com.myapp.myapplication.activity_view.AddExerciseActivity.Companion.EXTRA_NAME
import com.myapp.myapplication.activity_view.AddExerciseActivity.Companion.EXTRA_TYPE
import com.myapp.myapplication.infrastructure.ExerciseType

class EditExerciseActivity : AppCompatActivity() {

    private lateinit var editExerciseView: EditText
    private lateinit var editDescriptionView: EditText
    private lateinit var spinnerExerciseType: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)

        // Znajdujemy pola w układzie
        editExerciseView = findViewById(R.id.edit_exercise)
        editDescriptionView = findViewById(R.id.edit_description)
        spinnerExerciseType = findViewById(R.id.spinner_exercise_type)

        // Get the exercise details from the intent
        val exerciseName = intent.getStringExtra("currentName")
        val exerciseType = intent.getStringExtra("currentType")
        val exerciseDesc = intent.getStringExtra("currentDesc")
        val exerciseID = intent.getIntExtra("currentId", -1)

        // Lista typów ćwiczeń
        val exerciseTypes = ExerciseType.entries.map { it.displayName }

        // Tworzymy adapter do spinnera
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, // Układ dla każdego elementu listy
            exerciseTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Układ rozwijanego menu
        spinnerExerciseType.adapter = adapter

        val pos = adapter.getPosition(exerciseType)
        spinnerExerciseType.setSelection(pos)
        editExerciseView.text = Editable.Factory.getInstance().newEditable(exerciseName)
        editDescriptionView.text = Editable.Factory.getInstance().newEditable(exerciseDesc)

        //Ustawiamy akcję, którą ma wykonywać przycisk zapisu
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()

            //Walidacja czy aby nazwa nie jest pusta
            if (TextUtils.isEmpty(editExerciseView.text)) { //TODO: tu chyba trzeba sprawdzic czy type tez nie jest null
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                // Pobieranie danych z pól
                val exerciseName = editExerciseView.text.toString()
                val exerciseDescription = editDescriptionView.text.toString()
                val selectedType = spinnerExerciseType.selectedItem.toString()

                // Dodanie danych do Intent
                replyIntent.putExtra(EXTRA_NAME, exerciseName)
                replyIntent.putExtra(EXTRA_DESCRIPTION, exerciseDescription)
                replyIntent.putExtra(EXTRA_TYPE, selectedType)
                replyIntent.putExtra(EXTRA_ID, exerciseID)

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
}