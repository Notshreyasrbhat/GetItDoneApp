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

    private val repository : TaskRepository = GetItDoneApplication.taskRepository

    fun getTaskLists() : Flow<List<TaskList>> = repository.getTaskLists()

    fun createTask(title: String ,description: String?,listId: Int)
    {

        val task = Task(
            title = title,
            description = description,
            listId = listId
        )
        viewModelScope.launch {
            repository.createTask(task)

        }

    }

    fun addNewTaskList(listName: String?) {
        if (listName.isNullOrBlank()) return
        viewModelScope.launch {
            repository.createTaskList(listName)
        }
    }

}