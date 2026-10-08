package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.InnovaraDatabase
import com.example.data.local.entity.ActivityEntity
import com.example.data.local.entity.DevelopmentEntity
import com.example.data.local.entity.NoteEntity
import com.example.data.local.entity.ProjectEntity
import com.example.data.local.entity.TaskEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.VersionEntity
import com.example.data.repository.InnovaraRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
  object Landing : Screen()
  object Dashboard : Screen()
  object Bookshelf : Screen()
  data class StudentBook(val userId: String) : Screen()
  object CommunityFeed : Screen()
  object GlobalSearch : Screen()
}

class InnovaraViewModel(application: Application) : AndroidViewModel(application) {
  private val database = InnovaraDatabase.getDatabase(application)
  private val repository = InnovaraRepository(database.innovaraDao())

  val allUsers: StateFlow<List<UserEntity>> = repository.allUsers.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  val allActivities: StateFlow<List<ActivityEntity>> = repository.allActivities.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList()
  )

  private val _currentUser = MutableStateFlow<UserEntity?>(null)
  val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

  private val _activeViewingUser = MutableStateFlow<UserEntity?>(null)
  val activeViewingUser: StateFlow<UserEntity?> = _activeViewingUser.asStateFlow()

  private val _currentScreen = MutableStateFlow<Screen>(Screen.Landing)
  val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

  private val _screenStack = mutableListOf<Screen>()

  val bookshelfQuery = MutableStateFlow("")
  val bookshelfFilter = MutableStateFlow("All")

  val globalSearchQuery = MutableStateFlow("")

  val autosaveStatus = MutableStateFlow("Saved ✓")

  init {
    viewModelScope.launch {
      repository.ensureSeeded()
    }
  }

  fun navigateTo(screen: Screen) {
    _screenStack.add(_currentScreen.value)
    _currentScreen.value = screen
  }

  fun navigateBack(): Boolean {
    if (_screenStack.isNotEmpty()) {
      _currentScreen.value = _screenStack.removeAt(_screenStack.size - 1)
      return true
    }
    if (_currentScreen.value !is Screen.Dashboard && _currentUser.value != null) {
      _currentScreen.value = Screen.Dashboard
      return true
    }
    return false
  }

  fun enterAsUser(name: String) {
    viewModelScope.launch {
      val user = repository.getOrCreateUserByName(name)
      _currentUser.value = user
      _activeViewingUser.value = user
      _screenStack.clear()
      _currentScreen.value = Screen.Dashboard
    }
  }

  fun openStudentShelf(user: UserEntity) {
    _activeViewingUser.value = user
    navigateTo(Screen.StudentBook(user.id))
  }

  fun switchActiveUser(user: UserEntity) {
    _currentUser.value = user
  }

  // Active user's data flows
  @OptIn(ExperimentalCoroutinesApi::class)
  val viewingUserNotes: StateFlow<List<NoteEntity>> = combine(
    _activeViewingUser,
    _currentUser
  ) { viewing, current ->
    Pair(viewing, current)
  }.flatMapLatest { (viewing, current) ->
    if (viewing == null) flowOf(emptyList())
    else repository.getNotesForUser(viewing.id, isOwner = current?.id == viewing.id)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  @OptIn(ExperimentalCoroutinesApi::class)
  val viewingUserProjects: StateFlow<List<ProjectEntity>> = _activeViewingUser.flatMapLatest { user ->
    if (user == null) flowOf(emptyList())
    else repository.getProjectsForUser(user.id)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  @OptIn(ExperimentalCoroutinesApi::class)
  val viewingUserTasks: StateFlow<List<TaskEntity>> = _activeViewingUser.flatMapLatest { user ->
    if (user == null) flowOf(emptyList())
    else repository.getTasksForUser(user.id)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  @OptIn(ExperimentalCoroutinesApi::class)
  val viewingUserDevelopments: StateFlow<List<DevelopmentEntity>> = _activeViewingUser.flatMapLatest { user ->
    if (user == null) flowOf(emptyList())
    else repository.getDevelopmentsForUser(user.id)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  @OptIn(ExperimentalCoroutinesApi::class)
  val viewingUserActivities: StateFlow<List<ActivityEntity>> = _activeViewingUser.flatMapLatest { user ->
    if (user == null) flowOf(emptyList())
    else repository.getActivitiesForUser(user.id)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Mutations
  fun saveNote(note: NoteEntity) {
    viewModelScope.launch {
      autosaveStatus.value = "Saving..."
      val author = _currentUser.value?.name ?: "Innovator"
      repository.saveNote(note, author)
      delay(300)
      autosaveStatus.value = "Saved ✓"
    }
  }

  fun deleteNote(id: String) {
    viewModelScope.launch {
      repository.deleteNote(id)
    }
  }

  fun saveProject(project: ProjectEntity) {
    viewModelScope.launch {
      val author = _currentUser.value?.name ?: "Innovator"
      repository.saveProject(project, author)
    }
  }

  fun deleteProject(id: String) {
    viewModelScope.launch {
      repository.deleteProject(id)
    }
  }

  fun saveTask(task: TaskEntity) {
    viewModelScope.launch {
      val author = _currentUser.value?.name ?: "Innovator"
      repository.saveTask(task, author)
    }
  }

  fun toggleTask(task: TaskEntity) {
    viewModelScope.launch {
      val author = _currentUser.value?.name ?: "Innovator"
      repository.toggleTask(task, author)
    }
  }

  fun deleteTask(id: String) {
    viewModelScope.launch {
      repository.deleteTask(id)
    }
  }

  fun saveDevelopment(dev: DevelopmentEntity) {
    viewModelScope.launch {
      val author = _currentUser.value?.name ?: "Innovator"
      repository.saveDevelopment(dev, author)
    }
  }

  fun deleteDevelopment(id: String) {
    viewModelScope.launch {
      repository.deleteDevelopment(id)
    }
  }

  fun updateUser(user: UserEntity) {
    viewModelScope.launch {
      repository.updateUser(user)
      if (_currentUser.value?.id == user.id) {
        _currentUser.value = user
      }
      if (_activeViewingUser.value?.id == user.id) {
        _activeViewingUser.value = user
      }
    }
  }

  fun getVersionsForTarget(targetId: String) = repository.getVersionsForTarget(targetId)

  @OptIn(ExperimentalCoroutinesApi::class)
  val searchResultsNotes: StateFlow<List<NoteEntity>> = globalSearchQuery.flatMapLatest { q ->
    if (q.isBlank()) flowOf(emptyList()) else repository.searchNotes(q)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  @OptIn(ExperimentalCoroutinesApi::class)
  val searchResultsProjects: StateFlow<List<ProjectEntity>> = globalSearchQuery.flatMapLatest { q ->
    if (q.isBlank()) flowOf(emptyList()) else repository.searchProjects(q)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  @OptIn(ExperimentalCoroutinesApi::class)
  val searchResultsUsers: StateFlow<List<UserEntity>> = globalSearchQuery.flatMapLatest { q ->
    if (q.isBlank()) flowOf(emptyList()) else repository.searchUsers(q)
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
