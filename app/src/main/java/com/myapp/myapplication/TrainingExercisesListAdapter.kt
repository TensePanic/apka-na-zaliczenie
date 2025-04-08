package com.myapp.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.myapp.myapplication.activity_view.AddingSetActivity
import com.myapp.myapplication.activity_view.ExerciseDetailsActivity
import com.myapp.myapplication.data_access_layer.model.Exercise
import com.myapp.myapplication.data_access_layer.model.ExerciseSetDisplay

class TrainingExercisesListAdapter (private val context: Context, var tickClickListener: (Int) -> Unit) :
    ListAdapter<ExerciseSetDisplay, TrainingExercisesListAdapter.TrainingExercisesViewHolder>(TrainingExercisesComparator()) {

    private var currentExerciseIndex: Int = -1
    fun setCurrentExercise(index: Int) {
        val previousIndex = currentExerciseIndex
        currentExerciseIndex = index
        notifyItemChanged(previousIndex)
        notifyItemChanged(currentExerciseIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingExercisesViewHolder {
        return TrainingExercisesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TrainingExercisesViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, position == currentExerciseIndex, tickClickListener)

        holder.itemView.setOnClickListener {
                val intent = Intent(context, AddingSetActivity::class.java)
                    intent.putExtra("currentItemId", current.id)
                    context.startActivity(intent)
        }
    }

    class TrainingExercisesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val exerciseNameTextView: TextView = itemView.findViewById(R.id.exerciseNameTextView)
        private val exerciseDetailsTextView: TextView = itemView.findViewById(R.id.exerciseDetailsTextView)
        private val tickButton: ImageButton = itemView.findViewById(R.id.tickButton)

        fun bind(item: ExerciseSetDisplay, isCurrent: Boolean, tickClickListener: (Int) -> Unit) {
            exerciseNameTextView.text = item.exerciseName
            exerciseDetailsTextView.text = buildString {
                if (item.reps != null) append("Reps: ${item.reps}  ")
                if (item.weight != null) append("Weight: ${item.weight}kg  ")
                if (item.time != null) append("Time: ${item.time}s  ")
                if (item.distance != null) append("Distance: ${item.distance}m")

                itemView.setBackgroundColor(
                    if (isCurrent) Color.parseColor("#DFF0D8") else Color.TRANSPARENT
                )

                tickButton.visibility = if (isCurrent) View.VISIBLE else View.GONE

                tickButton.setOnClickListener {
                    tickClickListener.invoke(adapterPosition)
                }
            }
        }

        companion object {
            fun create(parent: ViewGroup): TrainingExercisesViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item_exercise, parent, false)
                return TrainingExercisesViewHolder(view)
            }
        }
    }

    class TrainingExercisesComparator : DiffUtil.ItemCallback<ExerciseSetDisplay>() {
        override fun areItemsTheSame(oldItem: ExerciseSetDisplay, newItem: ExerciseSetDisplay): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ExerciseSetDisplay, newItem: ExerciseSetDisplay): Boolean {
            return oldItem.exerciseName == newItem.exerciseName
        }
    }
}