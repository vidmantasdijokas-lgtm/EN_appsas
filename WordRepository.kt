package com.example.wordlearn

import android.content.Context
import org.json.JSONArray

data class WordPair(val word: String, val translation: String)

/**
 * Skaito žodžius iš assets/words.json ir seka, kuris žodis buvo rodytas paskutinis,
 * naudojant SharedPreferences (kad progresas išliktų tarp appso paleidimų).
 */
class WordRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("wordlearn_prefs", Context.MODE_PRIVATE)

    private fun loadWords(): List<WordPair> {
        val json = context.assets.open("words.json").bufferedReader(Charsets.UTF_8).use { it.readText() }
        val arr = JSONArray(json)
        val list = mutableListOf<WordPair>()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            list.add(WordPair(obj.getString("word"), obj.getString("translation")))
        }
        return list
    }

    /** Grąžina kitą žodį eilės tvarka (nuosekliai, kad nesikartotų atsitiktinai) ir pažymi jį kaip parodytą. */
    fun getNextWord(): WordPair {
        val words = loadWords()
        val currentIndex = prefs.getInt("current_index", 0)
        val word = words[currentIndex % words.size]
        prefs.edit().putInt("current_index", (currentIndex + 1) % words.size).apply()
        return word
    }

    /** Grąžina paskutinį parodytą žodį (nekeičiant progreso) - naudojama pagrindiniam ekranui rodyti. */
    fun getLastShownWord(): WordPair {
        val words = loadWords()
        val currentIndex = prefs.getInt("current_index", 0)
        // current_index rodo į SEKANTĮ žodį, todėl paskutinis parodytas yra prieš jį
        val lastIndex = if (currentIndex == 0) words.size - 1 else currentIndex - 1
        return words[lastIndex]
    }

    fun isReminderEnabled(): Boolean = prefs.getBoolean("reminder_enabled", false)

    fun setReminderEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("reminder_enabled", enabled).apply()
    }
}
