package com.project.getitdone.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.getitdone.GetItDoneApplication
import com.project.getitdone.data.TaskRepository
import com.project.getitdone.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class StarrredTaskViewModel : ViewModel() {

   private val repository: TaskRepository = GetItDoneApplication.taskRepository

    fun fetchTasks(): Flow<List<Task>> {
        return repository.getStarredTasks()

    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
