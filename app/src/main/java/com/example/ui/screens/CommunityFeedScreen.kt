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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserEntity
import com.example.ui.InnovaraViewModel
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
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
fun CommunityFeedScreen(
  viewModel: InnovaraViewModel,
  onOpenUserShelf: (UserEntity) -> Unit
) {
  val activities by viewModel.allActivities.collectAsState()
  val allUsers by viewModel.allUsers.collectAsState()
  val dateFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Graphite950)
      .padding(horizontal = 16.dp, vertical = 10.dp)
  ) {
    Text(
      text = "🔥 Community Activity Feed",
      style = MaterialTheme.typography.headlineSmall.copy(
        fontWeight = FontWeight.Black,
        color = TextHighEmphasis
      )
    )

    Text(
      text = "Live telemetry of what innovators across the university are building, researching, and achieving.",
      style = MaterialTheme.typography.bodyMedium.copy(
        color = TextMediumEmphasis,
        lineHeight = 20.sp
      ),
      modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
    )

    if (activities.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Text("No community activity recorded yet.", color = TextLowEmphasis)
      }
    } else {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.weight(1f)
      ) {
        items(activities, key = { it.id }) { act ->
          val user = allUsers.find { it.id == act.userId }
          val icon = when (act.targetType) {
            "IDEA" -> "💡"
            "PROJECT" -> "🚀"
            "TASK" -> "✅"
            "DEV_LOG" -> "🛠"
            "THOUGHT" -> "🧠"
            else -> "📝"
          }

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .border(1.dp, BorderSubtle, RoundedCornerShape(12.dp))
              .clickable {
                if (user != null) {
                  onOpenUserShelf(user)
                }
              },
            colors = CardDefaults.cardColors(containerColor = Graphite900)
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(Graphite850)
                  .border(1.dp, GoldPrimary.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(text = icon, fontSize = 20.sp)
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = act.userName,
                    style = MaterialTheme.typography.labelLarge.copy(
                      fontWeight = FontWeight.Bold,
                      color = TextHighEmphasis
                    )
                  )
                  Text(
                    text = dateFormatter.format(Date(act.timestamp)),
                    style = MaterialTheme.typography.labelSmall.copy(color = VioletAccent)
                  )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                  text = act.action,
                  style = MaterialTheme.typography.bodySmall.copy(color = TextLowEmphasis)
                )

                Text(
                  text = "“${act.targetTitle}”",
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = GoldLight
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
  }
}
