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
import com.myapp.myapplication.activity_view.TrainingDetailsActivity
import com.myapp.myapplication.data_access_layer.model.Training

class TrainingListAdapter(private val context: Context): ListAdapter<Training, TrainingListAdapter.TrainingViewHolder>(TrainingsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        return TrainingViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.trainingName)

        holder.itemView.setOnClickListener {view ->
                val intent = Intent(context, TrainingDetailsActivity::class.java)
                intent.putExtra("currentName", current.trainingName)
                intent.putExtra("currentId", current.id)
                context.startActivity(intent)
        }
    }

    class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?) {
            wordItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): TrainingViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return TrainingViewHolder(view)
            }
        }
    }

    class TrainingsComparator : DiffUtil.ItemCallback<Training>() {
        override fun areItemsTheSame(oldItem: Training, newItem: Training): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Training, newItem: Training): Boolean {
            return oldItem.trainingName == newItem.trainingName
        }
    }
}
