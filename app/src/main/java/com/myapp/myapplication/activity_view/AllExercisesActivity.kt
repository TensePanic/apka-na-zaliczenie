package com.myapp.myapplication.activity_view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myapp.myapplication.AllExercisesListAdapter
import com.myapp.myapplication.view_model.ExerciseViewModel
import com.myapp.myapplication.view_model.ExerciseViewModelFactory
import com.myapp.myapplication.R
import com.myapp.myapplication.TrainingsApplication
import com.myapp.myapplication.data_access_layer.model.Exercise

class AllExercisesActivity : AppCompatActivity() {

    private val addExerciseActivityRequestCode = 1
    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModelFactory((application as TrainingsApplication).repository)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menuButton -> {
                val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
                val navigationView = findViewById<LinearLayout>(R.id.navigationView)
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView)
                } else {
                    drawerLayout.openDrawer(navigationView)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    public fun startActivityForResult(intent: Intent){
        startActivityForResult(intent, addExerciseActivityRequestCode)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_exercises)

        val source = intent.getStringExtra("source") ?: "default"
        val trainingId = intent.getIntExtra("trainingId", -1)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = AllExercisesListAdapter(this@AllExercisesActivity, source, trainingId)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add an observer on the LiveData returned by getTrainings.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        exerciseViewModel.allExercises.observe(this, Observer { exercises ->
            // Update the cached copy of the exercises in the adapter.
            exercises?.let { adapter.submitList(it) }
        })

        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    return makeMovementFlags(0, ItemTouchHelper.LEFT)
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false // Nie obsługujemy przeciągania góra/dół
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val position = viewHolder.adapterPosition
                        val item = adapter.currentList[position]

                        // Tu wywołujesz funkcję usuwającą element (np. w ViewModelu)
                        exerciseViewModel.delete(item.id)

                        // Opcjonalnie możesz dać feedback
                        Toast.makeText(
                            applicationContext,
                            "Usunięto: ${item.exerciseName}",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }

        // Podpinamy helper do RecyclerView
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@AllExercisesActivity, AddExerciseActivity::class.java)
            startActivityForResult(intent, addExerciseActivityRequestCode)
        }

        val homeButton = findViewById<Button>(R.id.home_button)
        homeButton.setOnClickListener {
            val intent = Intent(this@AllExercisesActivity, MainActivity::class.java)
            startActivityForResult(intent,1)
        }

        val exercisesButton = findViewById<Button>(R.id.exercises_button)
        exercisesButton.setOnClickListener {
            val intent = Intent(this@AllExercisesActivity, AllExercisesActivity::class.java)
            startActivityForResult(intent,1)
        }

        val weightButton = findViewById<Button>(R.id.weight_button)
        exercisesButton.setOnClickListener {
            val intent = Intent(this@AllExercisesActivity, WeightActivity::class.java)
            startActivityForResult(intent,1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == addExerciseActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val name = data.getStringExtra(AddExerciseActivity.EXTRA_NAME)
                val description = data.getStringExtra(AddExerciseActivity.EXTRA_DESCRIPTION)
                val type = data.getStringExtra(AddExerciseActivity.EXTRA_TYPE)
                val id = data.getIntExtra(AddExerciseActivity.EXTRA_ID, -1)

                if (!name.isNullOrEmpty() && !type.isNullOrEmpty()) {
                    // Tworzymy obiekt Exercise i zapisujemy go w bazie danych
                    if(id == -1) {
                        val exercise = Exercise(0, name, description, type)
                        exerciseViewModel.insert(exercise)
                    }
                    else{
                        val exercise = Exercise(id, name, description, type)
                        exerciseViewModel.update(exercise)
                    }
                }
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

}