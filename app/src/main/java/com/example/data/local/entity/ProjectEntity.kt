package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
  @PrimaryKey val id: String,
  val userId: String,
  val name: String,
  val tagline: String,
  val description: String,
  val goal: String,
  val problem: String,
  val solution: String,
  val technologies: String, // comma or bullet separated
  val progressPercent: Int = 0,
  val status: String, // Building, Researching, Deployed, Testing
  val repoUrl: String = "",
  val demoUrl: String = "",
  val markdownDoc: String = "",
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)
