package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ActivityEntity
import com.example.data.local.entity.DevelopmentEntity
import com.example.data.local.entity.NoteEntity
import com.example.data.local.entity.ProjectEntity
import com.example.data.local.entity.TaskEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.VersionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InnovaraDao {
  // Users
  @Query("SELECT * FROM users ORDER BY updatedAt DESC")
  fun getAllUsers(): Flow<List<UserEntity>>

  @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
  fun getUserById(id: String): Flow<UserEntity?>

  @Query("SELECT * FROM users WHERE LOWER(name) = LOWER(:name) LIMIT 1")
  suspend fun getUserByName(name: String): UserEntity?

  @Query("SELECT * FROM users WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR LOWER(field) LIKE '%' || LOWER(:query) || '%'")
  fun searchUsers(query: String): Flow<List<UserEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUser(user: UserEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertUsers(users: List<UserEntity>)

  // Notes (Ideas, Thoughts, General, Research, Learning)
  @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY updatedAt DESC")
  fun getAllNotesForUser(userId: String): Flow<List<NoteEntity>>

  @Query("SELECT * FROM notes WHERE userId = :userId AND visibility = 'PUBLIC' ORDER BY updatedAt DESC")
  fun getPublicNotesForUser(userId: String): Flow<List<NoteEntity>>

  @Query("SELECT * FROM notes WHERE userId = :userId AND type = :type ORDER BY updatedAt DESC")
  fun getNotesByTypeForUser(userId: String, type: String): Flow<List<NoteEntity>>

  @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
  fun getNoteById(id: String): Flow<NoteEntity?>

  @Query("SELECT * FROM notes WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%' OR LOWER(content) LIKE '%' || LOWER(:query) || '%' OR LOWER(tags) LIKE '%' || LOWER(:query) || '%' ORDER BY updatedAt DESC")
  fun searchNotes(query: String): Flow<List<NoteEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNote(note: NoteEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertNotes(notes: List<NoteEntity>)

  @Update
  suspend fun updateNote(note: NoteEntity)

  @Query("DELETE FROM notes WHERE id = :id")
  suspend fun deleteNote(id: String)

  // Projects
  @Query("SELECT * FROM projects WHERE userId = :userId ORDER BY updatedAt DESC")
  fun getProjectsForUser(userId: String): Flow<List<ProjectEntity>>

  @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
  fun getProjectById(id: String): Flow<ProjectEntity?>

  @Query("SELECT * FROM projects WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR LOWER(description) LIKE '%' || LOWER(:query) || '%' OR LOWER(technologies) LIKE '%' || LOWER(:query) || '%' ORDER BY updatedAt DESC")
  fun searchProjects(query: String): Flow<List<ProjectEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProject(project: ProjectEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertProjects(projects: List<ProjectEntity>)

  @Update
  suspend fun updateProject(project: ProjectEntity)

  @Query("DELETE FROM projects WHERE id = :id")
  suspend fun deleteProject(id: String)

  // Tasks
  @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY createdAt DESC")
  fun getTasksForUser(userId: String): Flow<List<TaskEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTask(task: TaskEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTasks(tasks: List<TaskEntity>)

  @Update
  suspend fun updateTask(task: TaskEntity)

  @Query("DELETE FROM tasks WHERE id = :id")
  suspend fun deleteTask(id: String)

  // Developments
  @Query("SELECT * FROM developments WHERE userId = :userId ORDER BY createdAt DESC")
  fun getDevelopmentsForUser(userId: String): Flow<List<DevelopmentEntity>>

  @Query("SELECT * FROM developments WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%' OR LOWER(whatChanged) LIKE '%' || LOWER(:query) || '%' ORDER BY createdAt DESC")
  fun searchDevelopments(query: String): Flow<List<DevelopmentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDevelopment(dev: DevelopmentEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertDevelopments(devs: List<DevelopmentEntity>)

  @Query("DELETE FROM developments WHERE id = :id")
  suspend fun deleteDevelopment(id: String)

  // Activities (Global community feed)
  @Query("SELECT * FROM activities ORDER BY timestamp DESC LIMIT 60")
  fun getAllActivities(): Flow<List<ActivityEntity>>

  @Query("SELECT * FROM activities WHERE userId = :userId ORDER BY timestamp DESC LIMIT 30")
  fun getActivitiesForUser(userId: String): Flow<List<ActivityEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertActivity(activity: ActivityEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertActivities(activities: List<ActivityEntity>)

  // Versions
  @Query("SELECT * FROM versions WHERE targetId = :targetId ORDER BY versionNumber DESC")
  fun getVersionsForTarget(targetId: String): Flow<List<VersionEntity>>

  @Query("SELECT COUNT(*) FROM versions WHERE targetId = :targetId")
  suspend fun getVersionCount(targetId: String): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertVersion(version: VersionEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertVersions(versions: List<VersionEntity>)
}
