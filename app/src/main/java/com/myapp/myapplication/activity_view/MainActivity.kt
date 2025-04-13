package com.myapp.myapplication.activity_view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myapp.myapplication.R
import com.myapp.myapplication.data_access_layer.model.Training
import com.myapp.myapplication.TrainingListAdapter
import com.myapp.myapplication.view_model.TrainingViewModel
import com.myapp.myapplication.view_model.TrainingViewModelFactory
import com.myapp.myapplication.TrainingsApplication

class MainActivity : AppCompatActivity() {

    private val newTrainingActivityRequestCode = 1
    private val trainingViewModel: TrainingViewModel by viewModels {
        TrainingViewModelFactory((application as TrainingsApplication).repository)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = TrainingListAdapter(this@MainActivity)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add an observer on the LiveData returned by getTrainings.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        trainingViewModel.allTrainings.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.submitList(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewTrainingActivity::class.java)
            startActivityForResult(intent, newTrainingActivityRequestCode)
        }

        val homeButton = findViewById<Button>(R.id.home_button)
        homeButton.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivityForResult(intent,1)
        }

        val exercisesButton = findViewById<Button>(R.id.exercises_button)
        exercisesButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AllExercisesActivity::class.java)
            startActivityForResult(intent,1)
        }

        val weightButton = findViewById<Button>(R.id.weight_button)
        weightButton.setOnClickListener {
            val intent = Intent(this@MainActivity, WeightActivity::class.java)
            startActivityForResult(intent,1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newTrainingActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(NewTrainingActivity.EXTRA_REPLY)?.let { reply ->
                val training = Training(0, reply)
                trainingViewModel.insert(training)
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
