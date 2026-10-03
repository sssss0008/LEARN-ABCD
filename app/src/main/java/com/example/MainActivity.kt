package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LetterInfo
import com.example.data.PreferencesManager
import com.example.ui.components.BackupRestoreDialog
import com.example.ui.components.LetterTraceCanvas
import com.example.ui.components.ProfileEditDialog
import com.example.ui.components.VoiceSettingsDialog
import com.example.ui.screens.AboutUsScreen
import com.example.ui.screens.CertificateScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.screens.PracticeTab
import com.example.ui.screens.RankingsScreen
import com.example.ui.screens.SoundboardScreen
import com.example.ui.theme.KidsIndigoPrimary
import com.example.ui.theme.KidsOrange
import com.example.ui.theme.KidsStrawberry
import com.example.ui.theme.KidsSunYellow
import com.example.ui.theme.MyApplicationTheme
import com.example.util.TtsManager
import kotlinx.coroutines.launch

enum class MainNavTab(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    LEARN("Learn", Icons.Default.MenuBook),
    PRACTICE("Practice", Icons.Default.SportsEsports),
    ABOUT("About Us", Icons.Default.Info)
}

enum class SubScreen {
    NONE, SOUNDBOARD, RANKINGS, CERTIFICATE, TRACING
}

class MainActivity : ComponentActivity() {
    private lateinit var prefsManager: PreferencesManager
    private lateinit var ttsManager: TtsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        prefsManager = PreferencesManager(this)
        ttsManager = TtsManager(this)

        setContent {
            MyApplicationTheme {
                LearnAbcdApp(
                    prefsManager = prefsManager,
                    ttsManager = ttsManager
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsManager.shutdown()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnAbcdApp(
    prefsManager: PreferencesManager,
    ttsManager: TtsManager
) {
    val profile by prefsManager.profile.collectAsState()
    var currentNavTab by remember { mutableStateOf(MainNavTab.HOME) }
    var currentSubScreen by remember { mutableStateOf(SubScreen.NONE) }
    var tracingLetter by remember { mutableStateOf<LetterInfo?>(null) }
    var practiceInitialTab by remember { mutableStateOf(PracticeTab.QUIZ) }

    // Dialogs
    var showProfileDialog by remember { mutableStateOf(false) }
    var showBackupDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Handle back button for sub-screens
    if (currentSubScreen != SubScreen.NONE) {
        BackHandler {
            currentSubScreen = SubScreen.NONE
        }
    } else if (currentNavTab != MainNavTab.HOME) {
        BackHandler {
            currentNavTab = MainNavTab.HOME
        }
    }

    // First-launch Onboarding check
    if (!profile.onboardingCompleted) {
        OnboardingScreen(
            onComplete = { name, avatar, age ->
                prefsManager.completeOnboarding(name, avatar, age)
            },
            onSpeak = { text ->
                ttsManager.speak(text)
            }
        )
        return
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerShape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
                modifier = Modifier.width(310.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Drawer Header with Profile
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                scope.launch { drawerState.close() }
                                showProfileDialog = true
                            },
                        color = KidsIndigoPrimary.copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(KidsSunYellow.copy(alpha = 0.35f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = profile.avatar, fontSize = 32.sp)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = profile.name,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${profile.ageGroup} • ⭐ ${profile.stars} Stars",
                                    fontSize = 12.sp,
                                    color = KidsIndigoPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = KidsIndigoPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Drawer Items
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = null, tint = KidsIndigoPrimary) },
                        label = { Text("Profile & Mascot Avatar", fontWeight = FontWeight.Bold) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            showProfileDialog = true
                        },
                        colors = NavigationDrawerItemDefaults.colors(unselectedTextColor = MaterialTheme.colorScheme.onSurface)
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Extension, contentDescription = null, tint = KidsStrawberry) },
                        label = { Text("Games Hub (Pop, Match, Draw)", fontWeight = FontWeight.Bold) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            currentNavTab = MainNavTab.PRACTICE
                            currentSubScreen = SubScreen.NONE
                        }
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.GraphicEq, contentDescription = null, tint = Color(0xFF00BBF9)) },
                        label = { Text("Phonics Soundboard 🎧", fontWeight = FontWeight.Bold) },
                        selected = currentSubScreen == SubScreen.SOUNDBOARD,
                        onClick = {
                            scope.launch { drawerState.close() }
                            currentSubScreen = SubScreen.SOUNDBOARD
                        }
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Quiz, contentDescription = null, tint = KidsOrange) },
                        label = { Text("Alphabet & Sound Quiz 🎯", fontWeight = FontWeight.Bold) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            practiceInitialTab = PracticeTab.QUIZ
                            currentNavTab = MainNavTab.PRACTICE
                            currentSubScreen = SubScreen.NONE
                        }
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Leaderboard, contentDescription = null, tint = Color(0xFFEAB308)) },
                        label = { Text("Hall of Fame & Rankings 👑", fontWeight = FontWeight.Bold) },
                        selected = currentSubScreen == SubScreen.RANKINGS,
                        onClick = {
                            scope.launch { drawerState.close() }
                            currentSubScreen = SubScreen.RANKINGS
                        }
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.CardMembership, contentDescription = null, tint = Color(0xFF10B981)) },
                        label = { Text("My Alphabet Certificate 📜", fontWeight = FontWeight.Bold) },
                        selected = currentSubScreen == SubScreen.CERTIFICATE,
                        onClick = {
                            scope.launch { drawerState.close() }
                            currentSubScreen = SubScreen.CERTIFICATE
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    Spacer(modifier = Modifier.height(6.dp))

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Restore, contentDescription = null, tint = Color(0xFF2563EB)) },
                        label = { Text("Backup & Restore Data 🛡️", fontWeight = FontWeight.SemiBold) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            showBackupDialog = true
                        }
                    )

                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF64748B)) },
                        label = { Text("Audio & Parent Gate ⚙️", fontWeight = FontWeight.SemiBold) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            showSettingsDialog = true
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Learn ABCD",
                                fontWeight = FontWeight.ExtraBold,
                                color = KidsIndigoPrimary,
                                fontSize = 22.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("✨", fontSize = 16.sp)
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            },
                            modifier = Modifier.testTag("drawer_menu_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu Drawer",
                                tint = KidsIndigoPrimary
                            )
                        }
                    },
                    actions = {
                        // Quick Profile Pill
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { showProfileDialog = true }
                                .padding(end = 8.dp),
                            color = KidsSunYellow.copy(alpha = 0.25f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = profile.avatar, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "⭐ ${profile.stars}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFB45309)
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            bottomBar = {
                if (currentSubScreen == SubScreen.NONE) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("bottom_nav_bar")
                    ) {
                        MainNavTab.values().forEach { tab ->
                            val isSelected = currentNavTab == tab
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    currentNavTab = tab
                                    ttsManager.speak(tab.label)
                                },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.label,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = KidsIndigoPrimary,
                                    selectedTextColor = KidsIndigoPrimary,
                                    indicatorColor = KidsIndigoPrimary.copy(alpha = 0.15f)
                                ),
                                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Secondary Screens (if open from drawer)
                when (currentSubScreen) {
                    SubScreen.SOUNDBOARD -> {
                        SoundboardScreen(
                            onBack = { currentSubScreen = SubScreen.NONE },
                            onSpeak = { ttsManager.speak(it) }
                        )
                    }
                    SubScreen.RANKINGS -> {
                        RankingsScreen(
                            profile = profile,
                            leaderboard = prefsManager.getLeaderboard(),
                            onBack = { currentSubScreen = SubScreen.NONE }
                        )
                    }
                    SubScreen.CERTIFICATE -> {
                        CertificateScreen(
                            profile = profile,
                            onBack = { currentSubScreen = SubScreen.NONE }
                        )
                    }
                    SubScreen.TRACING -> {
                        tracingLetter?.let { letter ->
                            LetterTraceCanvas(
                                letterInfo = letter,
                                onComplete = {
                                    prefsManager.markLetterTraced(letter.char)
                                    ttsManager.speakPraise()
                                },
                                onSpeak = {
                                    ttsManager.speakLetter(letter.char)
                                },
                                onClose = {
                                    currentSubScreen = SubScreen.NONE
                                    tracingLetter = null
                                }
                            )
                        } ?: run {
                            currentSubScreen = SubScreen.NONE
                        }
                    }
                    SubScreen.NONE -> {
                        // 4 Bottom Navigation Screens
                        when (currentNavTab) {
                            MainNavTab.HOME -> {
                                HomeScreen(
                                    profile = profile,
                                    onNavigateToLearn = { currentNavTab = MainNavTab.LEARN },
                                    onNavigateToPractice = { currentNavTab = MainNavTab.PRACTICE },
                                    onOpenTrace = { letter ->
                                        tracingLetter = letter
                                        currentSubScreen = SubScreen.TRACING
                                    },
                                    onOpenBalloonGame = {
                                        practiceInitialTab = PracticeTab.BALLOON
                                        currentNavTab = MainNavTab.PRACTICE
                                    },
                                    onOpenMemoryGame = {
                                        practiceInitialTab = PracticeTab.MEMORY
                                        currentNavTab = MainNavTab.PRACTICE
                                    },
                                    onOpenQuiz = {
                                        practiceInitialTab = PracticeTab.QUIZ
                                        currentNavTab = MainNavTab.PRACTICE
                                    },
                                    onOpenProfileEdit = { showProfileDialog = true },
                                    onSpeak = { ttsManager.speak(it) }
                                )
                            }
                            MainNavTab.LEARN -> {
                                LearnScreen(
                                    profile = profile,
                                    onLetterLearned = { char ->
                                        prefsManager.markLetterLearned(char)
                                    },
                                    onOpenTrace = { letter ->
                                        tracingLetter = letter
                                        currentSubScreen = SubScreen.TRACING
                                    },
                                    onSpeak = { ttsManager.speak(it) }
                                )
                            }
                            MainNavTab.PRACTICE -> {
                                PracticeScreen(
                                    profile = profile,
                                    initialTab = practiceInitialTab,
                                    onQuizWon = { correctCount ->
                                        prefsManager.recordQuizResult(correctCount)
                                    },
                                    onBalloonPopWon = { score ->
                                        prefsManager.recordBalloonPopScore(score)
                                    },
                                    onMemoryMatchWon = { moves ->
                                        prefsManager.recordMemoryScore(moves)
                                    },
                                    onSpeak = { ttsManager.speak(it) }
                                )
                            }
                            MainNavTab.ABOUT -> {
                                AboutUsScreen(
                                    onOpenBackupRestore = { showBackupDialog = true },
                                    onOpenRanking = { currentSubScreen = SubScreen.RANKINGS }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialogs
    if (showProfileDialog) {
        ProfileEditDialog(
            profile = profile,
            onDismiss = { showProfileDialog = false },
            onSave = { name, avatar, age ->
                prefsManager.updateProfileInfo(name, avatar, age)
                ttsManager.speak("Profile updated for $name!")
            }
        )
    }

    if (showBackupDialog) {
        BackupRestoreDialog(
            backupCode = prefsManager.exportBackupCode(),
            onRestore = { code ->
                val ok = prefsManager.restoreFromBackupCode(code)
                if (ok) ttsManager.speak("Welcome back! Your learning progress is restored!")
                ok
            },
            onDismiss = { showBackupDialog = false }
        )
    }

    if (showSettingsDialog) {
        VoiceSettingsDialog(
            profile = profile,
            onSaveSettings = { speed, soundEffects, slowPhonics ->
                prefsManager.updateSettings(speed, soundEffects, slowPhonics)
                ttsManager.setSpeechSpeed(speed)
            },
            onResetProgress = {
                prefsManager.resetProgress()
                ttsManager.speak("Progress has been reset.")
            },
            onDismiss = { showSettingsDialog = false },
            onTestVoice = { speed ->
                ttsManager.setSpeechSpeed(speed)
                ttsManager.speak("Hello little explorer! Let's learn our ABCs together!")
            }
        )
    }
}
