package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.KidsBubblegum
import com.example.ui.theme.KidsIndigoPrimary
import com.example.ui.theme.KidsMintGreen
import com.example.ui.theme.KidsStrawberry
import com.example.ui.theme.KidsSunYellow

val AVATAR_OPTIONS = listOf(
    "🦁" to "Leo the Lion",
    "🐰" to "Bella Bunny",
    "🐻" to "Barnaby Bear",
    "🐼" to "Pip Panda",
    "🦖" to "Rexy Dino",
    "🦄" to "Sparkle Unicorn",
    "🤖" to "Robo Buddy",
    "⭐" to "Super Star"
)

val AGE_OPTIONS = listOf(
    "2 - 3 yrs" to "Toddler Play",
    "4 - 5 yrs" to "Preschool Explorer",
    "6+ yrs" to "Kindergarten Champ"
)

@Composable
fun OnboardingScreen(
    onComplete: (name: String, avatar: String, age: String) -> Unit,
    onSpeak: (String) -> Unit
) {
    var step by remember { mutableIntStateOf(0) }
    var kidName by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf("🦁") }
    var selectedAge by remember { mutableStateOf("3 - 5 yrs") }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF4F46E5),
            Color(0xFF7C3AED),
            Color(0xFFFF5964)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Stepper Dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { i ->
                    val isActive = i == step
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isActive) 24.dp else 10.dp, 10.dp)
                            .clip(CircleShape)
                            .background(if (isActive) KidsSunYellow else Color.White.copy(alpha = 0.4f))
                    )
                }
            }

            // Animated Screen Content
            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "OnboardingStep"
            ) { currentStep ->
                when (currentStep) {
                    0 -> StepWelcomeAndName(
                        name = kidName,
                        onNameChange = { kidName = it },
                        onSpeak = onSpeak
                    )
                    1 -> StepPickAvatar(
                        selectedAvatar = selectedAvatar,
                        onAvatarSelect = { selectedAvatar = it }
                    )
                    2 -> StepAgeAndStart(
                        name = kidName.ifBlank { "Little Explorer" },
                        avatar = selectedAvatar,
                        selectedAge = selectedAge,
                        onAgeSelect = { selectedAge = it }
                    )
                }
            }

            // Bottom Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                if (step < 2) {
                    Button(
                        onClick = {
                            if (step == 0 && kidName.isNotBlank()) {
                                onSpeak("Hello $kidName! Welcome to Alphabet Adventure!")
                            }
                            step++
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("onboarding_next_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = KidsSunYellow),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            text = if (step == 0) "Next: Pick Buddy ➔" else "Next: Learning Goal ➔",
                            color = Color(0xFF1E1B4B),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Button(
                        onClick = {
                            onSpeak("Let's learn the ABCs!")
                            onComplete(kidName, selectedAvatar, selectedAge)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .testTag("onboarding_start_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = KidsMintGreen),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Start Learning ABC! 🎉",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepWelcomeAndName(
    name: String,
    onNameChange: (String) -> Unit,
    onSpeak: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✨ 🎈 🌟",
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Welcome to",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF6B7280)
            )

            Text(
                text = "Learn ABCD",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = KidsIndigoPrimary
            )

            Text(
                text = "The magic world of letters, phonics & fun games!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B5563),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "What is your name, superstar?",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = KidsStrawberry
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                placeholder = { Text("e.g. Leo, Mia, Oliver...") },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KidsIndigoPrimary,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedContainerColor = Color(0xFFF8FAFC)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("kid_name_input")
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onSpeak("Welcome to Learn ABCD! What is your name?") },
                color = KidsIndigoPrimary.copy(alpha = 0.1f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🔊", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Listen to intro",
                        color = KidsIndigoPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StepPickAvatar(
    selectedAvatar: String,
    onAvatarSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pick Your Buddy!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = KidsIndigoPrimary
            )
            Text(
                text = "Choose a learning animal mascot to join you!",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(AVATAR_OPTIONS) { (emoji, label) ->
                    val isSelected = selectedAvatar == emoji
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (isSelected) KidsSunYellow.copy(alpha = 0.35f)
                                else Color(0xFFF1F5F9)
                            )
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) KidsSunYellow else Color.Transparent,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { onAvatarSelect(emoji) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 32.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val currentBuddyName = AVATAR_OPTIONS.find { it.first == selectedAvatar }?.second ?: ""
            Text(
                text = "Selected Buddy: $selectedAvatar $currentBuddyName",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = KidsStrawberry
            )
        }
    }
}

@Composable
private fun StepAgeAndStart(
    name: String,
    avatar: String,
    selectedAge: String,
    onAgeSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(KidsSunYellow.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = avatar, fontSize = 48.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Yay, $name!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = KidsIndigoPrimary
            )

            Text(
                text = "You're getting +15 Bonus Stars to start! ⭐",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = KidsStrawberry
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Select Age Group:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF374151)
            )

            Spacer(modifier = Modifier.height(8.dp))

            AGE_OPTIONS.forEach { (age, subtitle) ->
                val isSelected = selectedAge.contains(age.substring(0, 1)) || selectedAge == age
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onAgeSelect(age) },
                    color = if (isSelected) KidsIndigoPrimary.copy(alpha = 0.12f) else Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) KidsIndigoPrimary else Color(0xFFE2E8F0)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = age,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) KidsIndigoPrimary else Color(0xFF1E293B)
                            )
                            Text(
                                text = subtitle,
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        if (isSelected) {
                            Text("✨", fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}
