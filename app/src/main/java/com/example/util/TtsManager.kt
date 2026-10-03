package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isReady = false
    private var speechRate = 0.85f
    private var pitch = 1.12f

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.getDefault())
            }
            tts?.setSpeechRate(speechRate)
            tts?.setPitch(pitch)
            isReady = true
        } else {
            Log.w("TtsManager", "TTS initialization failed: $status")
        }
    }

    fun setSpeechSpeed(speed: Float) {
        speechRate = speed
        if (isReady) {
            tts?.setSpeechRate(speechRate)
        }
    }

    fun speak(text: String, flush: Boolean = true) {
        if (!isReady || tts == null) return
        val queueMode = if (flush) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD
        tts?.speak(text, queueMode, null, "Utterance_${System.currentTimeMillis()}")
    }

    fun speakLetter(char: Char) {
        val uppercase = char.uppercaseChar()
        val lowercase = char.lowercaseChar()
        speak("Letter $uppercase. Big $uppercase, little $lowercase.")
    }

    fun speakPhonics(char: Char, phonicsSound: String, sampleWord: String) {
        speak("The letter $char makes the sound $phonicsSound. $phonicsSound, $phonicsSound, $sampleWord!")
    }

    fun speakWord(word: String, sentence: String) {
        speak("$word. $sentence")
    }

    fun speakPraise() {
        val praises = listOf(
            "Great job!",
            "Super star!",
            "Awesome!",
            "You did it!",
            "Wonderful!",
            "Brilliant!",
            "High five!"
        )
        speak(praises.random())
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
            isReady = false
        } catch (_: Exception) {}
    }
}
