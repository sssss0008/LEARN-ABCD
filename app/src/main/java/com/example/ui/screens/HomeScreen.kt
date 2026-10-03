package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AlphabetRepository
import com.example.data.KidProfile
import com.example.data.LetterInfo
import com.example.ui.theme.KidsBubblegum
import com.example.ui.theme.KidsIndigoPrimary
import com.example.ui.theme.KidsMintGreen
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.KidsStrawberry
import com.example.ui.theme.KidsSunYellow
import java.util.Calendar

@Composable
fun HomeScreen(
    profile: KidProfile,
    onNavigateToLearn: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onOpenTrace: (LetterInfo) -> Unit,
    onOpenBalloonGame: () -> Unit,
    onOpenMemoryGame: () -> Unit,
    onOpenQuiz: () -> Unit,
    onOpenProfileEdit: () -> Unit,
    onSpeak: (String) -> Unit
) {
    // Pick daily letter based on day of month (1..31) % 26
    val letterOfTheDay = remember {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        AlphabetRepository.letters[(day - 1) % AlphabetRepository.letters.size]
    }

    val learnedCount = profile.learnedLetters.size
    val progressFraction = (learnedCount / 26f).coerceIn(0f, 1f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))

            // Kid Greeting & Status Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onOpenProfileEdit() }
                        .padding(end = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(KidsSunYellow.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = profile.avatar, fontSize = 32.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Hi, ${profile.name}!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "${profile.ageGroup} • Ready to learn",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Stars & Streak Pills
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Streak Pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = KidsOrange.copy(alpha = 0.15f),
                        modifier = Modifier.clip(RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🔥", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${profile.streakDays}d",
                                fontWeight = FontWeight.Bold,
                                color = KidsOrange,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Stars Pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = KidsSunYellow.copy(alpha = 0.2f),
                        modifier = Modifier.clip(RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⭐", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${profile.stars}",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFB45309),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Letter of the Day Spotlight Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("letter_of_the_day_card"),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(letterInfoToColor(letterOfTheDay)),
                                    KidsIndigoPrimary
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.25f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = KidsSunYellow,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "LETTER OF THE DAY",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Speaker icon
                            IconButton(
                                onClick = {
                                    val firstWord = letterOfTheDay.words.first()
                                    onSpeak("Letter ${letterOfTheDay.char}! ${letterOfTheDay.char} is for ${firstWord.word}!")
                                },
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Speak Letter of Day",
                                    tint = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Big Letter Badge
                            Surface(
                                modifier = Modifier.size(90.dp),
                                shape = RoundedCornerShape(24.dp),
                                color = Color.White.copy(alpha = 0.95f),
                                shadowElevation = 4.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${letterOfTheDay.char}${letterOfTheDay.lowercase}",
                                        fontSize = 42.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(letterInfoToColor(letterOfTheDay))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(18.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                val firstWord = letterOfTheDay.words.first()
                                Text(
                                    text = "${firstWord.emoji} ${firstWord.word}",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = letterOfTheDay.phonicsSpelling,
                                    fontSize = 14.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = firstWord.description,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f),
                                    maxLines = 2
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quick Actions on Letter of the Day
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val firstWord = letterOfTheDay.words.first()
                                    onSpeak("Let's hear: ${letterOfTheDay.char}! ${firstWord.sentence}")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color(letterInfoToColor(letterOfTheDay))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Hear Sound",
                                    color = Color(letterInfoToColor(letterOfTheDay)),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }

                            Button(
                                onClick = { onOpenTrace(letterOfTheDay) },
                                colors = ButtonDefaults.buttonColors(containerColor = KidsSunYellow),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "✍️ Trace It",
                                    color = Color(0xFF1E1B4B),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Learning Progress Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Alphabet Progress",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$learnedCount / 26 Letters",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = KidsIndigoPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        color = KidsMintGreen,
                        trackColor = Color(0xFFE2E8F0)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ProgressStat(label = "Learned", value = "$learnedCount", emoji = "📚")
                        ProgressStat(label = "Traced", value = "${profile.tracedLetters.size}", emoji = "🎨")
                        ProgressStat(label = "Badges", value = "${profile.unlockedBadgeIds.size}", emoji = "🏅")
                        ProgressStat(label = "Stars", value = "${profile.stars}", emoji = "⭐")
                    }
                }
            }
        }

        // Quick Adventure Launchers
        item {
            Text(
                text = "What would you like to do?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Learn A - Z",
                        subtitle = "Letters & Phonics",
                        emoji = "📖",
                        bgColor = Color(0xFFEEF2FF),
                        accentColor = KidsIndigoPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToLearn
                    )

                    QuickActionCard(
                        title = "Balloon Pop",
                        subtitle = "Fast Tap Fun",
                        emoji = "🎈",
                        bgColor = Color(0xFFFFECEB),
                        accentColor = KidsStrawberry,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenBalloonGame
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Card Match",
                        subtitle = "Letter Memory",
                        emoji = "🃏",
                        bgColor = Color(0xFFE6FFFA),
                        accentColor = KidsMintGreen,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenMemoryGame
                    )

                    QuickActionCard(
                        title = "Play Quiz",
                        subtitle = "Win Golden Stars",
                        emoji = "🎯",
                        bgColor = Color(0xFFFFF8E7),
                        accentColor = KidsOrange,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenQuiz
                    )
                }
            }
        }

        // Daily Mascot Tip
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(20.dp),
                color = KidsIndigoPrimary.copy(alpha = 0.08f)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = profile.avatar, fontSize = 36.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Buddy Tip of the Day:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = KidsIndigoPrimary
                        )
                        Text(
                            text = "Tracing letters with different rainbow colors helps your fingers remember the shapes!",
                            fontSize = 12.sp,
                            color = Color(0xFF475569)
                        )
                    }
                    IconButton(
                        onClick = {
                            onSpeak("Buddy tip! Tracing letters with different rainbow colors helps your fingers remember the shapes! Keep practicing!")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Speak tip",
                            tint = KidsIndigoPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressStat(label: String, value: String, emoji: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = emoji, fontSize = 20.sp)
        Text(
            text = value,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    emoji: String,
    bgColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(108.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = emoji, fontSize = 28.sp)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("➔", color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
        }
    }
}

fun letterInfoToColor(letter: LetterInfo): Long = letter.colorHex
