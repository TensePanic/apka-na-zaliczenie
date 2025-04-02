package com.myapp.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapp.myapplication.data_access_layer.model.Exercise

class TrainingExercisesListAdapter :
    ListAdapter<Exercise, TrainingExercisesListAdapter.TrainingExercisesViewHolder>(TrainingExercisesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingExercisesViewHolder {
        return TrainingExercisesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TrainingExercisesViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.exerciseName, current.exerciseDesc)
    }

    class TrainingExercisesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseNameTextView: TextView = itemView.findViewById(R.id.exerciseNameTextView)
        private val exerciseDescTextView: TextView = itemView.findViewById(R.id.exerciseDescTextView)

        fun bind(exerciseName: String?, exerciseDesc: String?) {
            exerciseNameTextView.text = exerciseName
            exerciseDescTextView.text = exerciseDesc
        }

        companion object {
            fun create(parent: ViewGroup): TrainingExercisesViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item_exercise, parent, false)
                return TrainingExercisesViewHolder(view)
            }
        }
    }

    class TrainingExercisesComparator : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.exerciseName == newItem.exerciseName && oldItem.exerciseDesc == newItem.exerciseDesc
        }
    }
}