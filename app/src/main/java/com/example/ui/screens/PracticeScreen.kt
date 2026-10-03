package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AlphabetRepository
import com.example.data.KidProfile
import com.example.ui.theme.KidsBubblegum
import com.example.ui.theme.KidsIndigoPrimary
import com.example.ui.theme.KidsMintGreen
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.KidsStrawberry
import com.example.ui.theme.KidsSunYellow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class PracticeTab(val title: String, val emoji: String) {
    QUIZ("Quiz", "🎯"),
    BALLOON("Balloon Pop", "🎈"),
    MEMORY("Card Match", "🃏"),
    SPELL("Spell Fun", "🔤")
}

@Composable
fun PracticeScreen(
    profile: KidProfile,
    initialTab: PracticeTab = PracticeTab.QUIZ,
    onQuizWon: (Int) -> Unit,
    onBalloonPopWon: (Int) -> Unit,
    onMemoryMatchWon: (Int) -> Unit,
    onSpeak: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(initialTab) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        // Tab Selector Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab.ordinal,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                    color = KidsIndigoPrimary,
                    height = 3.dp
                )
            }
        ) {
            PracticeTab.values().forEach { tab ->
                val isSelected = selectedTab == tab
                Tab(
                    selected = isSelected,
                    onClick = {
                        selectedTab = tab
                        onSpeak(tab.title)
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(tab.emoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = tab.title,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) KidsIndigoPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Content
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "PracticeContent",
            modifier = Modifier.weight(1f)
        ) { tab ->
            when (tab) {
                PracticeTab.QUIZ -> PhonicsQuizGame(
                    onQuizComplete = onQuizWon,
                    onSpeak = onSpeak
                )
                PracticeTab.BALLOON -> BalloonPopGame(
                    onGameEnd = onBalloonPopWon,
                    onSpeak = onSpeak
                )
                PracticeTab.MEMORY -> LetterMemoryMatchGame(
                    onGameWon = onMemoryMatchWon,
                    onSpeak = onSpeak
                )
                PracticeTab.SPELL -> WordSpellingGame(
                    onSpellComplete = { onQuizWon(1) },
                    onSpeak = onSpeak
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 1. Phonics & Letter Sound Quiz
// -------------------------------------------------------------
data class QuizQuestion(
    val promptText: String,
    val speakPrompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

@Composable
fun PhonicsQuizGame(
    onQuizComplete: (Int) -> Unit,
    onSpeak: (String) -> Unit
) {
    var score by remember { mutableIntStateOf(0) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var gameOver by remember { mutableStateOf(false) }

    val questions = remember { generateQuizQuestions() }
    val current = questions[currentQuestionIndex]

    LaunchedEffect(currentQuestionIndex) {
        selectedOption = null
        showResult = false
        onSpeak(current.speakPrompt)
    }

    if (gameOver) {
        CelebrationCard(
            title = "Quiz Master! 🏆",
            subtitle = "You got $score out of ${questions.size} correct!",
            starsAwarded = score * 3,
            onRestart = {
                score = 0
                currentQuestionIndex = 0
                gameOver = false
            }
        )
        return
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Question Counter & Sound Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                    style = MaterialTheme.typography.titleSmall,
                    color = KidsIndigoPrimary,
                    fontWeight = FontWeight.Bold
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⭐ $score", fontWeight = FontWeight.Bold, color = KidsOrange)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { onSpeak(current.speakPrompt) },
                        modifier = Modifier
                            .size(40.dp)
                            .background(KidsIndigoPrimary.copy(alpha = 0.12f), CircleShape)
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Hear Question", tint = KidsIndigoPrimary)
                    }
                }
            }

            // Question Prompt Box
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(20.dp),
                color = KidsSunYellow.copy(alpha = 0.15f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎯", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = current.promptText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Options Cards (3 Big Kid Friendly Buttons)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                current.options.forEachIndexed { index, option ->
                    val isChosen = selectedOption == index
                    val isCorrect = index == current.correctIndex

                    val bgColor = when {
                        showResult && isCorrect -> KidsMintGreen.copy(alpha = 0.25f)
                        showResult && isChosen && !isCorrect -> KidsStrawberry.copy(alpha = 0.25f)
                        else -> Color(0xFFF8FAFC)
                    }

                    val borderColor = when {
                        showResult && isCorrect -> KidsMintGreen
                        showResult && isChosen && !isCorrect -> KidsStrawberry
                        else -> Color(0xFFE2E8F0)
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable(enabled = !showResult) {
                                selectedOption = index
                                showResult = true
                                if (isCorrect) {
                                    score++
                                    onSpeak("Yay! Correct! ${current.explanation}")
                                } else {
                                    onSpeak("Nice try! ${current.explanation}")
                                }
                            }
                            .testTag("quiz_option_$index"),
                        color = bgColor,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = option,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1B4B)
                            )
                            if (showResult && isCorrect) {
                                Text("🎉 Correct!", color = KidsMintGreen, fontWeight = FontWeight.Bold)
                            } else if (showResult && isChosen) {
                                Text("Oops!", color = KidsStrawberry, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Next / Finish Button
            if (showResult) {
                Button(
                    onClick = {
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                        } else {
                            gameOver = true
                            onQuizComplete(score)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("quiz_next_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = KidsIndigoPrimary),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Text(
                        text = if (currentQuestionIndex < questions.size - 1) "Next Question ➔" else "See Results! 🏆",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(52.dp))
            }
        }
    }
}

private fun generateQuizQuestions(): List<QuizQuestion> {
    return listOf(
        QuizQuestion(
            promptText = "Which letter makes the sound /b/ as in Ball?",
            speakPrompt = "Which letter makes the sound buh as in Ball?",
            options = listOf("B b  ⚽", "D d  🐶", "P p  🐼"),
            correctIndex = 0,
            explanation = "Letter B makes the /b/ sound for Ball!"
        ),
        QuizQuestion(
            promptText = "Which letter comes right after 'M'?",
            speakPrompt = "Which letter comes right after M?",
            options = listOf("L l  🦁", "N n  🪺", "O o  🦉"),
            correctIndex = 1,
            explanation = "Letter N comes right after letter M!"
        ),
        QuizQuestion(
            promptText = "Find the word that starts with letter 'A':",
            speakPrompt = "Find the word that starts with letter A!",
            options = listOf("🍎 Apple", "🐱 Cat", "🐟 Fish"),
            correctIndex = 0,
            explanation = "A is for delicious red Apple!"
        ),
        QuizQuestion(
            promptText = "Which letter makes the sound /s/ as in Sun?",
            speakPrompt = "Which letter makes the sound sss as in Sun?",
            options = listOf("C c  🚗", "S s  ☀️", "Z z  🦓"),
            correctIndex = 1,
            explanation = "Letter S makes the sss sound for Sun!"
        ),
        QuizQuestion(
            promptText = "Which animal begins with letter 'Z'?",
            speakPrompt = "Which animal begins with letter Z?",
            options = listOf("🦁 Lion", "🦓 Zebra", "🐻 Bear"),
            correctIndex = 1,
            explanation = "Z is for Zebra with striped feet!"
        )
    )
}

// -------------------------------------------------------------
// 2. Balloon Pop Game
// -------------------------------------------------------------
data class BalloonItem(
    val id: Int,
    val char: Char,
    val color: Color,
    var isPopped: Boolean = false
)

@Composable
fun BalloonPopGame(
    onGameEnd: (Int) -> Unit,
    onSpeak: (String) -> Unit
) {
    var score by remember { mutableIntStateOf(0) }
    var targetLetter by remember { mutableStateOf('A') }
    val balloons = remember { mutableStateListOf<BalloonItem>() }
    var round by remember { mutableIntStateOf(1) }
    val maxRounds = 5

    val balloonColors = listOf(
        KidsStrawberry, KidsSunYellow, KidsMintGreen,
        KidsIndigoPrimary, KidsBubblegum, KidsOrange
    )

    fun startNewRound() {
        val randomLetter = ('A'..'Z').random()
        targetLetter = randomLetter
        balloons.clear()
        val choices = mutableListOf(randomLetter)
        while (choices.size < 6) {
            val other = ('A'..'Z').random()
            if (!choices.contains(other)) choices.add(other)
        }
        choices.shuffle()
        choices.forEachIndexed { i, c ->
            balloons.add(BalloonItem(i, c, balloonColors[i % balloonColors.size]))
        }
        onSpeak("Pop letter $targetLetter!")
    }

    LaunchedEffect(Unit) {
        startNewRound()
    }

    if (round > maxRounds) {
        CelebrationCard(
            title = "Balloon Pop Champion! 🎈",
            subtitle = "You scored $score points popping balloons!",
            starsAwarded = (score / 2).coerceAtLeast(5),
            onRestart = {
                score = 0
                round = 1
                startNewRound()
            }
        )
        return
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Round $round of $maxRounds",
                    fontWeight = FontWeight.Bold,
                    color = KidsIndigoPrimary
                )
                Text(
                    text = "Score: $score 🎈",
                    fontWeight = FontWeight.ExtraBold,
                    color = KidsStrawberry,
                    fontSize = 16.sp
                )
            }

            // Target Letter Announcer
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onSpeak("Pop letter $targetLetter!") },
                color = KidsStrawberry.copy(alpha = 0.15f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎈", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "POP THE BALLOON WITH: ",
                        fontWeight = FontWeight.Bold,
                        color = KidsStrawberry,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "$targetLetter",
                        fontWeight = FontWeight.ExtraBold,
                        color = KidsStrawberry,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.VolumeUp, contentDescription = null, tint = KidsStrawberry)
                }
            }

            // 6 Balloons Grid (2 rows of 3)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(balloons) { balloon ->
                    if (balloon.isPopped) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("💥 Pop!", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = KidsSunYellow)
                        }
                    } else {
                        // Floating Balloon Shape
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(balloon.color)
                                .clickable {
                                    if (balloon.char == targetLetter) {
                                        balloon.isPopped = true
                                        score += 10
                                        onSpeak("Pop! Super job! You found $targetLetter!")
                                        round++
                                        if (round <= maxRounds) {
                                            startNewRound()
                                        } else {
                                            onGameEnd(score)
                                        }
                                    } else {
                                        onSpeak("That's letter ${balloon.char}. Try to find letter $targetLetter!")
                                    }
                                }
                                .testTag("balloon_${balloon.char}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${balloon.char}",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                                Text("▲", fontSize = 10.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }

            Text(
                text = "Tap on the balloon that matches letter '$targetLetter'!",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// -------------------------------------------------------------
// 3. Letter Memory Match
// -------------------------------------------------------------
data class MemoryCard(
    val id: Int,
    val value: String,
    val pairId: Int,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)

@Composable
fun LetterMemoryMatchGame(
    onGameWon: (Int) -> Unit,
    onSpeak: (String) -> Unit
) {
    var moves by remember { mutableIntStateOf(0) }
    var flippedIndices = remember { mutableStateListOf<Int>() }
    val cards = remember { mutableStateListOf<MemoryCard>() }
    var isWon by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun resetGame() {
        moves = 0
        flippedIndices.clear()
        isWon = false
        val pairs = listOf(
            ("A" to "🍎"),
            ("B" to "⚽"),
            ("C" to "🐱"),
            ("D" to "🐶")
        )
        val generated = mutableListOf<MemoryCard>()
        pairs.forEachIndexed { index, (l, e) ->
            generated.add(MemoryCard(id = index * 2, value = l, pairId = index))
            generated.add(MemoryCard(id = index * 2 + 1, value = e, pairId = index))
        }
        generated.shuffle()
        cards.clear()
        cards.addAll(generated)
        onSpeak("Find matching pairs! Match the letter to its picture!")
    }

    LaunchedEffect(Unit) {
        resetGame()
    }

    if (isWon) {
        CelebrationCard(
            title = "Memory Champion! 🃏",
            subtitle = "You found all pairs in $moves moves!",
            starsAwarded = 15,
            onRestart = { resetGame() }
        )
        return
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Match Letter with Picture",
                    fontWeight = FontWeight.Bold,
                    color = KidsIndigoPrimary
                )
                Text("Moves: $moves", fontWeight = FontWeight.Bold, color = KidsOrange)
            }

            // 4x2 Cards Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cards.indices.toList()) { index ->
                    val card = cards[index]
                    val isRevealed = card.isFlipped || card.isMatched

                    Card(
                        modifier = Modifier
                            .height(110.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(enabled = !isRevealed && flippedIndices.size < 2) {
                                card.isFlipped = true
                                flippedIndices.add(index)
                                onSpeak(card.value)

                                if (flippedIndices.size == 2) {
                                    moves++
                                    val first = cards[flippedIndices[0]]
                                    val second = cards[flippedIndices[1]]

                                    if (first.pairId == second.pairId) {
                                        first.isMatched = true
                                        second.isMatched = true
                                        flippedIndices.clear()
                                        onSpeak("Pair matched! Excellent!")
                                        if (cards.all { it.isMatched }) {
                                            isWon = true
                                            onGameWon(moves)
                                        }
                                    } else {
                                        scope.launch {
                                            delay(900)
                                            first.isFlipped = false
                                            second.isFlipped = false
                                            flippedIndices.clear()
                                        }
                                    }
                                }
                            }
                            .testTag("memory_card_$index"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isRevealed) KidsMintGreen.copy(alpha = 0.2f) else KidsIndigoPrimary
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            2.dp,
                            if (card.isMatched) KidsMintGreen else KidsIndigoPrimary
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isRevealed) {
                                Text(
                                    text = card.value,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E1B4B)
                                )
                            } else {
                                Text("❓", fontSize = 28.sp)
                            }
                        }
                    }
                }
            }

            IconButton(
                onClick = { resetGame() },
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFF1F5F9), CircleShape)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Restart Memory Match", tint = KidsIndigoPrimary)
            }
        }
    }
}

// -------------------------------------------------------------
// 4. Word Spelling & Missing Letter Puzzle
// -------------------------------------------------------------
data class SpellingPuzzle(
    val emoji: String,
    val word: String,
    val missingIndex: Int,
    val options: List<Char>
)

@Composable
fun WordSpellingGame(
    onSpellComplete: () -> Unit,
    onSpeak: (String) -> Unit
) {
    val puzzles = listOf(
        SpellingPuzzle("🐱", "CAT", 1, listOf('A', 'O', 'U')),
        SpellingPuzzle("🐶", "DOG", 1, listOf('O', 'E', 'I')),
        SpellingPuzzle("☀️", "SUN", 1, listOf('U', 'A', 'E')),
        SpellingPuzzle("🦁", "LION", 0, listOf('L', 'M', 'R')),
        SpellingPuzzle("🍎", "APPLE", 0, listOf('A', 'E', 'O'))
    )

    var puzzleIndex by remember { mutableIntStateOf(0) }
    var solved by remember { mutableStateOf(false) }
    val current = puzzles[puzzleIndex]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fill in the Missing Letter",
                    fontWeight = FontWeight.Bold,
                    color = KidsIndigoPrimary
                )
                Text("Word ${puzzleIndex + 1}/${puzzles.size}", color = Color.Gray)
            }

            // Word with blank
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(current.emoji, fontSize = 64.sp)
                Spacer(modifier = Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    current.word.forEachIndexed { idx, char ->
                        if (idx == current.missingIndex && !solved) {
                            Surface(
                                modifier = Modifier.size(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = KidsSunYellow.copy(alpha = 0.3f),
                                border = androidx.compose.foundation.BorderStroke(2.dp, KidsSunYellow)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("?", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = KidsOrange)
                                }
                            }
                        } else {
                            Surface(
                                modifier = Modifier.size(52.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = KidsIndigoPrimary.copy(alpha = 0.15f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "$char",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = KidsIndigoPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Options
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Tap the letter that fits:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    current.options.forEach { char ->
                        val isTarget = char == current.word[current.missingIndex]
                        Button(
                            onClick = {
                                if (isTarget) {
                                    solved = true
                                    onSpeak("Correct! ${current.word}!")
                                    onSpellComplete()
                                } else {
                                    onSpeak("Not quite! Try another letter!")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = KidsIndigoPrimary),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.size(60.dp)
                        ) {
                            Text("$char", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            if (solved) {
                Button(
                    onClick = {
                        solved = false
                        puzzleIndex = (puzzleIndex + 1) % puzzles.size
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = KidsMintGreen),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Next Word ➔", color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else {
                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}

@Composable
fun CelebrationCard(
    title: String,
    subtitle: String,
    starsAwarded: Int,
    onRestart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🎉 ⭐ 🏆", fontSize = 42.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = KidsIndigoPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = KidsSunYellow.copy(alpha = 0.2f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⭐", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+$starsAwarded Stars Added!",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFB45309)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(containerColor = KidsIndigoPrimary),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Play Again!", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
