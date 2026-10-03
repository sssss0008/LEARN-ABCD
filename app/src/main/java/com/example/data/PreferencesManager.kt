package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("learn_abcd_prefs", Context.MODE_PRIVATE)

    private val _profile = MutableStateFlow(loadProfile())
    val profile: StateFlow<KidProfile> = _profile.asStateFlow()

    init {
        updateStreak()
    }

    private fun loadProfile(): KidProfile {
        val name = prefs.getString("kid_name", "Little Explorer") ?: "Little Explorer"
        val avatar = prefs.getString("kid_avatar", "🦁") ?: "🦁"
        val ageGroup = prefs.getString("kid_age", "3 - 5 yrs") ?: "3 - 5 yrs"
        val stars = prefs.getInt("kid_stars", 10)
        val streak = prefs.getInt("kid_streak", 1)
        val lastDate = prefs.getString("kid_last_date", "") ?: ""
        val learnedStr = prefs.getString("learned_letters", "AB") ?: "AB"
        val tracedStr = prefs.getString("traced_letters", "") ?: ""
        val badgesStr = prefs.getString("unlocked_badges", "FIRST_STEP") ?: "FIRST_STEP"
        val balloonScore = prefs.getInt("score_balloon", 0)
        val memoryScore = prefs.getInt("score_memory", 0)
        val quizScore = prefs.getInt("score_quiz", 0)
        val voiceSpeed = prefs.getFloat("voice_speed", 0.85f)
        val soundEffects = prefs.getBoolean("sound_effects", true)
        val slowPhonics = prefs.getBoolean("slow_phonics", false)
        val onboarding = prefs.getBoolean("onboarding_done", false)

        val learned = learnedStr.toCharArray().toSet()
        val traced = tracedStr.toCharArray().toSet()
        val badges = badgesStr.split(",").filter { it.isNotBlank() }.toSet()

        return KidProfile(
            name = name,
            avatar = avatar,
            ageGroup = ageGroup,
            stars = stars,
            streakDays = streak,
            lastActiveDate = lastDate,
            learnedLetters = learned,
            tracedLetters = traced,
            unlockedBadgeIds = badges,
            balloonPopScore = balloonScore,
            memoryMatchScore = memoryScore,
            quizScore = quizScore,
            voiceSpeed = voiceSpeed,
            soundEffectsEnabled = soundEffects,
            slowPhonics = slowPhonics,
            onboardingCompleted = onboarding
        )
    }

    private fun saveProfile(profile: KidProfile) {
        prefs.edit().apply {
            putString("kid_name", profile.name)
            putString("kid_avatar", profile.avatar)
            putString("kid_age", profile.ageGroup)
            putInt("kid_stars", profile.stars)
            putInt("kid_streak", profile.streakDays)
            putString("kid_last_date", profile.lastActiveDate)
            putString("learned_letters", profile.learnedLetters.joinToString(""))
            putString("traced_letters", profile.tracedLetters.joinToString(""))
            putString("unlocked_badges", profile.unlockedBadgeIds.joinToString(","))
            putInt("score_balloon", profile.balloonPopScore)
            putInt("score_memory", profile.memoryMatchScore)
            putInt("score_quiz", profile.quizScore)
            putFloat("voice_speed", profile.voiceSpeed)
            putBoolean("sound_effects", profile.soundEffectsEnabled)
            putBoolean("slow_phonics", profile.slowPhonics)
            putBoolean("onboarding_done", profile.onboardingCompleted)
            apply()
        }
        _profile.value = profile
    }

    private fun updateStreak() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val current = _profile.value
        if (current.lastActiveDate.isEmpty()) {
            saveProfile(current.copy(lastActiveDate = today, streakDays = 1))
            return
        }
        if (current.lastActiveDate == today) {
            // Already active today
            return
        }
        // Check if yesterday
        try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val last = format.parse(current.lastActiveDate)
            val now = format.parse(today)
            if (last != null && now != null) {
                val diffDays = ((now.time - last.time) / (1000 * 60 * 60 * 24)).toInt()
                val newStreak = if (diffDays == 1) current.streakDays + 1 else 1
                saveProfile(current.copy(lastActiveDate = today, streakDays = newStreak))
            }
        } catch (_: Exception) {
            saveProfile(current.copy(lastActiveDate = today))
        }
    }

    fun completeOnboarding(name: String, avatar: String, ageGroup: String) {
        val current = _profile.value
        val updated = current.copy(
            name = name.ifBlank { "Little Explorer" },
            avatar = avatar,
            ageGroup = ageGroup,
            onboardingCompleted = true,
            stars = current.stars + 15
        )
        saveProfile(checkBadgeUnlocks(updated))
    }

    fun updateProfileInfo(name: String, avatar: String, ageGroup: String) {
        val current = _profile.value
        saveProfile(current.copy(
            name = name.ifBlank { "Little Explorer" },
            avatar = avatar,
            ageGroup = ageGroup
        ))
    }

    fun addStars(amount: Int) {
        val current = _profile.value
        val updated = current.copy(stars = (current.stars + amount).coerceAtLeast(0))
        saveProfile(checkBadgeUnlocks(updated))
    }

    fun markLetterLearned(letter: Char) {
        val current = _profile.value
        val newLearned = current.learnedLetters + letter
        val addedStars = if (!current.learnedLetters.contains(letter)) 5 else 1
        val updated = current.copy(
            learnedLetters = newLearned,
            stars = current.stars + addedStars
        )
        saveProfile(checkBadgeUnlocks(updated))
    }

    fun markLetterTraced(letter: Char) {
        val current = _profile.value
        val newTraced = current.tracedLetters + letter
        val addedStars = if (!current.tracedLetters.contains(letter)) 10 else 2
        val updated = current.copy(
            tracedLetters = newTraced,
            stars = current.stars + addedStars
        )
        saveProfile(checkBadgeUnlocks(updated))
    }

    fun recordBalloonPopScore(score: Int) {
        val current = _profile.value
        val newHigh = maxOf(current.balloonPopScore, score)
        val addedStars = (score / 2).coerceAtLeast(2)
        val updated = current.copy(
            balloonPopScore = newHigh,
            stars = current.stars + addedStars
        )
        saveProfile(checkBadgeUnlocks(updated))
    }

    fun recordMemoryScore(moves: Int) {
        val current = _profile.value
        val score = (100 - moves * 5).coerceAtLeast(10)
        val newHigh = maxOf(current.memoryMatchScore, score)
        val updated = current.copy(
            memoryMatchScore = newHigh,
            stars = current.stars + 10
        )
        saveProfile(checkBadgeUnlocks(updated))
    }

    fun recordQuizResult(correctCount: Int) {
        val current = _profile.value
        val addedStars = correctCount * 3
        val updated = current.copy(
            quizScore = current.quizScore + correctCount,
            stars = current.stars + addedStars
        )
        saveProfile(checkBadgeUnlocks(updated))
    }

    fun updateSettings(voiceSpeed: Float, soundEffects: Boolean, slowPhonics: Boolean) {
        val current = _profile.value
        saveProfile(current.copy(
            voiceSpeed = voiceSpeed,
            soundEffectsEnabled = soundEffects,
            slowPhonics = slowPhonics
        ))
    }

    private fun checkBadgeUnlocks(profile: KidProfile): KidProfile {
        val currentBadges = profile.unlockedBadgeIds.toMutableSet()
        ALL_BADGES.forEach { badge ->
            if (profile.stars >= badge.requiredStars) {
                currentBadges.add(badge.id)
            }
        }
        if (profile.tracedLetters.size >= 3) currentBadges.add("TRACE_ARTIST")
        if (profile.learnedLetters.size >= 5) currentBadges.add("PHONICS_FAN")
        if (profile.learnedLetters.size >= 26) currentBadges.add("ALPHABET_EXPLORER")
        if (profile.balloonPopScore >= 10) currentBadges.add("BALLOON_POPPER")
        if (profile.quizScore >= 10) currentBadges.add("QUIZ_WHIZ")
        if (profile.memoryMatchScore >= 50) currentBadges.add("MEMORY_GENIUS")
        if (profile.stars >= 250) currentBadges.add("SUPER_STAR")

        return profile.copy(unlockedBadgeIds = currentBadges)
    }

    fun resetProgress() {
        val current = _profile.value
        saveProfile(KidProfile(
            name = current.name,
            avatar = current.avatar,
            ageGroup = current.ageGroup,
            onboardingCompleted = true,
            stars = 10,
            streakDays = 1
        ))
    }

    /**
     * Generates a portable, URL-safe base64 backup code
     * containing all user progress and profile data.
     */
    fun exportBackupCode(): String {
        val p = _profile.value
        val json = JSONObject().apply {
            put("v", 1)
            put("name", p.name)
            put("avatar", p.avatar)
            put("age", p.ageGroup)
            put("stars", p.stars)
            put("streak", p.streakDays)
            put("lastDate", p.lastActiveDate)
            put("learned", p.learnedLetters.joinToString(""))
            put("traced", p.tracedLetters.joinToString(""))
            put("badges", JSONArray(p.unlockedBadgeIds.toList()))
            put("balloonScore", p.balloonPopScore)
            put("memoryScore", p.memoryMatchScore)
            put("quizScore", p.quizScore)
            put("voiceSpeed", p.voiceSpeed.toDouble())
            put("soundEffects", p.soundEffectsEnabled)
            put("slowPhonics", p.slowPhonics)
        }
        return "ABCD-" + Base64.encodeToString(
            json.toString().toByteArray(Charsets.UTF_8),
            Base64.NO_WRAP or Base64.URL_SAFE
        )
    }

    /**
     * Restores user progress from a backup code.
     * Returns true if successfully parsed and restored, false otherwise.
     */
    fun restoreFromBackupCode(code: String): Boolean {
        try {
            val raw = code.trim().removePrefix("ABCD-")
            val jsonBytes = try {
                Base64.decode(raw, Base64.URL_SAFE or Base64.NO_WRAP)
            } catch (_: Exception) {
                Base64.decode(raw, Base64.DEFAULT)
            }
            val json = JSONObject(String(jsonBytes, Charsets.UTF_8))

            val name = json.optString("name", "Little Explorer")
            val avatar = json.optString("avatar", "🦁")
            val age = json.optString("age", "3 - 5 yrs")
            val stars = json.optInt("stars", 10)
            val streak = json.optInt("streak", 1)
            val lastDate = json.optString("lastDate", "")
            val learnedStr = json.optString("learned", "AB")
            val tracedStr = json.optString("traced", "")
            val balloonScore = json.optInt("balloonScore", 0)
            val memoryScore = json.optInt("memoryScore", 0)
            val quizScore = json.optInt("quizScore", 0)
            val voiceSpeed = json.optDouble("voiceSpeed", 0.85).toFloat()
            val soundEffects = json.optBoolean("soundEffects", true)
            val slowPhonics = json.optBoolean("slowPhonics", false)

            val badgeSet = mutableSetOf<String>()
            val badgesArray = json.optJSONArray("badges")
            if (badgesArray != null) {
                for (i in 0 until badgesArray.length()) {
                    badgeSet.add(badgesArray.getString(i))
                }
            } else {
                badgeSet.add("FIRST_STEP")
            }

            val restored = KidProfile(
                name = name,
                avatar = avatar,
                ageGroup = age,
                stars = stars,
                streakDays = streak,
                lastActiveDate = lastDate,
                learnedLetters = learnedStr.toCharArray().toSet(),
                tracedLetters = tracedStr.toCharArray().toSet(),
                unlockedBadgeIds = badgeSet,
                balloonPopScore = balloonScore,
                memoryMatchScore = memoryScore,
                quizScore = quizScore,
                voiceSpeed = voiceSpeed,
                soundEffectsEnabled = soundEffects,
                slowPhonics = slowPhonics,
                onboardingCompleted = true
            )
            saveProfile(checkBadgeUnlocks(restored))
            return true
        } catch (_: Exception) {
            return false
        }
    }

    /**
     * Returns dynamic leaderboard showing cute animal companions alongside the user.
     */
    fun getLeaderboard(): List<LeaderboardEntry> {
        val user = _profile.value
        val simulatedBuddies = listOf(
            LeaderboardEntry(0, "Leo Lion", "🦁", 280, false, "King of ABC"),
            LeaderboardEntry(0, "Pip Penguin", "🐧", 210, false, "Phonics Pro"),
            LeaderboardEntry(0, "Daisy Dino", "🦖", 160, false, "Letter Hunter"),
            LeaderboardEntry(0, "Bella Bunny", "🐰", 120, false, "Speedy Reader"),
            LeaderboardEntry(0, "Oliver Owl", "🦉", 85, false, "Wise Thinker"),
            LeaderboardEntry(0, "Sammy Star", "⭐", 45, false, "Bright Sprout")
        )

        val currentUserEntry = LeaderboardEntry(
            rank = 0,
            name = user.name,
            avatar = user.avatar,
            stars = user.stars,
            isCurrentUser = true,
            title = when {
                user.stars >= 250 -> "Grand Master"
                user.stars >= 150 -> "Alphabet Champion"
                user.stars >= 80 -> "Star Wonder"
                user.stars >= 30 -> "Curious Explorer"
                else -> "Budding Genius"
            }
        )

        val combined = (simulatedBuddies + currentUserEntry).sortedByDescending { it.stars }
        return combined.mapIndexed { index, item -> item.copy(rank = index + 1) }
    }
}
