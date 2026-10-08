package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.Graphite700
import com.example.ui.theme.Graphite800
import com.example.ui.theme.Graphite850
import com.example.ui.theme.Graphite950
import com.example.ui.theme.TextHighEmphasis
import com.example.ui.theme.TextLowEmphasis
import com.example.ui.theme.TextMediumEmphasis
import com.example.ui.theme.VioletAccent

@Composable
fun MarkdownView(
  markdown: String,
  modifier: Modifier = Modifier
) {
  val lines = markdown.lines()
  var inCodeBlock = false
  val codeBlockContent = StringBuilder()

  Column(modifier = modifier) {
    for (line in lines) {
      val trimmed = line.trim()

      if (trimmed.startsWith("```")) {
        if (inCodeBlock) {
          // Finish code block
          val code = codeBlockContent.toString().trimEnd()
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(Graphite950)
              .border(1.dp, Graphite700, RoundedCornerShape(8.dp))
              .padding(12.dp)
          ) {
            Text(
              text = code,
              fontFamily = FontFamily.Monospace,
              fontSize = 13.sp,
              color = GoldPrimary,
              lineHeight = 18.sp,
              modifier = Modifier.horizontalScroll(rememberScrollState())
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          codeBlockContent.clear()
          inCodeBlock = false
        } else {
          inCodeBlock = true
          codeBlockContent.clear()
        }
        continue
      }

      if (inCodeBlock) {
        codeBlockContent.append(line).append("\n")
        continue
      }

      when {
        trimmed.startsWith("# ") -> {
          Text(
            text = trimmed.removePrefix("# ").trim(),
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = GoldPrimary,
              fontSize = 20.sp
            ),
            modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
          )
        }
        trimmed.startsWith("## ") -> {
          Text(
            text = trimmed.removePrefix("## ").trim(),
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.SemiBold,
              color = VioletAccent,
              fontSize = 17.sp
            ),
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
          )
        }
        trimmed.startsWith("### ") -> {
          Text(
            text = trimmed.removePrefix("### ").trim(),
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Medium,
              color = TextHighEmphasis,
              fontSize = 15.sp
            ),
            modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
          )
        }
        trimmed.startsWith("> ") -> {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(Graphite850)
              .border(1.dp, Graphite700, RoundedCornerShape(4.dp))
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            Box(
              modifier = Modifier
                .width(3.dp)
                .height(20.dp)
                .background(GoldPrimary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = trimmed.removePrefix("> ").trim(),
              fontStyle = FontStyle.Italic,
              color = TextMediumEmphasis,
              fontSize = 14.sp
            )
          }
        }
        trimmed.startsWith("- [ ] ") || trimmed.startsWith("* [ ] ") -> {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 2.dp)
          ) {
            Icon(
              imageVector = Icons.Outlined.Circle,
              contentDescription = "Unchecked",
              tint = TextLowEmphasis,
              modifier = Modifier.padding(end = 8.dp)
            )
            Text(
              text = trimmed.substring(6).trim(),
              color = TextHighEmphasis,
              fontSize = 14.sp
            )
          }
        }
        trimmed.startsWith("- [x] ") || trimmed.startsWith("* [x] ") || trimmed.startsWith("- [X] ") -> {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 2.dp)
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = "Checked",
              tint = GoldPrimary,
              modifier = Modifier.padding(end = 8.dp)
            )
            Text(
              text = trimmed.substring(6).trim(),
              color = TextLowEmphasis,
              fontSize = 14.sp
            )
          }
        }
        trimmed.startsWith("- ") || trimmed.startsWith("* ") -> {
          Row(
            modifier = Modifier.padding(vertical = 2.dp)
          ) {
            Text(text = "•", color = GoldPrimary, fontSize = 16.sp, modifier = Modifier.padding(end = 8.dp))
            InlineFormattedText(trimmed.substring(2).trim())
          }
        }
        trimmed.startsWith("✓ ") -> {
          Row(modifier = Modifier.padding(vertical = 2.dp)) {
            Text(text = "✓", color = Color(0xFF34D399), fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
            InlineFormattedText(trimmed.removePrefix("✓ ").trim())
          }
        }
        trimmed.startsWith("⚠ ") -> {
          Row(modifier = Modifier.padding(vertical = 2.dp)) {
            Text(text = "⚠", color = Color(0xFFF59E0B), fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
            InlineFormattedText(trimmed.removePrefix("⚠ ").trim())
          }
        }
        trimmed.startsWith("→ ") -> {
          Row(modifier = Modifier.padding(vertical = 2.dp)) {
            Text(text = "→", color = VioletAccent, fontSize = 14.sp, modifier = Modifier.padding(end = 8.dp))
            InlineFormattedText(trimmed.removePrefix("→ ").trim())
          }
        }
        trimmed.isEmpty() -> {
          Spacer(modifier = Modifier.height(4.dp))
        }
        else -> {
          InlineFormattedText(line)
        }
      }
    }
  }
}

@Composable
fun InlineFormattedText(text: String) {
  val annotated = buildAnnotatedString {
    var i = 0
    while (i < text.length) {
      if (text.startsWith("**", i)) {
        val end = text.indexOf("**", i + 2)
        if (end != -1) {
          withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = TextHighEmphasis)) {
            append(text.substring(i + 2, end))
          }
          i = end + 2
          continue
        }
      } else if (text[i] == '`') {
        val end = text.indexOf('`', i + 1)
        if (end != -1) {
          withStyle(
            SpanStyle(
              fontFamily = FontFamily.Monospace,
              color = GoldPrimary,
              background = Graphite800
            )
          ) {
            append(" " + text.substring(i + 1, end) + " ")
          }
          i = end + 1
          continue
        }
      } else if (text[i] == '*' && i + 1 < text.length && text[i + 1] != '*') {
        val end = text.indexOf('*', i + 1)
        if (end != -1) {
          withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = TextMediumEmphasis)) {
            append(text.substring(i + 1, end))
          }
          i = end + 1
          continue
        }
      }

      append(text[i])
      i++
    }
  }

  Text(
    text = annotated,
    color = TextMediumEmphasis,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    modifier = Modifier.padding(vertical = 1.dp)
  )
}
