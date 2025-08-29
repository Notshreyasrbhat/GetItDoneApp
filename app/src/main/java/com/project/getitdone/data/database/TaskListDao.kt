package com.project.getitdone.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.project.getitdone.data.model.TaskList
import kotlinx.coroutines.flow.Flow

// DAO for performing CRUD operations on TaskList table
@Dao
interface TaskListDao {

    @Insert
    suspend fun createTaskList(list: TaskList) // Inserts a new task list

    @Query("SELECT * FROM task_list")
    fun getAllLists(): Flow<List<TaskList>>
    // Retrieves all task lists as a Flow

    @Update
    suspend fun updateTaskList(task: TaskList) // Updates an existing task list

    @Delete
    suspend fun deleteTaskList(task: TaskList) // Deletes a specific task list
}
