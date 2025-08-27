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

class TasksFragment(private val taskListId: Int) : Fragment(), TasksAdapter.TaskItemClickListener {

    private val viewModel : TaskViewModel by viewModels()
    private lateinit var binding: FragmentTasksBinding
    private val adapter = TasksAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentTasksBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter=adapter
        fetchAllTasks()
    }

    private fun fetchAllTasks() {
        lifecycleScope.launch {
            viewModel.fetchTasks(taskListId).collectLatest { tasks ->
                adapter.setTasks(tasks)
            }

        }
    }

    override fun onTaskUpdated(task: Task) {
         viewModel.updateTask(task)
    }

    override fun onTaskDeleted(task: Task) {
        viewModel.deleteTask(task)
    }
}