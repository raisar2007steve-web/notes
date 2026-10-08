package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.DevelopmentEntity
import com.example.data.local.entity.NoteEntity
import com.example.data.local.entity.ProjectEntity
import com.example.data.local.entity.TaskEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.VersionEntity
import com.example.ui.InnovaraViewModel
import com.example.ui.components.AddEntryDialog
import com.example.ui.components.MarkdownView
import com.example.ui.components.VersionHistoryDialog
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.Graphite700
import com.example.ui.theme.Graphite800
import com.example.ui.theme.Graphite850
import com.example.ui.theme.Graphite900
import com.example.ui.theme.Graphite950
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.PriorityMedium
import com.example.ui.theme.StatusBuilding
import com.example.ui.theme.StatusDeployed
import com.example.ui.theme.StatusIdea
import com.example.ui.theme.StatusResearch
import com.example.ui.theme.TextHighEmphasis
import com.example.ui.theme.TextLowEmphasis
import com.example.ui.theme.TextMediumEmphasis
import com.example.ui.theme.VioletAccent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StudentBookScreen(
  user: UserEntity,
  currentUser: UserEntity?,
  viewModel: InnovaraViewModel,
  onBackToBookshelf: () -> Unit
) {
  val notes by viewModel.viewingUserNotes.collectAsState()
  val projects by viewModel.viewingUserProjects.collectAsState()
  val tasks by viewModel.viewingUserTasks.collectAsState()
  val developments by viewModel.viewingUserDevelopments.collectAsState()
  val activities by viewModel.viewingUserActivities.collectAsState()
  val autosaveStatus by viewModel.autosaveStatus.collectAsState()

  val isOwner = currentUser?.id == user.id

  var selectedTab by remember { mutableIntStateOf(0) }
  val tabTitles = listOf("💡 Ideas", "🧠 Thoughts", "🛠 Devs", "✅ Tasks", "🚀 Projects", "📝 Notebook", "📊 Stats")

  var showAddDialog by remember { mutableStateOf(false) }
  var historyTarget by remember { mutableStateOf<Pair<String, String>?>(null) } // targetTitle to targetId
  val targetVersions by if (historyTarget != null) {
    viewModel.getVersionsForTarget(historyTarget!!.second).collectAsState(initial = emptyList())
  } else {
    remember { mutableStateOf(emptyList<VersionEntity>()) }
  }

  // Active selected project for detailed preview modal
  var selectedProjectDetail by remember { mutableStateOf<ProjectEntity?>(null) }

  val avatarColor = try {
    Color(android.graphics.Color.parseColor(user.avatarColorHex))
  } catch (e: Exception) {
    GoldPrimary
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Graphite950)
  ) {
    Column(modifier = Modifier.fillMaxSize()) {
      // Top Book Header
      BookHeader(
        user = user,
        isOwner = isOwner,
        currentUserName = currentUser?.name ?: "Guest",
        avatarColor = avatarColor,
        onBack = onBackToBookshelf
      )

      // Section Tabs
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Graphite900,
        contentColor = GoldPrimary,
        indicator = { tabPositions ->
          TabRowDefaults.SecondaryIndicator(
            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
            color = GoldPrimary,
            height = 3.dp
          )
        },
        modifier = Modifier.fillMaxWidth()
      ) {
        tabTitles.forEachIndexed { index, title ->
          Tab(
            selected = selectedTab == index,
            onClick = { selectedTab = index },
            text = {
              Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                  color = if (selectedTab == index) GoldLight else TextMediumEmphasis
                ),
                maxLines = 1
              )
            }
          )
        }
      }

      // Tab Content
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
      ) {
        when (selectedTab) {
          0 -> IdeasSection(
            ideas = notes.filter { it.type == "IDEA" },
            onViewHistory = { title, id -> historyTarget = Pair(title, id) },
            onDelete = { viewModel.deleteNote(it) }
          )
          1 -> ThoughtsSection(
            thoughts = notes.filter { it.type in listOf("THOUGHT", "LEARNING", "RESEARCH") },
            onViewHistory = { title, id -> historyTarget = Pair(title, id) },
            onDelete = { viewModel.deleteNote(it) }
          )
          2 -> DevelopmentsSection(
            developments = developments,
            onViewHistory = { title, id -> historyTarget = Pair(title, id) },
            onDelete = { viewModel.deleteDevelopment(it) }
          )
          3 -> TasksSection(
            tasks = tasks,
            authorName = currentUser?.name ?: user.name,
            onToggle = { viewModel.toggleTask(it) },
            onDelete = { viewModel.deleteTask(it) }
          )
          4 -> ProjectsSection(
            projects = projects,
            onSelectProject = { selectedProjectDetail = it },
            onViewHistory = { title, id -> historyTarget = Pair(title, id) }
          )
          5 -> MarkdownNotebookSection(
            notes = notes.filter { it.type == "GENERAL" },
            autosaveStatus = autosaveStatus,
            currentUserId = user.id,
            onSaveNote = { viewModel.saveNote(it) },
            onDeleteNote = { viewModel.deleteNote(it) }
          )
          6 -> StatsSection(
            user = user,
            notes = notes,
            projects = projects,
            tasks = tasks,
            developments = developments
          )
        }
      }
    }

    // Floating Action Button to Add Entry
    FloatingActionButton(
      onClick = { showAddDialog = true },
      containerColor = GoldPrimary,
      contentColor = Graphite950,
      shape = CircleShape,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(20.dp)
        .testTag("student_book_add_entry_fab")
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Add, contentDescription = "Add Entry")
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = "Add Entry", fontWeight = FontWeight.Bold)
      }
    }

    // Add Entry Dialog
    if (showAddDialog) {
      AddEntryDialog(
        currentUserId = user.id,
        currentUserName = user.name,
        onDismiss = { showAddDialog = false },
        onSaveNote = { viewModel.saveNote(it) },
        onSaveProject = { viewModel.saveProject(it) },
        onSaveTask = { viewModel.saveTask(it) },
        onSaveDevelopment = { viewModel.saveDevelopment(it) }
      )
    }

    // Version History Dialog
    if (historyTarget != null) {
      VersionHistoryDialog(
        targetTitle = historyTarget!!.first,
        versions = targetVersions,
        onDismiss = { historyTarget = null }
      )
    }

    // Project Detail Modal
    if (selectedProjectDetail != null) {
      ProjectDetailModal(
        project = selectedProjectDetail!!,
        onDismiss = { selectedProjectDetail = null }
      )
    }
  }
}

@Composable
private fun BookHeader(
  user: UserEntity,
  isOwner: Boolean,
  currentUserName: String,
  avatarColor: Color,
  onBack: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(
        Brush.verticalGradient(
          listOf(Graphite850, Graphite900)
        )
      )
      .border(1.dp, BorderSubtle)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Column {
      // Nav row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clickable { onBack() }
            .testTag("back_to_bookshelf")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back to Bookshelf",
            tint = GoldPrimary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Bookshelf",
            style = MaterialTheme.typography.labelLarge.copy(
              color = GoldPrimary,
              fontWeight = FontWeight.SemiBold
            )
          )
        }

        // Ownership / Open Community Editing Badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isOwner) GoldPrimary.copy(alpha = 0.15f) else Graphite800)
            .border(1.dp, if (isOwner) GoldPrimary else Graphite700, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = if (isOwner) "Editing: My Space" else "Community Mode: $currentUserName",
            style = MaterialTheme.typography.labelSmall.copy(
              color = if (isOwner) GoldLight else TextMediumEmphasis,
              fontWeight = FontWeight.Medium
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Student info row
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(avatarColor.copy(alpha = 0.2f))
            .border(1.5.dp, avatarColor, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = user.name.take(1).uppercase(),
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = avatarColor
            )
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = user.name,
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Black,
              color = TextHighEmphasis
            )
          )
          Text(
            text = user.field,
            style = MaterialTheme.typography.bodySmall.copy(color = VioletAccent)
          )
          if (user.quote.isNotEmpty()) {
            Text(
              text = "“${user.quote}”",
              style = MaterialTheme.typography.bodySmall.copy(
                color = TextMediumEmphasis,
                fontSize = 12.sp
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }
  }
}

// 1. IDEAS SECTION
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IdeasSection(
  ideas: List<NoteEntity>,
  onViewHistory: (String, String) -> Unit,
  onDelete: (String) -> Unit
) {
  if (ideas.isEmpty()) {
    EmptyState(
      icon = "💡",
      message = "No innovation ideas documented yet.",
      callToAction = "Capture your first spark of an idea using + Add Entry!"
    )
  } else {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(ideas, key = { it.id }) { idea ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp)),
          colors = CardDefaults.cardColors(containerColor = Graphite900)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Header row with status & visibility
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(GoldPrimary.copy(alpha = 0.15f))
                  .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                  .padding(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Text(
                  text = idea.status,
                  color = GoldLight,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
              }

              Row(verticalAlignment = Alignment.CenterVertically) {
                if (idea.visibility == "PRIVATE") {
                  Icon(
                    Icons.Default.Lock,
                    contentDescription = "Private",
                    tint = TextLowEmphasis,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Private", style = MaterialTheme.typography.labelSmall.copy(color = TextLowEmphasis))
                } else {
                  Icon(
                    Icons.Default.Public,
                    contentDescription = "Public",
                    tint = VioletAccent,
                    modifier = Modifier.size(14.dp)
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Public", style = MaterialTheme.typography.labelSmall.copy(color = VioletAccent))
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                  onClick = { onViewHistory(idea.title, idea.id) },
                  modifier = Modifier.size(26.dp)
                ) {
                  Icon(Icons.Default.History, contentDescription = "History", tint = TextMediumEmphasis, modifier = Modifier.size(16.dp))
                }

                IconButton(
                  onClick = { onDelete(idea.id) },
                  modifier = Modifier.size(26.dp)
                ) {
                  Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextLowEmphasis, modifier = Modifier.size(16.dp))
                }
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = idea.title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextHighEmphasis
              )
            )

            Spacer(modifier = Modifier.height(6.dp))

            MarkdownView(markdown = idea.content)

            if (idea.tags.isNotBlank()) {
              Spacer(modifier = Modifier.height(10.dp))
              FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                idea.tags.split(",").forEach { tag ->
                  Box(
                    modifier = Modifier
                      .clip(RoundedCornerShape(6.dp))
                      .background(Graphite800)
                      .padding(horizontal = 6.dp, vertical = 2.dp)
                  ) {
                    Text(
                      text = "#${tag.trim()}",
                      style = MaterialTheme.typography.labelSmall.copy(color = TextMediumEmphasis),
                      fontSize = 11.sp
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

// 2. THOUGHTS SECTION
@Composable
private fun ThoughtsSection(
  thoughts: List<NoteEntity>,
  onViewHistory: (String, String) -> Unit,
  onDelete: (String) -> Unit
) {
  if (thoughts.isEmpty()) {
    EmptyState(
      icon = "🧠",
      message = "No freeform thoughts or reflections yet.",
      callToAction = "Document questions, theories, and philosophical musings!"
    )
  } else {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(thoughts, key = { it.id }) { item ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp)),
          colors = CardDefaults.cardColors(containerColor = Graphite900)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${item.type} · ${item.category}",
                style = MaterialTheme.typography.labelMedium.copy(
                  color = VioletAccent,
                  fontWeight = FontWeight.Bold
                )
              )

              Row {
                IconButton(
                  onClick = { onViewHistory(item.title, item.id) },
                  modifier = Modifier.size(26.dp)
                ) {
                  Icon(Icons.Default.History, contentDescription = "History", tint = TextMediumEmphasis, modifier = Modifier.size(16.dp))
                }
                IconButton(
                  onClick = { onDelete(item.id) },
                  modifier = Modifier.size(26.dp)
                ) {
                  Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextLowEmphasis, modifier = Modifier.size(16.dp))
                }
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
              text = item.title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextHighEmphasis
              )
            )

            Spacer(modifier = Modifier.height(8.dp))

            MarkdownView(markdown = item.content)
          }
        }
      }
    }
  }
}

// 3. DEVELOPMENTS TIMELINE SECTION
@Composable
private fun DevelopmentsSection(
  developments: List<DevelopmentEntity>,
  onViewHistory: (String, String) -> Unit,
  onDelete: (String) -> Unit
) {
  if (developments.isEmpty()) {
    EmptyState(
      icon = "🛠",
      message = "No development milestones logged.",
      callToAction = "Log what changed, what worked, and what's next on your journey!"
    )
  } else {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(developments, key = { it.id }) { dev ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp)),
          colors = CardDefaults.cardColors(containerColor = Graphite900)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            // Milestone top line
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = dev.dateStr,
                style = MaterialTheme.typography.labelMedium.copy(
                  color = GoldLight,
                  fontWeight = FontWeight.Bold
                )
              )

              Row {
                IconButton(
                  onClick = { onViewHistory(dev.title, dev.id) },
                  modifier = Modifier.size(26.dp)
                ) {
                  Icon(Icons.Default.History, contentDescription = "History", tint = TextMediumEmphasis, modifier = Modifier.size(16.dp))
                }
                IconButton(
                  onClick = { onDelete(dev.id) },
                  modifier = Modifier.size(26.dp)
                ) {
                  Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextLowEmphasis, modifier = Modifier.size(16.dp))
                }
              }
            }

            Text(
              text = dev.title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextHighEmphasis
              ),
              modifier = Modifier.padding(vertical = 4.dp)
            )

            // What changed
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Graphite850)
                .padding(10.dp)
            ) {
              MarkdownView(markdown = dev.whatChanged)
            }

            if (dev.whatWorked.isNotBlank()) {
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "✓ What worked: ${dev.whatWorked}",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF34D399))
              )
            }

            if (dev.whatFailed.isNotBlank()) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "⚠ What failed: ${dev.whatFailed}",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFF59E0B))
              )
            }

            if (dev.nextStep.isNotBlank()) {
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "→ Next: ${dev.nextStep}",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = VioletAccent,
                  fontWeight = FontWeight.SemiBold
                )
              )
            }
          }
        }
      }
    }
  }
}

// 4. TASKS SECTION
@Composable
private fun TasksSection(
  tasks: List<TaskEntity>,
  authorName: String,
  onToggle: (TaskEntity) -> Unit,
  onDelete: (String) -> Unit
) {
  if (tasks.isEmpty()) {
    EmptyState(
      icon = "✅",
      message = "No tasks in the backlog.",
      callToAction = "Add actionable checklist items to keep track of development progress!"
    )
  } else {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      items(tasks, key = { it.id }) { task ->
        val isCompleted = task.status == "COMPLETED"
        val priorityColor = when (task.priority) {
          "HIGH" -> PriorityHigh
          "LOW" -> PriorityLow
          else -> PriorityMedium
        }

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Graphite900)
            .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
            .clickable { onToggle(task) }
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Checkbox(
            checked = isCompleted,
            onCheckedChange = { onToggle(task) },
            colors = CheckboxDefaults.colors(
              checkedColor = GoldPrimary,
              uncheckedColor = TextLowEmphasis,
              checkmarkColor = Graphite950
            ),
            modifier = Modifier.testTag("task_checkbox_${task.id}")
          )

          Spacer(modifier = Modifier.width(8.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = task.title,
              style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                color = if (isCompleted) TextLowEmphasis else TextHighEmphasis
              )
            )

            Row(
              modifier = Modifier.padding(top = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(priorityColor)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${task.priority} PRIORITY",
                style = MaterialTheme.typography.labelSmall.copy(color = priorityColor, fontSize = 10.sp)
              )
              if (task.dueDate.isNotBlank()) {
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                  text = "Due: ${task.dueDate}",
                  style = MaterialTheme.typography.labelSmall.copy(color = TextLowEmphasis, fontSize = 11.sp)
                )
              }
            }
          }

          IconButton(
            onClick = { onDelete(task.id) },
            modifier = Modifier.size(28.dp)
          ) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextLowEmphasis, modifier = Modifier.size(16.dp))
          }
        }
      }
    }
  }
}

// 5. PROJECTS SECTION
@Composable
private fun ProjectsSection(
  projects: List<ProjectEntity>,
  onSelectProject: (ProjectEntity) -> Unit,
  onViewHistory: (String, String) -> Unit
) {
  if (projects.isEmpty()) {
    EmptyState(
      icon = "🚀",
      message = "No projects documented in this library.",
      callToAction = "Create projects with architecture blueprints, tech stacks, and repos!"
    )
  } else {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(projects, key = { it.id }) { proj ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .clickable { onSelectProject(proj) },
          colors = CardDefaults.cardColors(containerColor = Graphite900)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🚀", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = proj.name,
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextHighEmphasis
                  )
                )
              }

              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(8.dp))
                  .background(StatusBuilding.copy(alpha = 0.2f))
                  .border(1.dp, StatusBuilding, RoundedCornerShape(8.dp))
                  .padding(horizontal = 8.dp, vertical = 3.dp)
              ) {
                Text(
                  text = proj.status,
                  color = StatusBuilding,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                )
              }
            }

            if (proj.tagline.isNotBlank()) {
              Text(
                text = proj.tagline,
                style = MaterialTheme.typography.bodyMedium.copy(
                  color = VioletAccent,
                  fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(top = 4.dp)
              )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
              text = proj.description,
              style = MaterialTheme.typography.bodyMedium.copy(color = TextMediumEmphasis),
              maxLines = 3,
              overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Progress bar
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "Progress", style = MaterialTheme.typography.labelSmall.copy(color = TextLowEmphasis))
              Text(
                text = "${proj.progressPercent}%",
                style = MaterialTheme.typography.labelSmall.copy(
                  color = GoldPrimary,
                  fontWeight = FontWeight.Bold
                )
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
              progress = { proj.progressPercent / 100f },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
              color = GoldPrimary,
              trackColor = Graphite800
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Technologies
            if (proj.technologies.isNotBlank()) {
              Text(
                text = "Technologies: ${proj.technologies}",
                style = MaterialTheme.typography.bodySmall.copy(color = TextLowEmphasis),
                fontSize = 12.sp
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.End
            ) {
              Button(
                onClick = { onSelectProject(proj) },
                colors = ButtonDefaults.buttonColors(
                  containerColor = Graphite800,
                  contentColor = GoldPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(34.dp)
              ) {
                Text("Open Project Overview", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
              }
            }
          }
        }
      }
    }
  }
}

// 6. MARKDOWN NOTEBOOK SECTION (General purpose markdown with live preview and autosave)
@Composable
private fun MarkdownNotebookSection(
  notes: List<NoteEntity>,
  autosaveStatus: String,
  currentUserId: String,
  onSaveNote: (NoteEntity) -> Unit,
  onDeleteNote: (String) -> Unit
) {
  var activeNote by remember(notes) {
    mutableStateOf(notes.firstOrNull() ?: NoteEntity(
      id = "note_scratchpad",
      userId = currentUserId,
      type = "GENERAL",
      title = "Research & Engineering Scratchpad",
      content = """# Innovara Research Scratchpad
Document concepts in full markdown.

## Core Tenets
- **Iterate fast**, keep architecture modular.
- Use `Flow<T>` for reactive state pipelines.
- Verify zero-knowledge proofs before gossiping.

```python
def synthesize(query: str):
    return f"Synthesized reasoning for: {query}"
```

> "Every idea deserves a place."
""".trimIndent(),
      category = "General",
      status = "💭 Idea",
      tags = "Scratchpad, Markdown",
      visibility = "PUBLIC"
    ))
  }

  var editorMode by remember { mutableIntStateOf(0) } // 0: Edit, 1: Preview, 2: Split

  var titleText by remember(activeNote.id) { mutableStateOf(activeNote.title) }
  var contentText by remember(activeNote.id) { mutableStateOf(activeNote.content) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp)
  ) {
    // Top Bar: Edit / Preview toggle + Autosave status
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        listOf("Edit", "Preview").forEachIndexed { index, mode ->
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(if (editorMode == index) GoldPrimary else Graphite850)
              .clickable { editorMode = index }
              .padding(horizontal = 14.dp, vertical = 6.dp)
          ) {
            Text(
              text = mode,
              color = if (editorMode == index) Graphite950 else TextMediumEmphasis,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = autosaveStatus,
          style = MaterialTheme.typography.labelSmall.copy(
            color = if (autosaveStatus.contains("Saved")) Color(0xFF34D399) else GoldPrimary,
            fontWeight = FontWeight.SemiBold
          )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Button(
          onClick = {
            val updated = activeNote.copy(
              title = titleText.trim(),
              content = contentText,
              updatedAt = System.currentTimeMillis()
            )
            onSaveNote(updated)
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = GoldPrimary,
            contentColor = Graphite950
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .height(32.dp)
            .testTag("save_notebook_button")
        ) {
          Text("Save Note", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Note Title Input
    OutlinedTextField(
      value = titleText,
      onValueChange = { titleText = it },
      label = { Text("Note Title") },
      singleLine = true,
      modifier = Modifier.fillMaxWidth(),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = GoldPrimary,
        unfocusedBorderColor = Graphite700,
        focusedTextColor = TextHighEmphasis,
        unfocusedTextColor = TextHighEmphasis,
        focusedContainerColor = Graphite900,
        unfocusedContainerColor = Graphite900
      )
    )

    Spacer(modifier = Modifier.height(10.dp))

    if (editorMode == 0) {
      // Edit Mode
      OutlinedTextField(
        value = contentText,
        onValueChange = {
          contentText = it
          // autosave trigger
        },
        label = { Text("Markdown Editor (# Heading, **bold**, `code`, > quote, ```codeblock)") },
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .testTag("markdown_editor_input"),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = GoldPrimary,
          unfocusedBorderColor = Graphite700,
          focusedTextColor = TextHighEmphasis,
          unfocusedTextColor = TextHighEmphasis,
          focusedContainerColor = Graphite900,
          unfocusedContainerColor = Graphite900
        )
      )
    } else {
      // Preview Mode
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
          .clip(RoundedCornerShape(12.dp))
          .background(Graphite900)
          .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
          .verticalScroll(rememberScrollState())
          .padding(16.dp)
      ) {
        MarkdownView(markdown = contentText)
      }
    }
  }
}

// 7. STATS & OVERVIEW SECTION
@Composable
private fun StatsSection(
  user: UserEntity,
  notes: List<NoteEntity>,
  projects: List<ProjectEntity>,
  tasks: List<TaskEntity>,
  developments: List<DevelopmentEntity>
) {
  val ideasCount = notes.count { it.type == "IDEA" }
  val activeProjects = projects.count { it.status != "Completed" }
  val completedProjects = projects.count { it.status == "Completed" }
  val completedTasks = tasks.count { it.status == "COMPLETED" }
  val totalTasks = tasks.size
  val devEntries = developments.size
  val researchEntries = notes.count { it.type == "RESEARCH" }
  val learningEntries = notes.count { it.type == "LEARNING" }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
  ) {
    Text(
      text = "Student Analytics & Innovation Index",
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        color = TextHighEmphasis
      )
    )

    Spacer(modifier = Modifier.height(14.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      StatOverviewCard("💡 Total Ideas", "$ideasCount", GoldPrimary, Modifier.weight(1f))
      StatOverviewCard("🚀 Active Projects", "$activeProjects", Color(0xFF38BDF8), Modifier.weight(1f))
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      StatOverviewCard("✅ Completed Tasks", "$completedTasks / $totalTasks", Color(0xFF34D399), Modifier.weight(1f))
      StatOverviewCard("🛠 Dev Milestones", "$devEntries", VioletAccent, Modifier.weight(1f))
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      StatOverviewCard("🔬 Research Logs", "$researchEntries", Color(0xFFFB7185), Modifier.weight(1f))
      StatOverviewCard("📚 Learning Notes", "$learningEntries", Color(0xFFFBBF24), Modifier.weight(1f))
    }

    Spacer(modifier = Modifier.height(20.dp))

    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp)),
      colors = CardDefaults.cardColors(containerColor = Graphite900)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Interests & Domain Knowledge",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextHighEmphasis)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = user.interests,
          style = MaterialTheme.typography.bodyMedium.copy(color = TextMediumEmphasis)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "Personal Bio",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextHighEmphasis)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = user.bio,
          style = MaterialTheme.typography.bodyMedium.copy(color = TextMediumEmphasis)
        )
      }
    }
  }
}

@Composable
private fun StatOverviewCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(Graphite900)
      .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
      .padding(14.dp)
  ) {
    Column {
      Text(text = label, style = MaterialTheme.typography.bodySmall.copy(color = TextLowEmphasis))
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Black,
          color = color
        )
      )
    }
  }
}

@Composable
private fun EmptyState(icon: String, message: String, callToAction: String) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(32.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(text = icon, fontSize = 48.sp)
      Spacer(modifier = Modifier.height(12.dp))
      Text(
        text = message,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          color = TextHighEmphasis
        )
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = callToAction,
        style = MaterialTheme.typography.bodySmall.copy(color = TextLowEmphasis)
      )
    }
  }
}

// Full modal dialog for Project Overview
@Composable
private fun ProjectDetailModal(
  project: ProjectEntity,
  onDismiss: () -> Unit
) {
  androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
    androidx.compose.material3.Surface(
      modifier = Modifier
        .fillMaxWidth()
        .height(600.dp)
        .clip(RoundedCornerShape(20.dp))
        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp)),
      color = Graphite900
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🚀", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = project.name,
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextHighEmphasis
                )
              )
              Text(
                text = project.tagline,
                style = MaterialTheme.typography.bodySmall.copy(color = VioletAccent)
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.OpenInNew, contentDescription = "Close", tint = TextMediumEmphasis)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text("Problem Statement:", style = MaterialTheme.typography.labelMedium.copy(color = GoldPrimary, fontWeight = FontWeight.Bold))
        Text(project.problem.ifBlank { project.description }, style = MaterialTheme.typography.bodyMedium.copy(color = TextMediumEmphasis))

        Spacer(modifier = Modifier.height(10.dp))

        Text("Proposed Solution:", style = MaterialTheme.typography.labelMedium.copy(color = GoldPrimary, fontWeight = FontWeight.Bold))
        Text(project.solution.ifBlank { "Architecture and system execution in progress." }, style = MaterialTheme.typography.bodyMedium.copy(color = TextMediumEmphasis))

        Spacer(modifier = Modifier.height(12.dp))

        if (project.markdownDoc.isNotBlank()) {
          Text("Project Documentation & Blueprint:", style = MaterialTheme.typography.labelMedium.copy(color = VioletAccent, fontWeight = FontWeight.Bold))
          Spacer(modifier = Modifier.height(6.dp))
          MarkdownView(markdown = project.markdownDoc)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(
            containerColor = GoldPrimary,
            contentColor = Graphite950
          ),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Close", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
