package com.project.getitdone.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.project.getitdone.data.model.Task
import com.project.getitdone.databinding.FragmentTasksBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Fragment that displays all tasks belonging to a specific TaskList.
// Handles rendering the list via RecyclerView + TasksAdapter.
class TasksFragment(private val taskListId: Int) : Fragment(), TasksAdapter.TaskItemClickListener {

    // Scoped ViewModel for handling data operations for tasks
    private val viewModel : TaskViewModel by viewModels()

    // ViewBinding for fragment layout
    private lateinit var binding: FragmentTasksBinding

    // Adapter for rendering tasks in RecyclerView, with callbacks for update/delete
    private val adapter = TasksAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Attach adapter to RecyclerView
        binding.recyclerView.adapter = adapter

        // Start collecting tasks for this list
        fetchAllTasks()
    }

    /**
     * Collects tasks for the given taskListId.
     * Uses Flow so UI auto-refreshes whenever DB data changes.
     */
    private fun fetchAllTasks() {
        lifecycleScope.launch {
            viewModel.fetchTasks(taskListId).collectLatest { tasks ->
                adapter.setTasks(tasks) // Update adapter whenever data changes
            }
        }
    }

    /**
     * Callback when a task is updated (e.g. marked complete, edited).
     * Delegates persistence logic to ViewModel.
     */
    override fun onTaskUpdated(task: Task) {
        viewModel.updateTask(task)
    }

    /**
     * Callback when a task is deleted by user.
     * Delegates deletion logic to ViewModel.
     */
    override fun onTaskDeleted(task: Task) {
        viewModel.deleteTask(task)
    }
}
