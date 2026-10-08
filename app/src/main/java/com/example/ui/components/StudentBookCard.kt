package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.Graphite800
import com.example.ui.theme.Graphite850
import com.example.ui.theme.Graphite900
import com.example.ui.theme.Graphite950
import com.example.ui.theme.TextHighEmphasis
import com.example.ui.theme.TextLowEmphasis
import com.example.ui.theme.TextMediumEmphasis
import com.example.ui.theme.VioletAccent

@Composable
fun StudentBookCard(
  user: UserEntity,
  ideasCount: Int,
  notesCount: Int,
  projectsCount: Int,
  tasksCount: Int,
  onOpenBook: () -> Unit,
  modifier: Modifier = Modifier
) {
  val avatarColor = try {
    Color(android.graphics.Color.parseColor(user.avatarColorHex))
  } catch (e: Exception) {
    GoldPrimary
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable { onOpenBook() }
      .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
      .testTag("student_card_${user.id}"),
    colors = CardDefaults.cardColors(
      containerColor = Graphite900
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)
    ) {
      // Decorative Book Spine
      Box(
        modifier = Modifier
          .width(10.dp)
          .fillMaxHeight()
          .background(
            Brush.verticalGradient(
              listOf(avatarColor, GoldPrimary)
            )
          )
      )

      Column(
        modifier = Modifier
          .weight(1f)
          .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        // Top section: Avatar + Name + Field
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(avatarColor.copy(alpha = 0.2f))
              .border(1.5.dp, avatarColor, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = user.name.take(1).uppercase(),
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = avatarColor
              )
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = user.name,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextHighEmphasis
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
            Text(
              text = user.field,
              style = MaterialTheme.typography.bodySmall.copy(
                color = VioletAccent,
                fontWeight = FontWeight.Medium
              ),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        // Quote / Tagline
        if (user.quote.isNotEmpty()) {
          Text(
            text = "“${user.quote}”",
            style = MaterialTheme.typography.bodySmall.copy(
              color = TextMediumEmphasis,
              fontSize = 12.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 4.dp)
          )
        }

        // Metrics Row (Ideas, Notes, Projects, Tasks)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Graphite850)
            .padding(horizontal = 8.dp, vertical = 6.dp),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalAlignment = Alignment.CenterVertically
        ) {
          MetricChip(icon = "💡", count = ideasCount, label = "Ideas")
          MetricChip(icon = "📘", count = notesCount, label = "Notes")
          MetricChip(icon = "🚀", count = projectsCount, label = "Projects")
          MetricChip(icon = "✅", count = tasksCount, label = "Tasks")
        }

        // Bottom row: "Updated 12m ago" + "Open Book" button
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Updated recently",
            style = MaterialTheme.typography.labelSmall.copy(color = TextLowEmphasis)
          )

          Button(
            onClick = onOpenBook,
            colors = ButtonDefaults.buttonColors(
              containerColor = Graphite800,
              contentColor = GoldPrimary
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .height(34.dp)
              .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
              .testTag("open_book_${user.id}")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.MenuBook,
              contentDescription = null,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Open Book",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun MetricChip(icon: String, count: Int, label: String) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Text(text = icon, fontSize = 12.sp)
    Spacer(modifier = Modifier.width(3.dp))
    Text(
      text = "$count",
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.Bold,
        color = TextHighEmphasis
      )
    )
  }
}
