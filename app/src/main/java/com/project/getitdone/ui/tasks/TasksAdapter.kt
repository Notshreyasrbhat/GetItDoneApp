package com.project.getitdone.ui.tasks

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.getitdone.data.model.Task
import com.project.getitdone.databinding.ItemTaskBinding


class TasksAdapter(private val listener: TaskItemClickListener) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    private var tasks: List<Task> = listOf()

    override fun getItemCount() = tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTaskBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTasks(tasks: List<Task>) {

        this.tasks = tasks.sortedBy { it.isComplete }
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                root.setOnLongClickListener {
                    listener.onTaskDeleted(task)
                    true  }
                checkBox.isChecked = task.isComplete
                toggleStar.isChecked = task.isStarred
                if (task.isComplete) {
                    textViewTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    textViewDetails.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    textViewTitle.paintFlags = 0
                    textViewDetails.paintFlags = 0
                }
                textViewTitle.text = task.title
                if (task.description.isNullOrEmpty()) {
                    textViewDetails.visibility = View.GONE
                } else {
                    textViewDetails.text = task.description
                    textViewDetails.visibility = View.VISIBLE

                }
                checkBox.setOnClickListener {
                    val upDatedTask = task.copy(isComplete = checkBox.isChecked)
                    listener.onTaskUpdated(upDatedTask)
                }
                toggleStar.setOnClickListener {
                    val upDatedTask = task.copy(isStarred = toggleStar.isChecked)
                    listener.onTaskUpdated(upDatedTask)
                }


            }

        }
    }

    interface TaskItemClickListener {

        fun onTaskUpdated(task: Task)

        fun onTaskDeleted(task: Task)
    }
}
