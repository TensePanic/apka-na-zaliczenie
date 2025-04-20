package com.myapp.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.myapp.myapplication.activity_view.AddingSetActivity
import com.myapp.myapplication.data_access_layer.model.ExerciseSetDisplay
import androidx.core.graphics.toColorInt

class TrainingExercisesListAdapter (private val context: Context, private var tickClickListener: (Int, ExerciseSetDisplay, Boolean) -> Unit) :
    ListAdapter<ExerciseSetDisplay, TrainingExercisesListAdapter.TrainingExercisesViewHolder>(TrainingExercisesComparator()) {

    private var currentExerciseIndex: Int = -1
    private var endTime = Long.MAX_VALUE
    private var isTimerOn = false
    private var countDownTimer: CountDownTimer? = null
    fun setCurrentExercise(index: Int) {
        endTime = Long.MAX_VALUE
        if(countDownTimer != null){
            countDownTimer?.cancel()
        }
        val previousIndex = currentExerciseIndex
        currentExerciseIndex = index
        notifyItemChanged(previousIndex)
        notifyItemChanged(currentExerciseIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingExercisesViewHolder {
        endTime = Long.MAX_VALUE
        if(countDownTimer != null){
            countDownTimer?.cancel()
        }
        return TrainingExercisesViewHolder.create(parent)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: TrainingExercisesViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, position == currentExerciseIndex, tickClickListener)

        holder.itemView.setOnClickListener {
                if(currentExerciseIndex == -1){
                    val intent = Intent(context, AddingSetActivity::class.java)
                    intent.putExtra("currentItemId", current.id)
                    context.startActivity(intent)
                }
        }

        if (current.time != null && position == currentExerciseIndex) {
            holder.tickButton.visibility = View.GONE
            holder.timerTextView.visibility = View.VISIBLE

            var timeLeft = endTime - System.currentTimeMillis()
            if(timeLeft <= 0){
                holder.tickButton.visibility = View.VISIBLE
                holder.timerTextView.visibility = View.GONE
            }
            else if(endTime == Long.MAX_VALUE){
                val totalMillis = current.time * 1000L
                endTime = System.currentTimeMillis() + totalMillis;
                timeLeft = endTime - System.currentTimeMillis()
            }
            countDownTimer = object : CountDownTimer(timeLeft, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = millisUntilFinished / 1000
                    val minutes = seconds / 60
                    val secs = seconds % 60
                    holder.timerTextView.text = String.format("%02d:%02d", minutes, secs)
                }

                override fun onFinish() {
                    holder.timerTextView.visibility = View.GONE
                    holder.tickButton.visibility = View.VISIBLE
                }
            }
            countDownTimer?.start()
        } else if (position == currentExerciseIndex) {
            holder.tickButton.visibility = View.VISIBLE
            holder.timerTextView.visibility = View.GONE
        } else {
            holder.tickButton.visibility = View.GONE
            holder.timerTextView.visibility = View.GONE
        }
    }

    class TrainingExercisesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val materialCardView: MaterialCardView = itemView.findViewById(R.id.materialcardview)
        private val exerciseNameTextView: TextView = itemView.findViewById(R.id.exerciseNameTextView)
        private val exerciseDetailsTextView: TextView = itemView.findViewById(R.id.exerciseDetailsTextView)
        val tickButton: ImageButton = itemView.findViewById(R.id.tickButton)
        val timerTextView: TextView = itemView.findViewById(R.id.timerTextView)

        fun bind(item: ExerciseSetDisplay, isCurrent: Boolean, tickClickListener: (Int, ExerciseSetDisplay, Boolean) -> Unit) {
            exerciseNameTextView.text = item.exerciseName
            exerciseDetailsTextView.text = buildString {
                if (item.reps != null) append("Powtórzenia: ${item.reps}  ")
                if (item.weight != null) append("Ciężar: ${item.weight}kg  ")
                if (item.time != null) append("Czas: ${item.time}s  ")
                if (item.distance != null) append("Dystans: ${item.distance}m")

                materialCardView.setCardBackgroundColor(
                    if (isCurrent) ContextCompat.getColor(itemView.context, R.color.activeSet)
                    else ContextCompat.getColor(itemView.context, R.color.colorPrimary)
                )

                tickButton.setOnClickListener {
                    tickClickListener.invoke(adapterPosition, item, timerTextView.isVisible)
                }
                timerTextView.setOnClickListener{
                    tickClickListener.invoke(adapterPosition, item, timerTextView.isVisible)}
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