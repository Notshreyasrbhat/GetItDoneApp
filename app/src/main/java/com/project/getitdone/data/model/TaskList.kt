package com.project.getitdone.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents a list of tasks in the database
@Entity(tableName = "task_list")
data class TaskList(
    @ColumnInfo(name = "task_list_id")
    @PrimaryKey(autoGenerate = true) // Unique ID for each task list, auto-generated
    val id: Int = 0,

    val name: String, // Name/title of the task list
)
