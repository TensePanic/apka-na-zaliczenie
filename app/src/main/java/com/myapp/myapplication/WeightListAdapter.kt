package com.myapp.myapplication

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapp.myapplication.AllExercisesListAdapter.ExerciseViewHolder
import com.myapp.myapplication.data_access_layer.model.Exercise
import com.myapp.myapplication.data_access_layer.model.WeightEntry
import java.util.Date
import java.util.Locale

class WeightListAdapter(private val context: Context) : ListAdapter<WeightEntry, WeightListAdapter.WeightEntryViewHolder>(WeightEntryComparator()) {

    class WeightEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val weightEntryView: TextView = itemView.findViewById(R.id.textView)

        fun bind(weight: Float?, time: Long) {
            val format = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            weightEntryView.text = weight.toString() + " - " + format.format(Date(time))
        }

        companion object {
            fun create(parent: ViewGroup): WeightEntryViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return WeightEntryViewHolder(view)
            }
        }
    }

    class WeightEntryComparator : DiffUtil.ItemCallback<WeightEntry>() {
        override fun areItemsTheSame(oldItem: WeightEntry, newItem: WeightEntry): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: WeightEntry, newItem: WeightEntry): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightEntryViewHolder {
            return WeightEntryViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WeightEntryViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.weightValue, current.timestamp)
    }
}