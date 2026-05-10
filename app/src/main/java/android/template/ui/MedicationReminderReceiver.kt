package android.template.ui

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A [BroadcastReceiver] that handles events triggered by [AlarmManager] or notification action buttons.
 * It is responsible for showing medication reminders, handling the "Take" action, and processing
 * "Snooze" requests to re-trigger the alarm.
 */
@AndroidEntryPoint
class MedicationReminderReceiver : BroadcastReceiver() {

    @Inject lateinit var medicationDao: MedicationDao
    @Inject lateinit var alarmScheduler: AlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MedicationReminder", "Received action: ${intent.action}")
        val medName = intent.getStringExtra("MED_NAME") ?: "your medication"
        val medId = intent.getLongExtra("MED_ID", -1L)

        when (intent.action) {
            "android.template.ui.ACTION_REMINDER" -> {
                showNotification(context, medName, medId)
                
                // Reschedule for tomorrow if it's a repeating medication
                if (medId != -1L) {
                    val pendingResult = goAsync()
                    CoroutineScope(Dispatchers.IO).launch {
                        val medication = medicationDao.getMedicationById(medId)
                        if (medication != null && medication.isRepeating) {
                            alarmScheduler.scheduleMedicationAlarm(medication)
                        }
                        pendingResult.finish()
                    }
                }
            }
            "android.template.ui.ACTION_TAKEN" -> {
                if (medId != -1L) {
                    val pendingResult = goAsync()
                    CoroutineScope(Dispatchers.IO).launch {
                        medicationDao.insertLog(DoseLog(medicationId = medId, timestamp = System.currentTimeMillis()))
                        pendingResult.finish()
                    }
                }
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(medId.toInt())
            }
            "android.template.ui.ACTION_SNOOZE" -> {
                // Handle snooze action to remind the user again later
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                // Cancel the current notification
                notificationManager.cancel(medId.toInt())

                // Schedule a new alarm for 15 minutes (15 * 60 * 1000 milliseconds) from now
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val snoozeReminderIntent = Intent(context, MedicationReminderReceiver::class.java).apply {
                    action = "android.template.ui.ACTION_REMINDER"
                    putExtra("MED_NAME", medName)
                    putExtra("MED_ID", medId)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    medId.toInt(), // Use medication ID to avoid collisions
                    snoozeReminderIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )

                val snoozeTimeMillis = System.currentTimeMillis() + (15 * 60 * 1000)
                try {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, snoozeTimeMillis, pendingIntent)
                } catch (e: SecurityException) {
                    e.printStackTrace()
                }
            }
            "android.template.ui.ACTION_IGNORE" -> {
                Log.d("MedicationReminder", "Notification for $medName (ID: $medId) was ignored (swiped away).")
            }
        }
    }

    /**
     * Constructs and displays a high-priority notification to remind the user to take their medication.
     *
     * @param context The context used to access the [NotificationManager].
     * @param medName The name of the medication to display in the notification.
     */
    private fun showNotification(context: Context, medName: String, medId: Long) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the NotificationChannel
        val channel = NotificationChannel(
            "med_reminders",
            "Medication Reminders",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for medication reminder notifications"
        }
        notificationManager.createNotificationChannel(channel)

        val pendingIntent = PendingIntent.getActivity(
            context, 0, Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        // 1. Create the PendingIntent for the "Take" action
        val takeIntent = Intent(context, MedicationReminderReceiver::class.java).apply {
            action = "android.template.ui.ACTION_TAKEN"
            putExtra("MED_NAME", medName)
            putExtra("MED_ID", medId)
        }
        val takePendingIntent = PendingIntent.getBroadcast(
            context,
            medId.toInt(), // Use medication ID for unique request code
            takeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        // 2. Create the PendingIntent for the "Snooze" action
        val snoozeIntent = Intent(context, MedicationReminderReceiver::class.java).apply {
            action = "android.template.ui.ACTION_SNOOZE"
            putExtra("MED_NAME", medName)
            putExtra("MED_ID", medId)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            medId.toInt() + 1, // Differentiate from the take pending intent request code
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        // 3. Create the PendingIntent for the "Ignore" action (swiping away)
        val ignoreIntent = Intent(context, MedicationReminderReceiver::class.java).apply {
            action = "android.template.ui.ACTION_IGNORE"
            putExtra("MED_NAME", medName)
            putExtra("MED_ID", medId)
        }
        val ignorePendingIntent = PendingIntent.getBroadcast(
            context,
            medId.toInt() + 2,
            ignoreIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, "med_reminders")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Fallback icon; replace with your own drawable
            .setContentTitle("Time for Medication")
            .setContentText("It's time to take $medName.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setDeleteIntent(ignorePendingIntent) // Handles swiping away
            .setAutoCancel(true)
            .addAction(0, "Take", takePendingIntent) // Add Take button
            .addAction(0, "Snooze", snoozePendingIntent) // Add Snooze button
            .build()

        // Use the medication ID as the notification ID to avoid collisions
        notificationManager.notify(medId.toInt(), notification)
    }
}
