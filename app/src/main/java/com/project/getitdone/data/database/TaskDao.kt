package com.project.getitdone.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.project.getitdone.data.model.Task
import kotlinx.coroutines.flow.Flow

// DAO for performing CRUD operations on Task table
@Dao
interface TaskDao {

    @Insert
    suspend fun createTask(task: Task) // Inserts a new task into the database

    @Query("SELECT * FROM `task` WHERE `list_id` = :taskListId")
    fun getAllTasks(taskListId: Int): Flow<List<Task>>
    // Retrieves all tasks belonging to a specific TaskList

    @Query("SELECT * FROM `task` WHERE `is_starred` = 1")
    fun getStarredTasks(): Flow<List<Task>>
    // Retrieves all tasks marked as starred

    @Update
    suspend fun updateTask(task: Task) // Updates an existing task

    @Delete
    suspend fun deleteTask(task: Task) // Deletes a specific task
}
