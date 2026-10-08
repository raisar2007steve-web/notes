package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LandingScreen(
  existingUsers: List<com.example.data.local.entity.UserEntity> = emptyList(),
  onEnter: (String) -> Unit
) {
  var showNamePrompt by remember { mutableStateOf(false) }
  var enteredName by remember { mutableStateOf("") }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Graphite950)
  ) {
    // Ambient radial glow in corner
    Box(
      modifier = Modifier
        .size(360.dp)
        .align(Alignment.TopCenter)
        .background(
          Brush.radialGradient(
            listOf(GoldPrimary.copy(alpha = 0.08f), Color.Transparent)
          )
        )
    )

    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Spacer(modifier = Modifier.height(30.dp))

      // Logo Icon
      Box(
        modifier = Modifier
          .size(80.dp)
          .clip(RoundedCornerShape(22.dp))
          .background(
            Brush.linearGradient(
              listOf(Graphite800, Graphite900)
            )
          )
          .border(1.5.dp, GoldPrimary, RoundedCornerShape(22.dp)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.AutoStories,
          contentDescription = "Innovara Notes Logo",
          tint = GoldPrimary,
          modifier = Modifier.size(42.dp)
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "INNOVARA NOTES",
        style = MaterialTheme.typography.headlineMedium.copy(
          fontWeight = FontWeight.Black,
          letterSpacing = 2.sp,
          color = TextHighEmphasis
        )
      )

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "“Every student has a story.\nEvery idea deserves a place.”",
        style = MaterialTheme.typography.bodyLarge.copy(
          fontStyle = FontStyle.Italic,
          color = GoldLight,
          textAlign = TextAlign.Center,
          lineHeight = 24.sp
        )
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "A shared digital bookshelf for collegiate innovation, living research notebooks, and collective developer memory.",
        style = MaterialTheme.typography.bodyMedium.copy(
          color = TextMediumEmphasis,
          textAlign = TextAlign.Center,
          lineHeight = 22.sp
        ),
        modifier = Modifier.padding(horizontal = 16.dp)
      )

      Spacer(modifier = Modifier.height(32.dp))

      if (!showNamePrompt) {
        Button(
          onClick = { showNamePrompt = true },
          colors = ButtonDefaults.buttonColors(
            containerColor = GoldPrimary,
            contentColor = Graphite950
          ),
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag("enter_innovara_button")
        ) {
          Text(
            text = "Enter Innovara Notes",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
      } else {
        AnimatedVisibility(
          visible = showNamePrompt,
          enter = fadeIn() + slideInVertically()
        ) {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(18.dp))
              .border(1.dp, BorderSubtle, RoundedCornerShape(18.dp)),
            colors = CardDefaults.cardColors(containerColor = Graphite900)
          ) {
            Column(modifier = Modifier.padding(20.dp)) {
              Text(
                text = "Who are you?",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontWeight = FontWeight.Bold,
                  color = TextHighEmphasis
                )
              )
              Text(
                text = "Enter your student name to access or write to your personal shelf.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextLowEmphasis),
                modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
              )

              OutlinedTextField(
                value = enteredName,
                onValueChange = { enteredName = it },
                label = { Text("Your Name") },
                singleLine = true,
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("student_name_input"),
                colors = OutlinedTextFieldDefaults.colors(
                  focusedBorderColor = GoldPrimary,
                  unfocusedBorderColor = Graphite700,
                  focusedTextColor = TextHighEmphasis,
                  unfocusedTextColor = TextHighEmphasis,
                  focusedContainerColor = Graphite850,
                  unfocusedContainerColor = Graphite850
                )
              )

              if (existingUsers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                  text = "Or continue as a student shelf on this device:",
                  style = MaterialTheme.typography.labelSmall.copy(color = TextMediumEmphasis)
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  existingUsers.forEach { user ->
                    Box(
                      modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (enteredName == user.name) GoldPrimary.copy(alpha = 0.2f) else Graphite800)
                        .border(
                          1.dp,
                          if (enteredName == user.name) GoldPrimary else Graphite700,
                          RoundedCornerShape(20.dp)
                        )
                        .clickable { enteredName = user.name }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                      Text(
                        text = user.name,
                        style = MaterialTheme.typography.labelMedium.copy(
                          color = if (enteredName == user.name) GoldLight else TextMediumEmphasis,
                          fontWeight = if (enteredName == user.name) FontWeight.Bold else FontWeight.Normal
                        )
                      )
                    }
                  }
                }
              } else {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                  text = "✓ Your student identity and shelf data will be remembered permanently on this device.",
                  style = MaterialTheme.typography.labelSmall.copy(color = TextLowEmphasis)
                )
              }

              Spacer(modifier = Modifier.height(20.dp))

              Button(
                onClick = {
                  if (enteredName.isNotBlank()) {
                    onEnter(enteredName.trim())
                  }
                },
                enabled = enteredName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                  containerColor = GoldPrimary,
                  contentColor = Graphite950
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .fillMaxWidth()
                  .height(50.dp)
                  .testTag("submit_name_button")
              ) {
                Text(
                  text = "Enter Notes",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}
