package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
  @PrimaryKey val id: String,
  val userId: String,
  val userName: String,
  val action: String, // "added a new idea", "updated project", "completed task", etc.
  val targetType: String, // IDEA, PROJECT, TASK, THOUGHT, DEV_LOG, NOTE
  val targetTitle: String,
  val targetId: String,
  val timestamp: Long = System.currentTimeMillis()
)
