package com.project.getitdone.data

import com.project.getitdone.data.database.TaskDao
import com.project.getitdone.data.database.TaskListDao
import com.project.getitdone.data.model.Task
import com.project.getitdone.data.model.TaskList
import kotlinx.coroutines.flow.Flow

class TaskRepository(private  val taskDao: TaskDao,private val taskListDao: TaskListDao){

    suspend fun createTask(task: Task) {
        taskDao.createTask(task)
    }

    fun getTasks(taskListId: Int): Flow<List<Task>> {
         return taskDao.getAllTasks(taskListId)
    }
    fun getTaskLists(): Flow<List<TaskList>> {
        return taskListDao.getAllLists()
    }

    fun getStarredTasks(): Flow<List<Task>> {
         return taskDao.getStarredTasks()
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task:Task){
       taskDao.deleteTask(task)
    }

    suspend fun createTaskList(listName: String) {
        val taskList = TaskList(name = listName)
        taskListDao.createTaskList(taskList)

    }


}