package com.project.getitdone.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.getitdone.GetItDoneApplication
import com.project.getitdone.data.TaskRepository
import com.project.getitdone.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    // Repository instance (shared across app via Application singleton).
    // Handles all task CRUD operations for a given task list.
    private val repository: TaskRepository = GetItDoneApplication.taskRepository

    /**
     * Fetches all tasks belonging to a specific TaskList.
     * Returns a Flow so UI can react to real-time DB changes.
     */
    fun fetchTasks(taskListId: Int): Flow<List<Task>> {
        return repository.getTasks(taskListId)
    }

    /**
     * Updates an existing task in the database.
     * Example: marking it completed, editing title, etc.
     */
    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    /**
     * Deletes a task permanently from the database.
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
