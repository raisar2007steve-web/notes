package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserEntity
import com.example.ui.InnovaraViewModel
import com.example.ui.components.StudentBookCard
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.Graphite700
import com.example.ui.theme.Graphite850
import com.example.ui.theme.Graphite950
import com.example.ui.theme.TextHighEmphasis
import com.example.ui.theme.TextLowEmphasis
import com.example.ui.theme.TextMediumEmphasis

@Composable
fun BookshelfScreen(
  viewModel: InnovaraViewModel,
  onOpenStudentBook: (UserEntity) -> Unit
) {
  val allUsers by viewModel.allUsers.collectAsState()
  val allNotes by viewModel.allNotes.collectAsState()
  val allProjects by viewModel.allProjects.collectAsState()
  val allTasks by viewModel.allTasks.collectAsState()
  val searchQuery by viewModel.bookshelfQuery.collectAsState()
  val selectedFilter by viewModel.bookshelfFilter.collectAsState()

  val filters = listOf(
    "All",
    "Ideas",
    "Projects",
    "Development",
    "Thoughts",
    "To-Do",
    "Learning",
    "Research"
  )

  val filteredUsers = allUsers.filter { user ->
    val matchesSearch = searchQuery.isBlank() ||
      user.name.contains(searchQuery, ignoreCase = true) ||
      user.field.contains(searchQuery, ignoreCase = true) ||
      user.interests.contains(searchQuery, ignoreCase = true)
    matchesSearch
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Graphite950)
      .padding(horizontal = 16.dp, vertical = 10.dp)
  ) {
    // Header
    Text(
      text = "📚 Innovara Bookshelf",
      style = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.Black,
        color = TextHighEmphasis
      )
    )

    Text(
      text = "Explore what your fellow innovators are thinking, building and learning.",
      style = MaterialTheme.typography.bodyMedium.copy(
        color = TextMediumEmphasis,
        lineHeight = 20.sp
      ),
      modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )

    // Search bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { viewModel.bookshelfQuery.value = it },
      placeholder = { Text("Search students, fields, research...", color = TextLowEmphasis) },
      leadingIcon = {
        Icon(Icons.Default.Search, contentDescription = null, tint = GoldPrimary)
      },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { viewModel.bookshelfQuery.value = "" }) {
            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMediumEmphasis)
          }
        }
      },
      singleLine = true,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("bookshelf_search_input"),
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = GoldPrimary,
        unfocusedBorderColor = Graphite700,
        focusedTextColor = TextHighEmphasis,
        unfocusedTextColor = TextHighEmphasis,
        focusedContainerColor = Graphite850,
        unfocusedContainerColor = Graphite850
      )
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Filter Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      filters.forEach { filter ->
        FilterChip(
          selected = selectedFilter == filter,
          onClick = { viewModel.bookshelfFilter.value = filter },
          label = { Text(filter) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = GoldPrimary,
            selectedLabelColor = Graphite950,
            containerColor = Graphite850,
            labelColor = TextMediumEmphasis
          )
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // List of student books
    if (filteredUsers.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("No innovators found matching \"$searchQuery\"", color = TextMediumEmphasis)
        }
      }
    } else {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.weight(1f)
      ) {
        items(filteredUsers, key = { it.id }) { user ->
          val userNotes = allNotes.filter { it.userId == user.id }
          val ideasCount = userNotes.count { it.type == "IDEA" }
          val notesCount = userNotes.size
          val projectsCount = allProjects.count { it.userId == user.id }
          val tasksCount = allTasks.count { it.userId == user.id && it.status == "COMPLETED" }

          StudentBookCard(
            user = user,
            ideasCount = ideasCount,
            notesCount = notesCount,
            projectsCount = projectsCount,
            tasksCount = tasksCount,
            onOpenBook = { onOpenStudentBook(user) }
          )
        }
      }
    }
  }
}
