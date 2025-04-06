package com.myapp.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapp.myapplication.activity_view.AddingSetActivity
import com.myapp.myapplication.activity_view.AllExercisesActivity
import com.myapp.myapplication.activity_view.ExerciseDetailsActivity
import com.myapp.myapplication.activity_view.TrainingDetailsActivity
import com.myapp.myapplication.data_access_layer.model.Exercise

class AllExercisesListAdapter(private val context: Context, private val source: String, private val trainingId: Int = -1): ListAdapter<Exercise, AllExercisesListAdapter.ExerciseViewHolder>(ExerciseComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.exerciseName)

        holder.itemView.setOnClickListener {
            when(source){
                "TrainingDetails" -> {val intent = Intent(context, AddingSetActivity::class.java)
                    intent.putExtra("currentName", current.exerciseName)
                    intent.putExtra("currentType", current.exerciseType)
                    intent.putExtra("currentDesc", current.exerciseDesc)
                    intent.putExtra("exerciseID", current.id)
                    intent.putExtra("trainingId", trainingId)
                    context.startActivity(intent)}
                else -> {
                    val intent = Intent(context, ExerciseDetailsActivity::class.java)
                    intent.putExtra("currentName", current.exerciseName)
                    intent.putExtra("currentType", current.exerciseType)
                    intent.putExtra("currentDesc", current.exerciseDesc)
                    context.startActivity(intent)
                }
            }
        }
    }

    class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?) {
            wordItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): ExerciseViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return ExerciseViewHolder(view)
            }
        }
    }

    class ExerciseComparator : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.exerciseName == newItem.exerciseName
        }
    }
}
