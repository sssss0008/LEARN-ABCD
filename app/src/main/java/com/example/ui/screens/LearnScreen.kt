package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.AlphabetRepository
import com.example.data.KidProfile
import com.example.data.LetterInfo
import com.example.data.WordItem
import com.example.ui.theme.KidsIndigoPrimary
import com.example.ui.theme.KidsMintGreen
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.KidsStrawberry
import com.example.ui.theme.KidsSunYellow

enum class CaseDisplayMode {
    BOTH, UPPERCASE, LOWERCASE
}

@Composable
fun LearnScreen(
    profile: KidProfile,
    onLetterLearned: (Char) -> Unit,
    onOpenTrace: (LetterInfo) -> Unit,
    onSpeak: (String) -> Unit
) {
    var displayMode by remember { mutableStateOf(CaseDisplayMode.BOTH) }
    var selectedLetter by remember { mutableStateOf<LetterInfo?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // Screen Subtitle & Case Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Alphabet A to Z",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Tap a letter to listen, explore & trace!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Case Toggle Pills
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                FilterChip(
                    selected = displayMode == CaseDisplayMode.BOTH,
                    onClick = { displayMode = CaseDisplayMode.BOTH },
                    label = { Text("Aa", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = KidsIndigoPrimary,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = displayMode == CaseDisplayMode.UPPERCASE,
                    onClick = { displayMode = CaseDisplayMode.UPPERCASE },
                    label = { Text("A", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = KidsIndigoPrimary,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = displayMode == CaseDisplayMode.LOWERCASE,
                    onClick = { displayMode = CaseDisplayMode.LOWERCASE },
                    label = { Text("a", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = KidsIndigoPrimary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Letter Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(AlphabetRepository.letters) { letter ->
                val isLearned = profile.learnedLetters.contains(letter.char)
                val isTraced = profile.tracedLetters.contains(letter.char)
                val letterColor = Color(letter.colorHex)

                Card(
                    modifier = Modifier
                        .height(120.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable {
                            selectedLetter = letter
                            onLetterLearned(letter.char)
                            onSpeak("Letter ${letter.char}! ${letter.phonicsSpelling}")
                        }
                        .testTag("letter_card_${letter.char}"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = letterColor.copy(alpha = 0.12f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isLearned) 2.dp else 1.dp,
                        color = if (isLearned) letterColor else letterColor.copy(alpha = 0.3f)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    ) {
                        // Badges top right
                        Row(
                            modifier = Modifier.align(Alignment.TopEnd),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            if (isTraced) {
                                Text("✍️", fontSize = 12.sp)
                            }
                            if (isLearned) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Learned",
                                    tint = KidsMintGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Big Letter Text
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val textToDisplay = when (displayMode) {
                                CaseDisplayMode.BOTH -> "${letter.char}${letter.lowercase}"
                                CaseDisplayMode.UPPERCASE -> "${letter.char}"
                                CaseDisplayMode.LOWERCASE -> "${letter.lowercase}"
                            }

                            Text(
                                text = textToDisplay,
                                fontSize = if (displayMode == CaseDisplayMode.BOTH) 32.sp else 38.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = letterColor
                            )
                        }

                        // Word Preview Bottom
                        Row(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val sampleWord = letter.words.first()
                            Text(
                                text = "${sampleWord.emoji} ${sampleWord.word}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    // Letter Detail Dialog
    selectedLetter?.let { letter ->
        LetterDetailDialog(
            letter = letter,
            isTraced = profile.tracedLetters.contains(letter.char),
            onDismiss = { selectedLetter = null },
            onOpenTrace = {
                selectedLetter = null
                onOpenTrace(letter)
            },
            onSpeak = onSpeak,
            onNavigatePrevious = {
                val index = AlphabetRepository.letters.indexOf(letter)
                if (index > 0) {
                    val prev = AlphabetRepository.letters[index - 1]
                    selectedLetter = prev
                    onLetterLearned(prev.char)
                    onSpeak("Letter ${prev.char}!")
                }
            },
            onNavigateNext = {
                val index = AlphabetRepository.letters.indexOf(letter)
                if (index < AlphabetRepository.letters.size - 1) {
                    val next = AlphabetRepository.letters[index + 1]
                    selectedLetter = next
                    onLetterLearned(next.char)
                    onSpeak("Letter ${next.char}!")
                }
            }
        )
    }
}

@Composable
fun LetterDetailDialog(
    letter: LetterInfo,
    isTraced: Boolean,
    onDismiss: () -> Unit,
    onOpenTrace: () -> Unit,
    onSpeak: (String) -> Unit,
    onNavigatePrevious: () -> Unit,
    onNavigateNext: () -> Unit
) {
    val letterColor = Color(letter.colorHex)
    val currentIndex = AlphabetRepository.letters.indexOf(letter)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigatePrevious,
                        enabled = currentIndex > 0
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous letter",
                            tint = if (currentIndex > 0) letterColor else Color.LightGray
                        )
                    }

                    Text(
                        text = "Letter ${letter.char} of 26",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    IconButton(
                        onClick = onNavigateNext,
                        enabled = currentIndex < AlphabetRepository.letters.size - 1
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next letter",
                            tint = if (currentIndex < AlphabetRepository.letters.size - 1) letterColor else Color.LightGray
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFF1F5F9), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Hero Letter Display
                Surface(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(32.dp)),
                    color = letterColor.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(3.dp, letterColor)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${letter.char} ${letter.lowercase}",
                            fontSize = 54.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = letterColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Phonics Sound Pill
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            onSpeak("Letter ${letter.char} makes the sound: ${letter.phonicsSound}! Like ${letter.words.first().word}!")
                        },
                    color = letterColor.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = letterColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sound: ${letter.phonicsSpelling}",
                            fontWeight = FontWeight.Bold,
                            color = letterColor,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 3 Word Cards
                Text(
                    text = "Words that start with ${letter.char}:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                letter.words.forEach { word ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                onSpeak("${word.word}! ${word.sentence}")
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = word.emoji, fontSize = 34.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = word.word,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color(0xFF1E293B)
                                )
                                Text(
                                    text = word.description,
                                    fontSize = 12.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                            IconButton(
                                onClick = { onSpeak("${word.word}. ${word.sentence}") }
                            ) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = "Speak word",
                                    tint = letterColor
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Rhyme Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = KidsSunYellow.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🎶", fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = letter.rhyme,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF78350F),
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { onSpeak(letter.rhyme) }
                        ) {
                            Icon(
                                Icons.Default.VolumeUp,
                                contentDescription = "Speak rhyme",
                                tint = KidsOrange
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Trace Button
                Button(
                    onClick = onOpenTrace,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("dialog_trace_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = KidsIndigoPrimary),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Icon(Icons.Default.Create, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isTraced) "Practice Tracing Again ✍️" else "Trace This Letter! ✍️",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
