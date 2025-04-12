package com.myapp.myapplication.activity_view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myapp.myapplication.R
import com.myapp.myapplication.TrainingExercisesListAdapter
import com.myapp.myapplication.TrainingsApplication
import com.myapp.myapplication.data_access_layer.model.ExerciseSetDisplay
import com.myapp.myapplication.view_model.TrainingDetailsViewModel
import com.myapp.myapplication.view_model.TrainingDetailsViewModelFactory
import kotlinx.coroutines.launch

class TrainingDetailsActivity : AppCompatActivity() {

    private val trainingDetailsViewModel: TrainingDetailsViewModel by viewModels {
        TrainingDetailsViewModelFactory((application as TrainingsApplication).repository)
    }
    private lateinit var trainingExercisesListAdapter: TrainingExercisesListAdapter
    var currentExerciseIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_view)

        // Get the trainingId from the intent
        val trainingId = intent.getIntExtra("currentId", -1)

        // Set up the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        trainingExercisesListAdapter =
            TrainingExercisesListAdapter(this@TrainingDetailsActivity){ index, item ->
                onClickAction(index, item)
            }
                recyclerView.adapter = trainingExercisesListAdapter
                recyclerView.layoutManager = LinearLayoutManager(this)


                val startBtn = findViewById<Button>(R.id.startTrainingButton)
                startBtn.setOnClickListener {
                    if (trainingExercisesListAdapter.currentList.isNotEmpty()) {
                        currentExerciseIndex = 0
                        trainingExercisesListAdapter.setCurrentExercise(currentExerciseIndex)
                    }
                }

                val itemTouchHelperCallback =
                    object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                        override fun getMovementFlags(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder
                        ): Int {
                            return if (currentExerciseIndex == -1) {
                                // Pozwól przesuwać tylko, jeśli trening nie jest aktywny
                                makeMovementFlags(0, ItemTouchHelper.LEFT)
                            } else {
                                // Zablokuj swipe całkowicie
                                makeMovementFlags(0, 0)
                            }
                        }

                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder
                        ): Boolean {
                            return false // Nie obsługujemy przeciągania góra/dół
                        }

                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            if (currentExerciseIndex == -1) {
                                val position = viewHolder.adapterPosition
                                val item = trainingExercisesListAdapter.currentList[position]

                                // Tu wywołujesz funkcję usuwającą element (np. w ViewModelu)
                                trainingDetailsViewModel.deleteExerciseSetById(item.id)

                                // Opcjonalnie możesz dać feedback
                                Toast.makeText(
                                    applicationContext,
                                    "Usunięto: ${item.exerciseName}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                // Podpinamy helper do RecyclerView
                val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
                itemTouchHelper.attachToRecyclerView(recyclerView)

                val fab = findViewById<FloatingActionButton>(R.id.fab)
                fab.setOnClickListener {
                    if (currentExerciseIndex == -1) {
                        val intent =
                        Intent(this@TrainingDetailsActivity, AllExercisesActivity::class.java)
                        intent.putExtra("source", "TrainingDetails")
                        intent.putExtra("trainingId", trainingId)
                        startActivityForResult(intent, 1)
                    }
                }

                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        trainingDetailsViewModel.getExercisesForTraining(trainingId)
                            .collect { exercises ->
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
    private fun onClickAction(index: Int, item: ExerciseSetDisplay){
        if(item.exerciseType == "Time")
            showSkipConfirmationDialog(index)
        else
            endSet(index)
    }
    private fun showSkipConfirmationDialog(index: Int) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Pomiń ćwiczenie?")
        builder.setMessage("Czy na pewno chcesz pominąć to ćwiczenie?")
        builder.setPositiveButton("Tak") { dialog, _ ->
            endSet(index)
            dialog.dismiss()
        }
        builder.setNegativeButton("Nie") { dialog, _ ->
            // Nie rób nic, kontynuuj odliczanie
            dialog.dismiss()
        }
        builder.show()
    }

    private fun endSet(index: Int) {
        if (index == currentExerciseIndex) {
            currentExerciseIndex++
            if (currentExerciseIndex < trainingExercisesListAdapter.itemCount) {
                trainingExercisesListAdapter.setCurrentExercise(currentExerciseIndex)
            } else {
                Toast.makeText(this, "Trening zakończony!", Toast.LENGTH_LONG).show()
                currentExerciseIndex = -1
                trainingExercisesListAdapter.setCurrentExercise(currentExerciseIndex)
            }
        }
    }
}