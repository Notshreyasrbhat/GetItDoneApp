package com.project.getitdone.data

import com.project.getitdone.data.database.TaskDao
import com.project.getitdone.data.database.TaskListDao
import com.project.getitdone.data.model.Task
import com.project.getitdone.data.model.TaskList
import kotlinx.coroutines.flow.Flow

// Repository acts as a single source of truth for tasks & task lists
class TaskRepository(
    private val taskDao: TaskDao,
    private val taskListDao: TaskListDao
) {

    suspend fun createTask(task: Task) {
        taskDao.createTask(task)
    }

    fun getTasks(taskListId: Int): Flow<List<Task>> =
        taskDao.getAllTasks(taskListId)

    fun getTaskLists(): Flow<List<TaskList>> =
        taskListDao.getAllLists()

    fun getStarredTasks(): Flow<List<Task>> =
        taskDao.getStarredTasks()

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun createTaskList(listName: String) {
        val taskList = TaskList(name = listName)
        taskListDao.createTaskList(taskList)
    }
}
