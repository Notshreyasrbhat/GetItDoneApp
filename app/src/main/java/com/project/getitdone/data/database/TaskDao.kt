package com.project.getitdone.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.project.getitdone.data.model.Task
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao {

    @Insert
    suspend fun createTask(task: Task)

    @Query("SELECT * FROM `task` WHERE `list_id` = :taskListId")
    fun getAllTasks(taskListId: Int): Flow<List<Task>>

    @Query("SELECT * FROM `task` WHERE `is_starred` = 1")
    fun getStarredTasks(): Flow<List<Task>>


    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

}

