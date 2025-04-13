package com.myapp.myapplication.activity_view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.myapp.myapplication.R
import com.myapp.myapplication.TrainingsApplication
import com.myapp.myapplication.WeightListAdapter
import com.myapp.myapplication.data_access_layer.model.WeightEntry
import com.myapp.myapplication.view_model.WeightViewModel
import com.myapp.myapplication.view_model.WeightViewModelFactory
import java.util.Date
import java.util.Locale

class WeightActivity : AppCompatActivity() {

    val addWeightEntryActivityRequestCode = 2
    private val weightViewModel: WeightViewModel by viewModels {
        WeightViewModelFactory((application as TrainingsApplication).repository)
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
        setContentView(R.layout.activity_weight)
        weightViewModel.allWeightEntriesAsc(0)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //supportActionBar?.hide() // jeśli chcesz ukryć domyślny ActionBar, bo masz Toolbar w layoucie

        val recyclerView = findViewById<RecyclerView>(R.id.weightHistoryRecyclerView)
        val adapter = WeightListAdapter(this@WeightActivity)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        weightViewModel.allWeightEntries.observe(this, Observer { weight ->
            // Update the cached copy of the exercises in the adapter.
            weight?.let { adapter.submitList(it) }
        })
        buildChart(0)

        val btn1w = findViewById<Button>(R.id.btn1w)
        btn1w.setOnClickListener {
            buildChart(1)
        }
        val btn1m = findViewById<Button>(R.id.btn1m)
        btn1m.setOnClickListener {
            buildChart(2)
        }
        val btn6m = findViewById<Button>(R.id.btn6m)
        btn6m.setOnClickListener {
            buildChart(3)
        }
        val btnMax = findViewById<Button>(R.id.btn1y)
        btnMax.setOnClickListener {
            buildChart(0)
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@WeightActivity, AddWeightEntryActivity::class.java)
            startActivityForResult(intent, addWeightEntryActivityRequestCode)
        }

        val homeButton = findViewById<Button>(R.id.home_button)
        homeButton.setOnClickListener {
            val intent = Intent(this@WeightActivity, MainActivity::class.java)
            startActivityForResult(intent,1)
        }

        val exercisesButton = findViewById<Button>(R.id.exercises_button)
        exercisesButton.setOnClickListener {
            val intent = Intent(this@WeightActivity, AllExercisesActivity::class.java)
            startActivityForResult(intent,1)
        }

        val weightButton = findViewById<Button>(R.id.weight_button)
        weightButton.setOnClickListener {
            val intent = Intent(this@WeightActivity, WeightActivity::class.java)
            startActivityForResult(intent,1)
        }
    }

    fun buildChart(type: Int){
        val chart = findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.weightChart)
        weightViewModel.allWeightEntriesAsc(type).observe(this) { entries ->
            val entriesForChart = entries.mapIndexed { index, entry ->
                Entry(entry.timestamp.toFloat(), entry.weightValue)
            }

            val dataSet = LineDataSet(entriesForChart, "Waga (kg)").apply {
                color = Color.BLUE
                valueTextColor = Color.BLACK
                lineWidth = 2f
                setDrawCircles(true)
                setDrawValues(false)
            }

            val xAxis = chart.xAxis
            xAxis.valueFormatter = DateAxisFormatter()
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawLabels(true)

            val lineData = LineData(dataSet)
            chart.data = lineData
            chart.invalidate()

            with(chart) {
                description.isEnabled = false
                axisRight.isEnabled = false
                legend.isEnabled = true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == addWeightEntryActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val weightVal = data.getStringExtra(AddWeightEntryActivity.EXTRA_WEIGHT)?.toFloatOrNull()

                if (weightVal != null) {
                    // Tworzymy obiekt WeightEntry i zapisujemy go w bazie danych
                    val weightEntry = WeightEntry(0, weightVal)
                    weightViewModel.insert(weightEntry)
                }else{
                    Toast.makeText(
                        applicationContext,
                        R.string.invalid_not_saved,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}

class DateAxisFormatter : ValueFormatter() {
    private val dateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())

    override fun getFormattedValue(value: Float): String {
        val millis = value.toLong()
        return dateFormat.format(Date(millis))
    }
}
