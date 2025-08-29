package com.project.getitdone.ui.tasks

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.getitdone.data.model.Task
import com.project.getitdone.databinding.ItemTaskBinding

// Adapter for displaying a list of tasks in RecyclerView.
// Handles binding of data + forwarding user actions back to Fragment via listener.
class TasksAdapter(private val listener: TaskItemClickListener) :
    RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    // Internal task list (always sorted before displaying).
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

    /**
     * Updates the tasks list and refreshes the UI.
     * Sorting ensures incomplete tasks are shown before completed ones.
     * (Currently uses notifyDataSetChanged — could be optimized with DiffUtil for large lists.)
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks.sortedBy { it.isComplete } // Incomplete first, completed later
        notifyDataSetChanged()
    }

    // ViewHolder for a single Task item.
    // Responsible for rendering task details + handling interactions.
    inner class ViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {

                // Long press on task → delete
                root.setOnLongClickListener {
                    listener.onTaskDeleted(task)
                    true
                }

                // Bind completion + starred states
                checkBox.isChecked = task.isComplete
                toggleStar.isChecked = task.isStarred

                // Apply strikethrough if task is completed
                if (task.isComplete) {
                    textViewTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    textViewDetails.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    textViewTitle.paintFlags = 0
                    textViewDetails.paintFlags = 0
                }

                // Bind title
                textViewTitle.text = task.title

                // Show/hide description depending on value
                if (task.description.isNullOrEmpty()) {
                    textViewDetails.visibility = View.GONE
                } else {
                    textViewDetails.text = task.description
                    textViewDetails.visibility = View.VISIBLE
                }

                // Checkbox toggle → update completion state
                checkBox.setOnClickListener {
                    val upDatedTask = task.copy(isComplete = checkBox.isChecked)
                    listener.onTaskUpdated(upDatedTask)
                }

                // Star toggle → update starred state
                toggleStar.setOnClickListener {
                    val upDatedTask = task.copy(isStarred = toggleStar.isChecked)
                    listener.onTaskUpdated(upDatedTask)
                }
            }
        }
    }

    /**
     * Listener interface to forward user actions (update/delete) back to Fragment.
     * Keeps adapter UI-focused without handling DB logic directly.
     */
    interface TaskItemClickListener {
        fun onTaskUpdated(task: Task)
        fun onTaskDeleted(task: Task)
    }
}
