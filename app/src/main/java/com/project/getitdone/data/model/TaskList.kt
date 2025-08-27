package com.project.getitdone.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_list")
data class TaskList(
    @ColumnInfo(name = "task_list_id") @PrimaryKey(autoGenerate = true) val id : Int=0,
    val name : String,
)
