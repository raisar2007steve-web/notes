package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "versions")
data class VersionEntity(
  @PrimaryKey val id: String,
  val targetType: String, // NOTE, PROJECT, DEV_LOG
  val targetId: String,
  val versionNumber: Int,
  val contentSnippet: String,
  val editedBy: String,
  val createdAt: Long = System.currentTimeMillis()
)
