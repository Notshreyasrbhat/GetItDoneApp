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

/**
 * Fragment responsible for displaying only the starred tasks in a RecyclerView.
 * Uses [StarredTaskViewModel] to fetch, update and delete tasks.
 */
class StarredTasksFragment : Fragment(), TasksAdapter.TaskItemClickListener {

    // ViewModel instance scoped to this fragment's lifecycle
    private val viewModel: StarredTaskViewModel by viewModels()

    // View binding for fragment layout
    private lateinit var binding: FragmentTasksBinding

    // Adapter for RecyclerView, handles displaying list of tasks
    private val adapter = TasksAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout and initialize binding
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set adapter for RecyclerView
        binding.recyclerView.adapter = adapter

        // Start observing starred tasks
        fetchStarredTasks()
    }

    /**
     * Collects starred tasks from ViewModel and updates adapter whenever data changes.
     * Uses viewLifecycleOwner.lifecycleScope to avoid memory leaks when fragment view is destroyed.
     */
    private fun fetchStarredTasks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchTasks().collectLatest { tasks ->
                adapter.setTasks(tasks)
            }
        }
    }

    /**
     * Handles when user updates a task (e.g., marking complete or toggling star).
     */
    override fun onTaskUpdated(task: Task) {
        viewModel.updateTask(task)
    }

    /**
     * Handles when user deletes a task (via long press).
     */
    override fun onTaskDeleted(task: Task) {
        viewModel.deleteTask(task)
    }
}
