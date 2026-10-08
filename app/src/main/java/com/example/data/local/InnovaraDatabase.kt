package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.InnovaraDao
import com.example.data.local.entity.ActivityEntity
import com.example.data.local.entity.DevelopmentEntity
import com.example.data.local.entity.NoteEntity
import com.example.data.local.entity.ProjectEntity
import com.example.data.local.entity.TaskEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.VersionEntity

@Database(
  entities = [
    UserEntity::class,
    NoteEntity::class,
    ProjectEntity::class,
    TaskEntity::class,
    DevelopmentEntity::class,
    ActivityEntity::class,
    VersionEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class InnovaraDatabase : RoomDatabase() {
  abstract fun innovaraDao(): InnovaraDao

  companion object {
    @Volatile
    private var INSTANCE: InnovaraDatabase? = null

    fun getDatabase(context: Context): InnovaraDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          InnovaraDatabase::class.java,
          "innovara_notes_db"
        )
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
