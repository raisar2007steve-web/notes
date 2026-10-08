package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "developments")
data class DevelopmentEntity(
  @PrimaryKey val id: String,
  val userId: String,
  val projectId: String = "",
  val title: String,
  val dateStr: String,
  val whatChanged: String,
  val whatWorked: String,
  val whatFailed: String = "",
  val nextStep: String,
  val status: String = "In Progress",
  val createdAt: Long = System.currentTimeMillis()
)
