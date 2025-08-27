package com.project.getitdone

import android.app.Application
import com.project.getitdone.data.TaskRepository
import com.project.getitdone.data.database.GetItDoneDatabase

class GetItDoneApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        val database = GetItDoneDatabase.getDataBase(this)
        val taskDao=database.getTaskDao()
        val taskListDao=database.getTaskListDao()
        taskRepository = TaskRepository(taskDao,taskListDao)
    }

    companion object {
        lateinit var taskRepository: TaskRepository
    }

}