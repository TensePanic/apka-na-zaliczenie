package com.myapp.myapplication.activity_view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.myapp.myapplication.R

class AddExerciseActivity : AppCompatActivity() {

    private lateinit var editExerciseView: EditText
    private lateinit var editDescriptionView: EditText
    private lateinit var spinnerExerciseType: Spinner

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)

        // Znajdujemy pola w układzie
        editExerciseView = findViewById(R.id.edit_exercise)
        editDescriptionView = findViewById(R.id.edit_description)
        spinnerExerciseType = findViewById(R.id.spinner_exercise_type)

        // Lista typów ćwiczeń
        val exerciseTypes = listOf("Without weights", "With weights", "Time", "Distance")

        // Tworzymy adapter do spinnera
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, // Układ dla każdego elementu listy
            exerciseTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Układ rozwijanego menu
        spinnerExerciseType.adapter = adapter

        //Ustawiamy akcję, którą ma wykonywać przycisk zapisu
        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()

            //Walidacja czy aby nazwa nie jest pusta
            if (TextUtils.isEmpty(editExerciseView.text)) { //TODO: Rafał, zobacz bo tu chyba trzeba sprawdzic czy type tez nie jest null
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

                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_NAME = "com.myapp.myapplication.EXTRA_NAME"
        const val EXTRA_DESCRIPTION = "com.myapp.myapplication.EXTRA_DESCRIPTION"
        const val EXTRA_TYPE = "com.myapp.myapplication.EXTRA_TYPE"

    }
}