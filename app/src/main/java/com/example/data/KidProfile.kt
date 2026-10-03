package com.example.data

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val iconEmoji: String,
    val requiredStars: Int
)

val ALL_BADGES = listOf(
    Badge("FIRST_STEP", "First Step", "Began learning ABC!", "🌱", 5),
    Badge("PHONICS_FAN", "Phonics Fan", "Listened to 5 letter sounds", "🎧", 15),
    Badge("TRACE_ARTIST", "Trace Artist", "Traced 3 letters on chalkboard", "🎨", 30),
    Badge("BALLOON_POPPER", "Balloon Hero", "Popped 10 letter balloons", "🎈", 50),
    Badge("QUIZ_WHIZ", "Quiz Whiz", "Answered 10 quiz questions correctly", "💡", 75),
    Badge("MEMORY_GENIUS", "Memory Champ", "Matched letter cards in game", "🧠", 100),
    Badge("ALPHABET_EXPLORER", "Alphabet Explorer", "Explored all 26 letters", "🚀", 150),
    Badge("SUPER_STAR", "Alphabet Master", "Earned 250 golden stars!", "👑", 250)
)

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val avatar: String,
    val stars: Int,
    val isCurrentUser: Boolean = false,
    val title: String
)

data class KidProfile(
    val name: String = "Little Explorer",
    val avatar: String = "🦁",
    val ageGroup: String = "3 - 5 yrs",
    val stars: Int = 10,
    val streakDays: Int = 1,
    val lastActiveDate: String = "",
    val learnedLetters: Set<Char> = setOf('A', 'B'),
    val tracedLetters: Set<Char> = emptySet(),
    val unlockedBadgeIds: Set<String> = setOf("FIRST_STEP"),
    val balloonPopScore: Int = 0,
    val memoryMatchScore: Int = 0,
    val quizScore: Int = 0,
    val voiceSpeed: Float = 0.85f,
    val soundEffectsEnabled: Boolean = true,
    val slowPhonics: Boolean = false,
    val onboardingCompleted: Boolean = false
)
