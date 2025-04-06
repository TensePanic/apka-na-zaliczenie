package com.myapp.myapplication.activity_view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myapp.myapplication.R
import com.myapp.myapplication.TrainingExercisesListAdapter
import com.myapp.myapplication.TrainingsApplication
import com.myapp.myapplication.view_model.TrainingDetailsViewModel
import com.myapp.myapplication.view_model.TrainingDetailsViewModelFactory
import kotlinx.coroutines.launch

class TrainingDetailsActivity : AppCompatActivity() {

    private val trainingDetailsViewModel: TrainingDetailsViewModel by viewModels {
        TrainingDetailsViewModelFactory((application as TrainingsApplication).repository)
    }
    private lateinit var trainingExercisesListAdapter: TrainingExercisesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_view)

        // Get the trainingId from the intent
        val trainingId = intent.getIntExtra("currentId", -1)

        // Set up the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        trainingExercisesListAdapter = TrainingExercisesListAdapter()
        recyclerView.adapter = trainingExercisesListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@TrainingDetailsActivity, AllExercisesActivity::class.java)
            intent.putExtra("source", "TrainingDetails")
            intent.putExtra("trainingId", trainingId)
            startActivityForResult(intent, 1)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                trainingDetailsViewModel.getExercisesForTraining(trainingId).collect { exercises ->
                    // Update the UI with the list of exercises
                    trainingExercisesListAdapter.submitList(exercises)
                    // Update the TextView with the selected item
                    val selectedItem = intent.getStringExtra("currentName")
                    val textView: TextView = findViewById(R.id.textView2)
                    textView.text = selectedItem
                }
            }
        }
    }
}