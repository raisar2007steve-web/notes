package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
  @PrimaryKey val id: String,
  val userId: String,
  val type: String, // IDEA, THOUGHT, RESEARCH, LEARNING, GENERAL
  val title: String,
  val content: String, // Markdown content
  val category: String,
  val status: String, // e.g. "💭 Idea", "🔬 Researching", "🛠 Building", "🧪 Testing", "🚀 Deployed", "✅ Completed", "⏸ Paused"
  val tags: String, // comma-separated
  val visibility: String, // PUBLIC, PRIVATE
  val attachmentName: String = "",
  val attachmentType: String = "", // IMAGE, PDF, CODE
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)
