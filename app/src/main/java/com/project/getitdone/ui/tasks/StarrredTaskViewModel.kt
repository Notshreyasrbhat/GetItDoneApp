package com.project.getitdone.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.getitdone.GetItDoneApplication
import com.project.getitdone.data.TaskRepository
import com.project.getitdone.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

// ViewModel dedicated to managing only Starred tasks.
// Works similar to TaskViewModel, but fetches from repository's starred query.
class StarredTaskViewModel : ViewModel() {

    // Repository instance shared across the app (singleton).
    private val repository: TaskRepository = GetItDoneApplication.taskRepository

    /**
     * Fetches all starred tasks from the database.
     * Returns a Flow, so UI auto-updates whenever starred tasks change.
     */
    fun fetchTasks(): Flow<List<Task>> {
        return repository.getStarredTasks()
    }

    /**
     * Updates a given starred task in the database.
     * Example: un-starring or marking as complete.
     */
    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    /**
     * Deletes a starred task from the database.
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
