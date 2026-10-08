package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.local.entity.DevelopmentEntity
import com.example.data.local.entity.NoteEntity
import com.example.data.local.entity.ProjectEntity
import com.example.data.local.entity.TaskEntity
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.Graphite700
import com.example.ui.theme.Graphite800
import com.example.ui.theme.Graphite850
import com.example.ui.theme.Graphite900
import com.example.ui.theme.PriorityHigh
import com.example.ui.theme.PriorityLow
import com.example.ui.theme.PriorityMedium
import com.example.ui.theme.TextHighEmphasis
import com.example.ui.theme.TextLowEmphasis
import com.example.ui.theme.TextMediumEmphasis
import com.example.ui.theme.VioletAccent
import java.util.UUID

enum class EntryType(val label: String, val icon: String, val description: String) {
  IDEA("Idea", "💡", "New concept, invention, startup idea, or problem statement"),
  THOUGHT("Thought", "🧠", "Observations, philosophical questions, reflections"),
  DEVELOPMENT("Development", "🛠", "Chronological development milestone or sprint update"),
  TASK("Task", "✅", "Action item for your personal backlog"),
  PROJECT("Project", "🚀", "Full system, hardware or software innovation project"),
  RESEARCH("Research", "🔬", "Formal investigation, literature review, benchmark"),
  LEARNING("Learning", "📚", "New framework, theory, or engineering insight"),
  NOTE("General Note", "📝", "Freeform markdown document")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEntryDialog(
  currentUserId: String,
  currentUserName: String,
  onDismiss: () -> Unit,
  onSaveNote: (NoteEntity) -> Unit,
  onSaveProject: (ProjectEntity) -> Unit,
  onSaveTask: (TaskEntity) -> Unit,
  onSaveDevelopment: (DevelopmentEntity) -> Unit
) {
  var selectedType by remember { mutableStateOf<EntryType?>(null) }

  // Common Note Fields
  var title by remember { mutableStateOf("") }
  var content by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Innovation") }
  var tags by remember { mutableStateOf("") }
  var isPrivate by remember { mutableStateOf(false) }
  var noteStatus by remember { mutableStateOf("💭 Idea") }

  // Task Fields
  var taskPriority by remember { mutableStateOf("MEDIUM") }
  var taskDueDate by remember { mutableStateOf("Soon") }

  // Development Fields
  var devDate by remember { mutableStateOf("October 8, 2026") }
  var devWhatChanged by remember { mutableStateOf("") }
  var devWhatWorked by remember { mutableStateOf("") }
  var devWhatFailed by remember { mutableStateOf("") }
  var devNextStep by remember { mutableStateOf("") }
  var devStatus by remember { mutableStateOf("In Progress") }

  // Project Fields
  var projTagline by remember { mutableStateOf("") }
  var projGoal by remember { mutableStateOf("") }
  var projProblem by remember { mutableStateOf("") }
  var projSolution by remember { mutableStateOf("") }
  var projTechnologies by remember { mutableStateOf("") }
  var projProgress by remember { mutableFloatStateOf(20f) }
  var projRepoUrl by remember { mutableStateOf("") }
  var projDemoUrl by remember { mutableStateOf("") }
  var projStatus by remember { mutableStateOf("Building") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = 640.dp)
        .clip(RoundedCornerShape(20.dp))
        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp)),
      color = Graphite900
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Top Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (selectedType == null) "What do you want to add?" else "Add ${selectedType?.label}",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = GoldPrimary
              )
            )
            Text(
              text = "Adding to $currentUserName's space",
              style = MaterialTheme.typography.bodySmall.copy(color = TextLowEmphasis)
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_add_dialog")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMediumEmphasis)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedType == null) {
          // Selector grid
          Text(
            text = "Select an entry type to begin:",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextMediumEmphasis)
          )
          Spacer(modifier = Modifier.height(12.dp))

          EntryType.values().forEach { type ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Graphite850)
                .border(1.dp, Graphite700, RoundedCornerShape(12.dp))
                .clickable { selectedType = type }
                .padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(text = type.icon, fontSize = 24.sp)
              Spacer(modifier = Modifier.width(14.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = type.label,
                  style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextHighEmphasis
                  )
                )
                Text(
                  text = type.description,
                  style = MaterialTheme.typography.bodySmall.copy(color = TextLowEmphasis),
                  fontSize = 12.sp
                )
              }
            }
          }
        } else {
          // Form based on type
          when (selectedType) {
            EntryType.TASK -> {
              OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title *") },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("task_title_input"),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Description / Details") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text("Priority:", style = MaterialTheme.typography.labelMedium.copy(color = TextMediumEmphasis))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
              ) {
                listOf("HIGH" to PriorityHigh, "MEDIUM" to PriorityMedium, "LOW" to PriorityLow).forEach { (p, col) ->
                  FilterChip(
                    selected = taskPriority == p,
                    onClick = { taskPriority = p },
                    label = { Text(p, color = if (taskPriority == p) Color.Black else col) },
                    colors = FilterChipDefaults.filterChipColors(
                      selectedContainerColor = col
                    )
                  )
                }
              }
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = taskDueDate,
                onValueChange = { taskDueDate = it },
                label = { Text("Due Date (e.g. Oct 12)") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
              )
            }

            EntryType.DEVELOPMENT -> {
              OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Development Milestone / Title *") },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("dev_title_input"),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = devDate,
                onValueChange = { devDate = it },
                label = { Text("Date (e.g. October 8, 2026)") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = devWhatChanged,
                onValueChange = { devWhatChanged = it },
                label = { Text("What Changed? (supports ✓, ⚠, → bullets) *") },
                placeholder = { Text("✓ Created memory module\n✓ Added research workflow\n⚠ Tests pending") },
                minLines = 3,
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = devWhatWorked,
                onValueChange = { devWhatWorked = it },
                label = { Text("What worked well?") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = devWhatFailed,
                onValueChange = { devWhatFailed = it },
                label = { Text("What failed / blocked?") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = devNextStep,
                onValueChange = { devNextStep = it },
                label = { Text("Next step?") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
              )
            }

            EntryType.PROJECT -> {
              OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Project Name *") },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("proj_name_input"),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = projTagline,
                onValueChange = { projTagline = it },
                label = { Text("Tagline / Short description") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Overview & Abstract") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = projTechnologies,
                onValueChange = { projTechnologies = it },
                label = { Text("Technologies (e.g. Python · Rust · Compose)") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              Text("Progress: ${projProgress.toInt()}%", style = MaterialTheme.typography.labelMedium.copy(color = TextMediumEmphasis))
              Slider(
                value = projProgress,
                onValueChange = { projProgress = it },
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                  thumbColor = GoldPrimary,
                  activeTrackColor = GoldPrimary
                )
              )
              Spacer(modifier = Modifier.height(6.dp))
              OutlinedTextField(
                value = projRepoUrl,
                onValueChange = { projRepoUrl = it },
                label = { Text("Repository URL") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
              )
            }

            else -> {
              // Note / Idea / Thought / Learning / Research
              OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title *") },
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("entry_title_input"),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content (Markdown supported: #, **, `, -) *") },
                minLines = 4,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("entry_content_input"),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))

              if (selectedType == EntryType.IDEA) {
                Text("Idea Status:", style = MaterialTheme.typography.labelMedium.copy(color = TextMediumEmphasis))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                  listOf("💭 Idea", "🔬 Researching", "🛠 Building", "🧪 Testing", "🚀 Deployed", "✅ Completed").forEach { s ->
                    FilterChip(
                      selected = noteStatus == s,
                      onClick = { noteStatus = s },
                      label = { Text(s) },
                      colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GoldPrimary,
                        selectedLabelColor = Graphite950
                      )
                    )
                  }
                }
                Spacer(modifier = Modifier.height(8.dp))
              }

              OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category / Domain") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
              )
              Spacer(modifier = Modifier.height(10.dp))
              OutlinedTextField(
                value = tags,
                onValueChange = { tags = it },
                label = { Text("Tags (comma separated, e.g. AI, Edge, IoT)") },
                modifier = Modifier.fillMaxWidth(),
                colors = outlinedColors()
              )

              Spacer(modifier = Modifier.height(12.dp))

              // Visibility toggle
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(8.dp))
                  .background(Graphite800)
                  .clickable { isPrivate = !isPrivate }
                  .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = if (isPrivate) Icons.Default.Lock else Icons.Default.Public,
                    contentDescription = null,
                    tint = if (isPrivate) GoldPrimary else VioletAccent
                  )
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(
                      text = if (isPrivate) "🔒 Private Entry" else "🌐 Public Entry",
                      style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextHighEmphasis
                      )
                    )
                    Text(
                      text = if (isPrivate) "Only visible to you" else "Openly readable by all students",
                      style = MaterialTheme.typography.bodySmall.copy(color = TextLowEmphasis),
                      fontSize = 11.sp
                    )
                  }
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(18.dp))

          // Action buttons
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            TextButton(
              onClick = { selectedType = null }
            ) {
              Text("Back", color = TextMediumEmphasis)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(
              onClick = {
                val currentType = selectedType ?: return@Button
                when (currentType) {
                  EntryType.TASK -> {
                    if (title.isNotBlank()) {
                      onSaveTask(
                        TaskEntity(
                          id = "task_" + UUID.randomUUID().toString().take(8),
                          userId = currentUserId,
                          title = title.trim(),
                          description = content.trim(),
                          priority = taskPriority,
                          status = "TODO",
                          dueDate = taskDueDate.trim()
                        )
                      )
                      onDismiss()
                    }
                  }
                  EntryType.DEVELOPMENT -> {
                    if (title.isNotBlank()) {
                      onSaveDevelopment(
                        DevelopmentEntity(
                          id = "dev_" + UUID.randomUUID().toString().take(8),
                          userId = currentUserId,
                          title = title.trim(),
                          dateStr = devDate.trim(),
                          whatChanged = devWhatChanged.trim(),
                          whatWorked = devWhatWorked.trim(),
                          whatFailed = devWhatFailed.trim(),
                          nextStep = devNextStep.trim(),
                          status = devStatus
                        )
                      )
                      onDismiss()
                    }
                  }
                  EntryType.PROJECT -> {
                    if (title.isNotBlank()) {
                      onSaveProject(
                        ProjectEntity(
                          id = "proj_" + UUID.randomUUID().toString().take(8),
                          userId = currentUserId,
                          name = title.trim(),
                          tagline = projTagline.trim(),
                          description = content.trim(),
                          goal = projGoal.trim(),
                          problem = projProblem.trim(),
                          solution = projSolution.trim(),
                          technologies = projTechnologies.trim(),
                          progressPercent = projProgress.toInt(),
                          status = projStatus,
                          repoUrl = projRepoUrl.trim(),
                          demoUrl = projDemoUrl.trim()
                        )
                      )
                      onDismiss()
                    }
                  }
                  else -> {
                    if (title.isNotBlank() || content.isNotBlank()) {
                      val finalTitle = title.ifBlank { "Untitled ${currentType.label}" }
                      onSaveNote(
                        NoteEntity(
                          id = "note_" + UUID.randomUUID().toString().take(8),
                          userId = currentUserId,
                          type = currentType.name,
                          title = finalTitle.trim(),
                          content = content.trim(),
                          category = category.trim(),
                          status = noteStatus,
                          tags = tags.trim(),
                          visibility = if (isPrivate) "PRIVATE" else "PUBLIC"
                        )
                      )
                      onDismiss()
                    }
                  }
                }
              },
              colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = Graphite950
              ),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("submit_entry_button")
            ) {
              Text("Save Entry", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun outlinedColors() = OutlinedTextFieldDefaults.colors(
  focusedBorderColor = GoldPrimary,
  unfocusedBorderColor = Graphite700,
  focusedTextColor = TextHighEmphasis,
  unfocusedTextColor = TextHighEmphasis,
  focusedLabelColor = GoldPrimary,
  unfocusedLabelColor = TextLowEmphasis,
  focusedContainerColor = Graphite850,
  unfocusedContainerColor = Graphite850
)
