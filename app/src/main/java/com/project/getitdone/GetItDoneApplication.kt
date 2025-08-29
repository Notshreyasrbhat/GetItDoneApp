package com.project.getitdone

import android.app.Application
import com.project.getitdone.data.TaskRepository
import com.project.getitdone.data.database.GetItDoneDatabase

class GetItDoneApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        // Build the Room database instance (singleton per app lifecycle).
        val database = GetItDoneDatabase.getDataBase(this)

        // Get DAO instances for tasks and task lists.
        val taskDao = database.getTaskDao()
        val taskListDao = database.getTaskListDao()

        // Create a single repository instance shared across the whole app.
        taskRepository = TaskRepository(taskDao, taskListDao)
    }

    companion object {
        // Exposed as a global singleton reference.
        // Allows ViewModels to access the repository without manual DI setup.
        lateinit var taskRepository: TaskRepository
    }
}
