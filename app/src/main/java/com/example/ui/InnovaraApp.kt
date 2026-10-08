package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.entity.UserEntity
import com.example.ui.components.AddEntryDialog
import com.example.ui.screens.BookshelfScreen
import com.example.ui.screens.CommunityFeedScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GlobalSearchScreen
import com.example.ui.screens.LandingScreen
import com.example.ui.screens.StudentBookScreen
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.Graphite700
import com.example.ui.theme.Graphite800
import com.example.ui.theme.Graphite850
import com.example.ui.theme.Graphite900
import com.example.ui.theme.Graphite950
import com.example.ui.theme.TextHighEmphasis
import com.example.ui.theme.TextLowEmphasis
import com.example.ui.theme.TextMediumEmphasis
import com.example.ui.theme.VioletAccent

@Composable
fun InnovaraApp(
  viewModel: InnovaraViewModel = viewModel()
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val currentUser by viewModel.currentUser.collectAsState()
  val activeViewingUser by viewModel.activeViewingUser.collectAsState()
  val allUsers by viewModel.allUsers.collectAsState()
  val activities by viewModel.allActivities.collectAsState()
  val notes by viewModel.viewingUserNotes.collectAsState()
  val projects by viewModel.viewingUserProjects.collectAsState()
  val tasks by viewModel.viewingUserTasks.collectAsState()
  val developments by viewModel.viewingUserDevelopments.collectAsState()

  var showSwitchUserModal by remember { mutableStateOf(false) }
  var showGlobalAddDialog by remember { mutableStateOf(false) }

  // Handle hardware Back button
  BackHandler(enabled = currentScreen !is Screen.Landing && currentScreen !is Screen.Dashboard) {
    viewModel.navigateBack()
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .statusBarsPadding(),
    containerColor = Graphite950,
    bottomBar = {
      if (currentScreen !is Screen.Landing && currentUser != null) {
        NavigationBar(
          containerColor = Graphite900,
          contentColor = GoldPrimary,
          tonalElevation = 6.dp,
          modifier = Modifier
            .navigationBarsPadding()
            .border(1.dp, BorderSubtle)
            .testTag("innovara_bottom_nav")
        ) {
          NavigationBarItem(
            selected = currentScreen is Screen.Dashboard,
            onClick = { viewModel.navigateTo(Screen.Dashboard) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Home", fontSize = 11.sp) },
            colors = navItemColors(),
            modifier = Modifier.testTag("nav_home")
          )

          NavigationBarItem(
            selected = currentScreen is Screen.Bookshelf,
            onClick = { viewModel.navigateTo(Screen.Bookshelf) },
            icon = { Icon(Icons.Default.AutoStories, contentDescription = "Bookshelf") },
            label = { Text("Bookshelf", fontSize = 11.sp) },
            colors = navItemColors(),
            modifier = Modifier.testTag("nav_bookshelf")
          )

          NavigationBarItem(
            selected = currentScreen is Screen.StudentBook && activeViewingUser?.id == currentUser?.id,
            onClick = {
              currentUser?.let {
                viewModel.openStudentShelf(it)
              }
            },
            icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "My Shelf") },
            label = { Text("My Shelf", fontSize = 11.sp) },
            colors = navItemColors(),
            modifier = Modifier.testTag("nav_my_shelf")
          )

          NavigationBarItem(
            selected = currentScreen is Screen.CommunityFeed,
            onClick = { viewModel.navigateTo(Screen.CommunityFeed) },
            icon = { Icon(Icons.Default.Whatshot, contentDescription = "Activity") },
            label = { Text("Feed", fontSize = 11.sp) },
            colors = navItemColors(),
            modifier = Modifier.testTag("nav_feed")
          )

          NavigationBarItem(
            selected = currentScreen is Screen.GlobalSearch,
            onClick = { viewModel.navigateTo(Screen.GlobalSearch) },
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search", fontSize = 11.sp) },
            colors = navItemColors(),
            modifier = Modifier.testTag("nav_search")
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (val screen = currentScreen) {
        is Screen.Landing -> {
          LandingScreen(
            onEnter = { name ->
              viewModel.enterAsUser(name)
            }
          )
        }

        is Screen.Dashboard -> {
          if (currentUser != null) {
            DashboardScreen(
              currentUser = currentUser!!,
              allUsers = allUsers,
              recentActivities = activities,
              ideasCount = 12,
              projectsCount = 4,
              thoughtsCount = 27,
              tasksCount = 18,
              developmentsCount = 31,
              onOpenMyShelf = { viewModel.openStudentShelf(currentUser!!) },
              onOpenUserShelf = { user -> viewModel.openStudentShelf(user) },
              onOpenBookshelf = { viewModel.navigateTo(Screen.Bookshelf) },
              onOpenSearch = { viewModel.navigateTo(Screen.GlobalSearch) },
              onAddEntry = { showGlobalAddDialog = true }
            )
          }
        }

        is Screen.Bookshelf -> {
          BookshelfScreen(
            viewModel = viewModel,
            onOpenStudentBook = { user -> viewModel.openStudentShelf(user) }
          )
        }

        is Screen.StudentBook -> {
          val viewing = activeViewingUser ?: currentUser
          if (viewing != null) {
            StudentBookScreen(
              user = viewing,
              currentUser = currentUser,
              viewModel = viewModel,
              onBackToBookshelf = { viewModel.navigateTo(Screen.Bookshelf) }
            )
          }
        }

        is Screen.CommunityFeed -> {
          CommunityFeedScreen(
            viewModel = viewModel,
            onOpenUserShelf = { user -> viewModel.openStudentShelf(user) }
          )
        }

        is Screen.GlobalSearch -> {
          GlobalSearchScreen(
            viewModel = viewModel,
            onOpenUserShelf = { user -> viewModel.openStudentShelf(user) }
          )
        }
      }

      // Quick identity switcher pill in top-right when not in landing
      if (currentScreen !is Screen.Landing && currentUser != null && currentScreen !is Screen.StudentBook) {
        Box(
          modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(top = 10.dp, end = 16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Graphite900.copy(alpha = 0.9f))
            .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .clickable { showSwitchUserModal = true }
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .testTag("switch_user_pill")
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(GoldPrimary),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = currentUser!!.name.take(1).uppercase(),
                color = Graphite950,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = currentUser!!.name.split(" ").firstOrNull() ?: currentUser!!.name,
              color = GoldLight,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      // Global Add Dialog
      if (showGlobalAddDialog && currentUser != null) {
        AddEntryDialog(
          currentUserId = currentUser!!.id,
          currentUserName = currentUser!!.name,
          onDismiss = { showGlobalAddDialog = false },
          onSaveNote = { viewModel.saveNote(it) },
          onSaveProject = { viewModel.saveProject(it) },
          onSaveTask = { viewModel.saveTask(it) },
          onSaveDevelopment = { viewModel.saveDevelopment(it) }
        )
      }

      // Switch Identity Modal
      if (showSwitchUserModal) {
        SwitchUserDialog(
          currentUser = currentUser,
          allUsers = allUsers,
          onSelect = { selected ->
            viewModel.switchActiveUser(selected)
            showSwitchUserModal = false
          },
          onDismiss = { showSwitchUserModal = false }
        )
      }
    }
  }
}

@Composable
private fun SwitchUserDialog(
  currentUser: UserEntity?,
  allUsers: List<UserEntity>,
  onSelect: (UserEntity) -> Unit,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(18.dp))
        .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp)),
      color = Graphite900
    ) {
      androidx.compose.foundation.layout.Column(
        modifier = Modifier.padding(20.dp)
      ) {
        Text(
          text = "Switch Student Identity",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextHighEmphasis
          )
        )
        Text(
          text = "Select which innovator's perspective you wish to write and interact from:",
          style = MaterialTheme.typography.bodySmall.copy(color = TextLowEmphasis),
          modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
        )

        allUsers.forEach { user ->
          val isSelected = user.id == currentUser?.id
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(if (isSelected) GoldPrimary.copy(alpha = 0.15f) else Graphite850)
              .border(1.dp, if (isSelected) GoldPrimary else Graphite700, RoundedCornerShape(10.dp))
              .clickable { onSelect(user) }
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(GoldPrimary.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = user.name.take(1).uppercase(),
                color = GoldPrimary,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.width(12.dp))
            androidx.compose.foundation.layout.Column {
              Text(
                text = user.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = if (isSelected) GoldLight else TextHighEmphasis
                )
              )
              Text(
                text = user.field,
                style = MaterialTheme.typography.bodySmall.copy(color = TextLowEmphasis),
                fontSize = 11.sp
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun navItemColors() = NavigationBarItemDefaults.colors(
  selectedIconColor = Graphite950,
  selectedTextColor = GoldPrimary,
  indicatorColor = GoldPrimary,
  unselectedIconColor = TextMediumEmphasis,
  unselectedTextColor = TextLowEmphasis
)
