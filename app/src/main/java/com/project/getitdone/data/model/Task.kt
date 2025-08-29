package com.project.getitdone.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// Represents a single task in the database
@Entity(
    tableName = "task",
    foreignKeys = [
        ForeignKey(
            entity = TaskList::class, // Links task to a TaskList
            parentColumns = ["task_list_id"], // Column in TaskList
            childColumns = ["list_id"],       // Corresponding column in Task
            onDelete = ForeignKey.CASCADE     // Deletes tasks if parent list is deleted
        )
    ]
)
data class Task(
    @ColumnInfo(name = "task_id")
    @PrimaryKey(autoGenerate = true) // Unique ID for each task, auto-generated
    val taskId: Int = 0,

    val title: String, // Task title

    val description: String? = null, // Optional task details

    @ColumnInfo(name = "is_starred")
    val isStarred: Boolean = false, // Marks task as important

    @ColumnInfo(name = "is_complete")
    val isComplete: Boolean = false, // Marks task as done

    @ColumnInfo(name = "list_id")
    val listId: Int // Foreign key linking to TaskList
)
