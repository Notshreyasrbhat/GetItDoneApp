package com.project.getitdone.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.getitdone.GetItDoneApplication
import com.project.getitdone.data.TaskRepository
import com.project.getitdone.data.model.Task
import com.project.getitdone.data.model.TaskList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel(){

    // Repository instance for handling all task-related data operations.
    // Pulled from Application class to ensure it's a singleton shared across app.
    private val repository : TaskRepository = GetItDoneApplication.taskRepository

    /**
     * Exposes all TaskLists as a Flow.
     * UI can collect this to auto-update when task lists change in DB.
     */
    fun getTaskLists() : Flow<List<TaskList>> = repository.getTaskLists()

    /**
     * Creates a new Task inside the given TaskList.
     * Runs inside viewModelScope (lifecycle-aware) so it won't leak coroutines.
     */
    fun createTask(title: String, description: String?, listId: Int) {
        val task = Task(
            title = title,
            description = description,
            listId = listId
        )
        viewModelScope.launch {
            repository.createTask(task)
        }
    }

    /**
     * Adds a new TaskList if the name is valid (non-null, non-empty).
     * Launches in coroutine scope to call repository asynchronously.
     */
    fun addNewTaskList(listName: String?) {
        if (listName.isNullOrBlank()) return
        viewModelScope.launch {
            repository.createTaskList(listName)
        }
    }
}
