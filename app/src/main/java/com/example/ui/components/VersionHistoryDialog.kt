package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entity.VersionEntity
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.Graphite700
import com.example.ui.theme.Graphite850
import com.example.ui.theme.Graphite900
import com.example.ui.theme.TextHighEmphasis
import com.example.ui.theme.TextLowEmphasis
import com.example.ui.theme.TextMediumEmphasis
import com.example.ui.theme.VioletAccent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun VersionHistoryDialog(
  targetTitle: String,
  versions: List<VersionEntity>,
  onDismiss: () -> Unit
) {
  val dateFormatter = SimpleDateFormat("d MMM yyyy — h:mm a", Locale.getDefault())

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = 540.dp)
        .clip(RoundedCornerShape(20.dp))
        .border(1.dp, BorderSubtle, RoundedCornerShape(20.dp)),
      color = Graphite900
    ) {
      Column(
        modifier = Modifier.padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.History,
              contentDescription = null,
              tint = GoldPrimary
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Version History",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextHighEmphasis
                )
              )
              Text(
                text = targetTitle,
                style = MaterialTheme.typography.bodySmall.copy(color = TextLowEmphasis),
                maxLines = 1
              )
            }
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_version_history")
          ) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMediumEmphasis)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (versions.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Initial version. No previous modifications logged.",
              color = TextLowEmphasis,
              fontSize = 13.sp
            )
          }
        } else {
          LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f, fill = false)
          ) {
            items(versions) { ver ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .background(Graphite850)
                  .border(1.dp, Graphite700, RoundedCornerShape(12.dp))
                  .padding(14.dp)
              ) {
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(GoldPrimary.copy(alpha = 0.15f))
                    .border(1.dp, GoldPrimary, CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "v${ver.versionNumber}",
                    color = GoldPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                  )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                  ) {
                    Text(
                      text = "Updated by ${ver.editedBy}",
                      style = MaterialTheme.typography.labelMedium.copy(
                        color = TextHighEmphasis,
                        fontWeight = FontWeight.SemiBold
                      )
                    )
                    Text(
                      text = dateFormatter.format(Date(ver.createdAt)),
                      style = MaterialTheme.typography.labelSmall.copy(color = VioletAccent)
                    )
                  }

                  Spacer(modifier = Modifier.height(4.dp))

                  Text(
                    text = ver.contentSnippet,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMediumEmphasis),
                    fontSize = 12.sp
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
