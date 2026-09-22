package com.example.wordlearn

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Kas kartą, kai WorkManager paleidžia šią užduotį (maždaug kas valandą),
 * paimamas kitas žodis iš sąrašo ir parodomas kaip pranešimas.
 */
class WordReminderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val repository = WordRepository(applicationContext)
        val word = repository.getNextWord()
        NotificationHelper.showWordNotification(applicationContext, word)
        return Result.success()
    }

    companion object {
        const val WORK_NAME = "hourly_word_reminder"
    }
}
