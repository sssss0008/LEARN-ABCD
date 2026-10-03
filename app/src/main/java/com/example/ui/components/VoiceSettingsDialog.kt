package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.KidProfile
import com.example.ui.theme.KidsIndigoPrimary
import com.example.ui.theme.KidsStrawberry

@Composable
fun VoiceSettingsDialog(
    profile: KidProfile,
    onSaveSettings: (speed: Float, soundEffects: Boolean, slowPhonics: Boolean) -> Unit,
    onResetProgress: () -> Unit,
    onDismiss: () -> Unit,
    onTestVoice: (Float) -> Unit
) {
    var voiceSpeed by remember { mutableFloatStateOf(profile.voiceSpeed) }
    var soundEffects by remember { mutableStateOf(profile.soundEffectsEnabled) }
    var slowPhonics by remember { mutableStateOf(profile.slowPhonics) }
    var showParentGate by remember { mutableStateOf(false) }
    var mathAnswer by remember { mutableStateOf("") }
    var gateError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Audio & Parent Settings ⚙️",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = KidsIndigoPrimary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Voice Speed Slider
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Speech Rate:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = when {
                                voiceSpeed <= 0.7f -> "Toddler Slow (0.65x)"
                                voiceSpeed <= 0.9f -> "Normal Kid (0.85x)"
                                else -> "Standard (1.0x)"
                            },
                            color = KidsIndigoPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Slider(
                        value = voiceSpeed,
                        onValueChange = { voiceSpeed = it },
                        valueRange = 0.5f..1.1f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = KidsIndigoPrimary,
                            activeTrackColor = KidsIndigoPrimary
                        )
                    )

                    Button(
                        onClick = { onTestVoice(voiceSpeed) },
                        colors = ButtonDefaults.buttonColors(containerColor = KidsIndigoPrimary.copy(alpha = 0.12f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = KidsIndigoPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Test Voice Sound", color = KidsIndigoPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Sound Effects Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Sound Effects & Chimes", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Plays celebration chimes & pop sounds", fontSize = 11.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = soundEffects,
                        onCheckedChange = { soundEffects = it }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Slow Phonics Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Extra Gentle Phonics", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Repeats letter sounds slowly", fontSize = 11.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = slowPhonics,
                        onCheckedChange = { slowPhonics = it }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Parent Gate / Reset Progress
                if (!showParentGate) {
                    Button(
                        onClick = { showParentGate = true },
                        colors = ButtonDefaults.buttonColors(containerColor = KidsStrawberry.copy(alpha = 0.12f)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = KidsStrawberry, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Parent Gate: Reset Progress", color = KidsStrawberry, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Parent Math Check: What is 4 + 3?",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = KidsStrawberry
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = mathAnswer,
                                onValueChange = {
                                    mathAnswer = it
                                    gateError = false
                                },
                                singleLine = true,
                                placeholder = { Text("Answer") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            if (gateError) {
                                Text("Incorrect answer", color = Color.Red, fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    if (mathAnswer.trim() == "7") {
                                        onResetProgress()
                                        onDismiss()
                                    } else {
                                        gateError = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = KidsStrawberry),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Confirm Reset", color = Color.White)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FilledTonalButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            onSaveSettings(voiceSpeed, soundEffects, slowPhonics)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = KidsIndigoPrimary),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier.weight(1f).testTag("save_settings_button")
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
