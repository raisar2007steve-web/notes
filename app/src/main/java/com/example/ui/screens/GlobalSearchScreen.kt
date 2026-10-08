package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserEntity
import com.example.ui.InnovaraViewModel
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.Graphite700
import com.example.ui.theme.Graphite850
import com.example.ui.theme.Graphite900
import com.example.ui.theme.Graphite950
import com.example.ui.theme.TextHighEmphasis
import com.example.ui.theme.TextLowEmphasis
import com.example.ui.theme.TextMediumEmphasis
import com.example.ui.theme.VioletAccent

@Composable
fun GlobalSearchScreen(
  viewModel: InnovaraViewModel,
  onOpenUserShelf: (UserEntity) -> Unit
) {
  val query by viewModel.globalSearchQuery.collectAsState()
  val matchedUsers by viewModel.searchResultsUsers.collectAsState()
  val matchedNotes by viewModel.searchResultsNotes.collectAsState()
  val matchedProjects by viewModel.searchResultsProjects.collectAsState()
  val allUsers by viewModel.allUsers.collectAsState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Graphite950)
      .padding(horizontal = 16.dp, vertical = 10.dp)
  ) {
    Text(
      text = "🔎 Global Knowledge Search",
      style = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.Black,
        color = TextHighEmphasis
      )
    )

    Text(
      text = "Search across all student notebooks, ideas, projects, and architecture notes.",
      style = MaterialTheme.typography.bodyMedium.copy(
        color = TextMediumEmphasis,
        lineHeight = 20.sp
      ),
      modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )

    OutlinedTextField(
      value = query,
      onValueChange = { viewModel.globalSearchQuery.value = it },
      placeholder = { Text("Search e.g. \"AI memory\", \"ESP32\", \"Consensus\"...", color = TextLowEmphasis) },
      leadingIcon = {
        Icon(Icons.Default.Search, contentDescription = null, tint = GoldPrimary)
      },
      trailingIcon = {
        if (query.isNotEmpty()) {
          IconButton(onClick = { viewModel.globalSearchQuery.value = "" }) {
            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMediumEmphasis)
          }
        }
      },
      singleLine = true,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("global_search_input"),
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

    Spacer(modifier = Modifier.height(14.dp))

    if (query.isBlank()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text("Type a query to search across all student knowledge spaces", color = TextLowEmphasis, fontSize = 13.sp)
        }
      }
    } else {
      val hasResults = matchedUsers.isNotEmpty() || matchedProjects.isNotEmpty() || matchedNotes.isNotEmpty()

      if (!hasResults) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Text("No results found for \"$query\"", color = TextMediumEmphasis)
        }
      } else {
        LazyColumn(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Users
          if (matchedUsers.isNotEmpty()) {
            item {
              SectionHeader("Students & Innovators (${matchedUsers.size})")
            }
            items(matchedUsers) { user ->
              SearchResultCard(
                icon = "👤",
                title = user.name,
                subtitle = user.field,
                tag = "Student",
                onClick = { onOpenUserShelf(user) }
              )
            }
          }

          // Projects
          if (matchedProjects.isNotEmpty()) {
            item {
              SectionHeader("Projects (${matchedProjects.size})")
            }
            items(matchedProjects) { proj ->
              val owner = allUsers.find { it.id == proj.userId }
              SearchResultCard(
                icon = "🚀",
                title = proj.name,
                subtitle = "${owner?.name ?: "Student"} · ${proj.tagline}",
                tag = "Project",
                onClick = {
                  if (owner != null) onOpenUserShelf(owner)
                }
              )
            }
          }

          // Notes
          if (matchedNotes.isNotEmpty()) {
            item {
              SectionHeader("Notes, Ideas & Thoughts (${matchedNotes.size})")
            }
            items(matchedNotes) { note ->
              val owner = allUsers.find { it.id == note.userId }
              val icon = when (note.type) {
                "IDEA" -> "💡"
                "THOUGHT" -> "🧠"
                "RESEARCH" -> "🔬"
                "LEARNING" -> "📚"
                else -> "📝"
              }
              SearchResultCard(
                icon = icon,
                title = note.title,
                subtitle = "${owner?.name ?: "Student"} · ${note.content.take(70)}...",
                tag = note.type,
                onClick = {
                  if (owner != null) onOpenUserShelf(owner)
                }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun SectionHeader(title: String) {
  Text(
    text = title,
    style = MaterialTheme.typography.labelLarge.copy(
      color = GoldPrimary,
      fontWeight = FontWeight.Bold
    ),
    modifier = Modifier.padding(vertical = 4.dp)
  )
}

@Composable
private fun SearchResultCard(
  icon: String,
  title: String,
  subtitle: String,
  tag: String,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
      .clickable { onClick() },
    colors = CardDefaults.cardColors(containerColor = Graphite900)
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(Graphite850),
        contentAlignment = Alignment.Center
      ) {
        Text(text = icon, fontSize = 18.sp)
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Bold,
              color = TextHighEmphasis
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Graphite800)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(text = tag, color = VioletAccent, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall.copy(color = TextMediumEmphasis),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}
