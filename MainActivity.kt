package com.example.wordlearn

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.wordlearn.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: WordRepository

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startReminders()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = WordRepository(this)

        showCurrentWordOnScreen()
        updateToggleButtonLabel()

        binding.showNowButton.setOnClickListener {
            val word = repository.getNextWord()
            NotificationHelper.showWordNotification(this, word)
            binding.wordText.text = word.word
            binding.translationText.text = word.translation
        }

        binding.toggleButton.setOnClickListener {
            if (repository.isReminderEnabled()) {
                stopReminders()
            } else {
                requestPermissionAndStart()
            }
            updateToggleButtonLabel()
        }
    }

    private fun showCurrentWordOnScreen() {
        val word = repository.getLastShownWord()
        binding.wordText.text = word.word
        binding.translationText.text = word.translation
    }

    private fun updateToggleButtonLabel() {
        binding.toggleButton.text = if (repository.isReminderEnabled())
            getString(R.string.stop_button) else getString(R.string.start_button)
    }

    private fun requestPermissionAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }
        startReminders()
    }

    private fun startReminders() {
        val request = PeriodicWorkRequestBuilder<WordReminderWorker>(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            WordReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
        repository.setReminderEnabled(true)
        updateToggleButtonLabel()
    }

    private fun stopReminders() {
        WorkManager.getInstance(this).cancelUniqueWork(WordReminderWorker.WORK_NAME)
        repository.setReminderEnabled(false)
        updateToggleButtonLabel()
    }
}
