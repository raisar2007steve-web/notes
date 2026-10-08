package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
  @PrimaryKey val id: String,
  val userId: String,
  val title: String,
  val description: String = "",
  val priority: String = "MEDIUM", // HIGH, MEDIUM, LOW
  val status: String = "TODO", // TODO, IN_PROGRESS, COMPLETED
  val dueDate: String = "",
  val category: String = "General",
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)
