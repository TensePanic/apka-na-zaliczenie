package com.myapp.myapplication

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class AddExerciseActivity : AppCompatActivity() {

    private lateinit var editExerciseView: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)
        editExerciseView = findViewById(R.id.edit_exercise)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editExerciseView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val exercise = editExerciseView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, exercise)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }
}