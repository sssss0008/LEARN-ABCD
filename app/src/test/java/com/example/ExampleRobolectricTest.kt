package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.AlphabetRepository
import com.example.data.PreferencesManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun testAppName() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Learn ABCD", appName)
    }

    @Test
    fun testAlphabetCompleteness() {
        val letters = AlphabetRepository.letters
        assertEquals(26, letters.size)

        // Verify A to Z
        ('A'..'Z').forEachIndexed { index, char ->
            val letter = letters[index]
            assertEquals(char, letter.char)
            assertTrue(letter.words.isNotEmpty())
            assertTrue(letter.phonicsSound.isNotBlank())
            assertTrue(letter.rhyme.isNotBlank())
        }
    }

    @Test
    fun testPreferencesAndDataPreservation() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = PreferencesManager(context)

        // Complete onboarding
        prefs.completeOnboarding("Leo Explorer", "🦁", "4 - 5 yrs")
        val p1 = prefs.profile.value
        assertEquals("Leo Explorer", p1.name)
        assertEquals("🦁", p1.avatar)
        assertTrue(p1.onboardingCompleted)

        // Learn letter and trace
        prefs.markLetterLearned('C')
        prefs.markLetterTraced('C')
        prefs.addStars(50)

        val p2 = prefs.profile.value
        assertTrue(p2.learnedLetters.contains('C'))
        assertTrue(p2.tracedLetters.contains('C'))

        // Export backup code
        val backupCode = prefs.exportBackupCode()
        assertTrue(backupCode.startsWith("ABCD-"))

        // Simulate reinstall / fresh restore
        val restoredPrefs = PreferencesManager(context)
        val success = restoredPrefs.restoreFromBackupCode(backupCode)
        assertTrue(success)

        val restoredProfile = restoredPrefs.profile.value
        assertEquals("Leo Explorer", restoredProfile.name)
        assertEquals("🦁", restoredProfile.avatar)
        assertTrue(restoredProfile.learnedLetters.contains('C'))
        assertTrue(restoredProfile.tracedLetters.contains('C'))
    }

    @Test
    fun testLeaderboardRanking() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val prefs = PreferencesManager(context)
        prefs.completeOnboarding("Sammy", "⭐", "3 - 5 yrs")
        prefs.addStars(300)

        val leaderboard = prefs.getLeaderboard()
        assertTrue(leaderboard.isNotEmpty())

        val userEntry = leaderboard.find { it.isCurrentUser }
        assertNotNull(userEntry)
        // With 300+ stars, user should rank near top
        assertEquals(1, userEntry?.rank)
    }
}
