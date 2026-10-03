package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LetterInfo
import com.example.ui.theme.KidsIndigoPrimary
import com.example.ui.theme.KidsMintGreen
import com.example.ui.theme.KidsStrawberry
import com.example.ui.theme.KidsSunYellow

data class ColoredPath(
    val path: Path,
    val color: Color,
    val strokeWidth: Float
)

@Composable
fun LetterTraceCanvas(
    letterInfo: LetterInfo,
    onComplete: () -> Unit,
    onSpeak: () -> Unit,
    onClose: () -> Unit
) {
    val paths = remember { mutableStateListOf<ColoredPath>() }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    var selectedColor by remember { mutableStateOf(Color(0xFFFF5964)) }
    var strokeWidth by remember { mutableStateOf(28f) }
    var completed by remember { mutableStateOf(false) }

    val colors = listOf(
        Color(0xFFFF5964), // Coral Red
        Color(0xFFFFB703), // Sun Yellow
        Color(0xFF06D6A0), // Mint Green
        Color(0xFF00BBF9), // Sky Blue
        Color(0xFF9B5DE5), // Purple
        Color(0xFFFB5607)  // Bright Orange
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Trace Letter ${letterInfo.char} ${letterInfo.lowercase}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Follow the shape with your finger!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onSpeak,
                    modifier = Modifier
                        .size(48.dp)
                        .background(KidsIndigoPrimary.copy(alpha = 0.12f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Speak Letter",
                        tint = KidsIndigoPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Canvas Drawing Surface
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1E293B))
                    .pointerInput(selectedColor, strokeWidth) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val newPath = Path().apply { moveTo(offset.x, offset.y) }
                                currentPath = newPath
                                paths.add(ColoredPath(newPath, selectedColor, strokeWidth))
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                currentPath?.lineTo(change.position.x, change.position.y)
                            },
                            onDragEnd = {
                                currentPath = null
                            }
                        )
                    }
                    .testTag("trace_canvas_area")
            ) {
                // Background Guide Letter & Paths
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    // Draw chalkboard lines
                    drawLine(
                        color = Color(0x33FFFFFF),
                        start = Offset(24f, height * 0.25f),
                        end = Offset(width - 24f, height * 0.25f),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = Color(0x55FFB703),
                        start = Offset(24f, height * 0.5f),
                        end = Offset(width - 24f, height * 0.5f),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = Color(0x33FFFFFF),
                        start = Offset(24f, height * 0.75f),
                        end = Offset(width - 24f, height * 0.75f),
                        strokeWidth = 2f
                    )

                    // Draw background letter stencil
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.argb(70, 255, 255, 255)
                        textSize = height * 0.65f
                        textAlign = android.graphics.Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                    drawContext.canvas.nativeCanvas.drawText(
                        "${letterInfo.char} ${letterInfo.lowercase}",
                        width / 2f,
                        height * 0.68f,
                        paint
                    )

                    // Draw all user traced paths
                    for (cp in paths) {
                        drawPath(
                            path = cp.path,
                            color = cp.color,
                            style = Stroke(
                                width = cp.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }

                // If completed banner overlay
                if (completed) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xCC000000)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "🎉",
                                fontSize = 48.sp
                            )
                            Text(
                                text = "Star Traced! +10 Stars ⭐",
                                style = MaterialTheme.typography.titleLarge,
                                color = KidsSunYellow,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Color Palette Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                colors.forEach { color ->
                    val isSelected = selectedColor == color
                    Surface(
                        modifier = Modifier
                            .size(if (isSelected) 42.dp else 34.dp)
                            .clip(CircleShape),
                        color = color,
                        shadowElevation = if (isSelected) 6.dp else 2.dp,
                        onClick = { selectedColor = color }
                    ) {
                        if (isSelected) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected Color",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Clear button
                IconButton(
                    onClick = { paths.clear() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFEF4444).copy(alpha = 0.12f), CircleShape)
                        .testTag("clear_trace_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear Canvas",
                        tint = Color(0xFFEF4444)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalButton(
                    onClick = onClose,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Close")
                }

                Button(
                    onClick = {
                        completed = true
                        onComplete()
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .testTag("finish_trace_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = KidsMintGreen)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("I Traced It!", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
