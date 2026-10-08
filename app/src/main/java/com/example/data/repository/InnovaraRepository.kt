package com.example.data.repository

import com.example.data.local.dao.InnovaraDao
import com.example.data.local.entity.ActivityEntity
import com.example.data.local.entity.DevelopmentEntity
import com.example.data.local.entity.NoteEntity
import com.example.data.local.entity.ProjectEntity
import com.example.data.local.entity.TaskEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.VersionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class InnovaraRepository(private val dao: InnovaraDao) {

  val allUsers: Flow<List<UserEntity>> = dao.getAllUsers()
  val allActivities: Flow<List<ActivityEntity>> = dao.getAllActivities()
  val allNotes: Flow<List<NoteEntity>> = dao.getAllNotes()
  val allProjects: Flow<List<ProjectEntity>> = dao.getAllProjects()
  val allTasks: Flow<List<TaskEntity>> = dao.getAllTasks()
  val allDevelopments: Flow<List<DevelopmentEntity>> = dao.getAllDevelopments()

  suspend fun ensureSeeded() {
    // Live data mode: no hardcoded seed data
  }

  fun getUserById(id: String): Flow<UserEntity?> = dao.getUserById(id)

  suspend fun getOrCreateUserByName(name: String): UserEntity {
    val existing = dao.getUserByName(name.trim())
    if (existing != null) return existing

    val newId = "user_" + UUID.randomUUID().toString().take(8)
    val initials = name.trim().take(1).uppercase()
    val palette = listOf("#F5B041", "#8B7CF6", "#38BDF8", "#34D399", "#FB7185", "#F472B6")
    val color = palette[Math.abs(name.hashCode()) % palette.size]

    val newUser = UserEntity(
      id = newId,
      name = name.trim(),
      field = "Innovator & Student",
      bio = "Welcome to my digital bookshelf on Innovara Notes.",
      quote = "Learning, building, and exploring new frontiers.",
      avatarColorHex = color,
      interests = "Innovation · Research · Technology"
    )
    dao.insertUser(newUser)
    logActivity(newUser.id, newUser.name, "joined Innovara Notes", "USER", newUser.name, newUser.id)
    return newUser
  }

  suspend fun updateUser(user: UserEntity) {
    dao.insertUser(user)
  }

  fun getNotesForUser(userId: String, isOwner: Boolean): Flow<List<NoteEntity>> {
    return if (isOwner) {
      dao.getAllNotesForUser(userId)
    } else {
      dao.getPublicNotesForUser(userId)
    }
  }

  fun getProjectsForUser(userId: String): Flow<List<ProjectEntity>> = dao.getProjectsForUser(userId)
  fun getProjectById(id: String): Flow<ProjectEntity?> = dao.getProjectById(id)

  fun getTasksForUser(userId: String): Flow<List<TaskEntity>> = dao.getTasksForUser(userId)
  fun getDevelopmentsForUser(userId: String): Flow<List<DevelopmentEntity>> = dao.getDevelopmentsForUser(userId)
  fun getActivitiesForUser(userId: String): Flow<List<ActivityEntity>> = dao.getActivitiesForUser(userId)
  fun getVersionsForTarget(targetId: String): Flow<List<VersionEntity>> = dao.getVersionsForTarget(targetId)

  // Save Note with Version History
  suspend fun saveNote(note: NoteEntity, authorName: String) {
    val existing = dao.getNoteById(note.id).firstOrNull()
    dao.insertNote(note)

    val verCount = dao.getVersionCount(note.id) + 1
    val snippet = if (note.title.isNotEmpty()) "${note.title}: ${note.content.take(60)}" else note.content.take(60)
    dao.insertVersion(
      VersionEntity(
        id = UUID.randomUUID().toString(),
        targetType = "NOTE",
        targetId = note.id,
        versionNumber = verCount,
        contentSnippet = snippet,
        editedBy = authorName
      )
    )

    val action = if (existing == null) "added a new ${note.type.lowercase()}" else "updated ${note.type.lowercase()}"
    logActivity(note.userId, authorName, action, note.type, note.title, note.id)
  }

  suspend fun deleteNote(id: String) {
    dao.deleteNote(id)
  }

  // Save Project with Version History
  suspend fun saveProject(project: ProjectEntity, authorName: String) {
    val existing = dao.getProjectById(project.id).firstOrNull()
    dao.insertProject(project)

    val verCount = dao.getVersionCount(project.id) + 1
    dao.insertVersion(
      VersionEntity(
        id = UUID.randomUUID().toString(),
        targetType = "PROJECT",
        targetId = project.id,
        versionNumber = verCount,
        contentSnippet = "Progress ${project.progressPercent}% - ${project.status} - ${project.name}",
        editedBy = authorName
      )
    )

    val action = if (existing == null) "created new project" else "updated project"
    logActivity(project.userId, authorName, action, "PROJECT", project.name, project.id)
  }

  suspend fun deleteProject(id: String) {
    dao.deleteProject(id)
  }

  // Tasks
  suspend fun saveTask(task: TaskEntity, authorName: String) {
    dao.insertTask(task)
    logActivity(task.userId, authorName, "created task", "TASK", task.title, task.id)
  }

  suspend fun toggleTask(task: TaskEntity, authorName: String) {
    val newStatus = if (task.status == "COMPLETED") "TODO" else "COMPLETED"
    val updated = task.copy(status = newStatus, updatedAt = System.currentTimeMillis())
    dao.updateTask(updated)
    val action = if (newStatus == "COMPLETED") "completed a task" else "reopened a task"
    logActivity(task.userId, authorName, action, "TASK", task.title, task.id)
  }

  suspend fun deleteTask(id: String) {
    dao.deleteTask(id)
  }

  // Developments
  suspend fun saveDevelopment(dev: DevelopmentEntity, authorName: String) {
    dao.insertDevelopment(dev)
    val verCount = dao.getVersionCount(dev.id) + 1
    dao.insertVersion(
      VersionEntity(
        id = UUID.randomUUID().toString(),
        targetType = "DEV_LOG",
        targetId = dev.id,
        versionNumber = verCount,
        contentSnippet = dev.whatChanged.take(80),
        editedBy = authorName
      )
    )
    logActivity(dev.userId, authorName, "logged development update", "DEV_LOG", dev.title, dev.id)
  }

  suspend fun deleteDevelopment(id: String) {
    dao.deleteDevelopment(id)
  }

  private suspend fun logActivity(
    userId: String,
    userName: String,
    action: String,
    targetType: String,
    targetTitle: String,
    targetId: String
  ) {
    dao.insertActivity(
      ActivityEntity(
        id = UUID.randomUUID().toString(),
        userId = userId,
        userName = userName,
        action = action,
        targetType = targetType,
        targetTitle = targetTitle,
        targetId = targetId,
        timestamp = System.currentTimeMillis()
      )
    )
  }

  fun searchUsers(query: String): Flow<List<UserEntity>> = dao.searchUsers(query)
  fun searchNotes(query: String): Flow<List<NoteEntity>> = dao.searchNotes(query)
  fun searchProjects(query: String): Flow<List<ProjectEntity>> = dao.searchProjects(query)
}
