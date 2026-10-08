package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ActivityEntity
import com.example.data.local.entity.UserEntity
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
  currentUser: UserEntity,
  allUsers: List<UserEntity>,
  recentActivities: List<ActivityEntity>,
  ideasCount: Int,
  projectsCount: Int,
  thoughtsCount: Int,
  tasksCount: Int,
  developmentsCount: Int,
  onOpenMyShelf: () -> Unit,
  onOpenUserShelf: (UserEntity) -> Unit,
  onOpenBookshelf: () -> Unit,
  onOpenSearch: () -> Unit,
  onAddEntry: () -> Unit
) {
  val timeOfDay = when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
    in 5..11 -> "Good morning"
    in 12..16 -> "Good afternoon"
    else -> "Good evening"
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Graphite950)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 18.dp, vertical = 12.dp)
  ) {
    // Top Bar with Greeting & Search Icon
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "$timeOfDay, ${currentUser.name.split(" ").firstOrNull() ?: currentUser.name} 👋",
          style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            color = TextHighEmphasis
          )
        )
        Text(
          text = currentUser.field,
          style = MaterialTheme.typography.bodySmall.copy(color = VioletAccent)
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(
          onClick = onOpenSearch,
          modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Graphite850)
            .testTag("dashboard_search_button")
        ) {
          Icon(Icons.Default.Search, contentDescription = "Search", tint = GoldPrimary)
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // Featured Hero Banner: Quick Access to Personal Shelf
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(18.dp))
        .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
        .clickable { onOpenMyShelf() }
        .testTag("dashboard_open_myshelf_hero"),
      colors = CardDefaults.cardColors(containerColor = Graphite900)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            Brush.horizontalGradient(
              listOf(Graphite900, Graphite850, Graphite800)
            )
          )
          .padding(18.dp)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(GoldPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.MenuBook,
                  contentDescription = null,
                  tint = GoldPrimary,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "YOUR INNOVARA SHELF",
                style = MaterialTheme.typography.labelMedium.copy(
                  letterSpacing = 1.2.sp,
                  fontWeight = FontWeight.Bold,
                  color = GoldLight
                )
              )
            }

            Button(
              onClick = onAddEntry,
              colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = Graphite950
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .height(32.dp)
                .testTag("dashboard_quick_add")
            ) {
              Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Add Entry", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "“${currentUser.quote}”",
            style = MaterialTheme.typography.bodyMedium.copy(
              color = TextHighEmphasis,
              fontWeight = FontWeight.Medium
            )
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "Open full notebook & workspace",
              style = MaterialTheme.typography.labelMedium.copy(color = VioletAccent)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
              Icons.AutoMirrored.Filled.ArrowForward,
              contentDescription = null,
              tint = VioletAccent,
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // YOUR INNOVARA SPACE: Stat tiles
    Text(
      text = "YOUR INNOVARA SPACE",
      style = MaterialTheme.typography.labelLarge.copy(
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = TextMediumEmphasis
      )
    )

    Spacer(modifier = Modifier.height(10.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      StatCard(
        title = "Ideas",
        count = ideasCount,
        icon = "💡",
        color = GoldPrimary,
        modifier = Modifier.weight(1f)
      )
      StatCard(
        title = "Projects",
        count = projectsCount,
        icon = "🚀",
        color = Color(0xFF38BDF8),
        modifier = Modifier.weight(1f)
      )
      StatCard(
        title = "Tasks",
        count = tasksCount,
        icon = "✅",
        color = Color(0xFF34D399),
        modifier = Modifier.weight(1f)
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      StatCard(
        title = "Thoughts",
        count = thoughtsCount,
        icon = "🧠",
        color = VioletAccent,
        modifier = Modifier.weight(1f)
      )
      StatCard(
        title = "Developments",
        count = developmentsCount,
        icon = "🛠",
        color = Color(0xFFF472B6),
        modifier = Modifier.weight(1f)
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Explore Innovators Horizontal Shelf
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "EXPLORE INNOVATORS",
        style = MaterialTheme.typography.labelLarge.copy(
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp,
          color = TextMediumEmphasis
        )
      )
      Text(
        text = "View All",
        style = MaterialTheme.typography.labelMedium.copy(color = GoldPrimary),
        modifier = Modifier
          .clickable { onOpenBookshelf() }
          .padding(4.dp)
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      allUsers.forEach { user ->
        val userColor = try {
          Color(android.graphics.Color.parseColor(user.avatarColorHex))
        } catch (e: Exception) {
          GoldPrimary
        }

        Box(
          modifier = Modifier
            .width(150.dp)
            .height(140.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Graphite900)
            .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
            .clickable { onOpenUserShelf(user) }
            .padding(12.dp)
        ) {
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(userColor.copy(alpha = 0.2f))
                .border(1.dp, userColor, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = user.name.take(1).uppercase(),
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = userColor
                )
              )
            }

            Column {
              Text(
                text = user.name,
                style = MaterialTheme.typography.titleSmall.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextHighEmphasis
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = user.field,
                style = MaterialTheme.typography.bodySmall.copy(
                  color = TextLowEmphasis,
                  fontSize = 11.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }

            Text(
              text = "Open Book →",
              style = MaterialTheme.typography.labelSmall.copy(
                color = GoldPrimary,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // RECENT ACTIVITY FEED
    Text(
      text = "RECENT INNOVARA ACTIVITY",
      style = MaterialTheme.typography.labelLarge.copy(
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = TextMediumEmphasis
      )
    )

    Spacer(modifier = Modifier.height(10.dp))

    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      recentActivities.take(5).forEach { act ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Graphite900)
            .border(1.dp, Graphite800, RoundedCornerShape(10.dp))
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = when (act.targetType) {
              "IDEA" -> "💡"
              "PROJECT" -> "🚀"
              "TASK" -> "✅"
              "DEV_LOG" -> "🛠"
              "THOUGHT" -> "🧠"
              else -> "📝"
            },
            fontSize = 20.sp
          )

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "${act.userName} ${act.action}",
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = TextHighEmphasis
              )
            )
            Text(
              text = "“${act.targetTitle}”",
              style = MaterialTheme.typography.bodySmall.copy(
                color = GoldLight,
                fontSize = 12.sp
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(30.dp))
  }
}

@Composable
private fun StatCard(
  title: String,
  count: Int,
  icon: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(Graphite900)
      .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
      .padding(12.dp)
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = icon, fontSize = 16.sp)
        Text(
          text = "$count",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Black,
            color = color
          )
        )
      }
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
          color = TextMediumEmphasis
        )
      )
    }
  }
}
