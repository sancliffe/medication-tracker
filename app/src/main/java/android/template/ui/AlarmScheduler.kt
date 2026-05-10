package android.template.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Helper class responsible for scheduling exact alarms using [AlarmManager].
 * These alarms trigger [MedicationReminderReceiver] to show notifications
 * when it is time to take a specific medication.
 *
 * @property context The application context used to access system services.
 */
class AlarmScheduler @Inject constructor(@ApplicationContext private val context: Context) {

    /**
     * Schedules an exact alarm for the given medication based on its time string.
     * If the time has already passed for the current day, it schedules it for the next day.
     *
     * @param medication The medication to schedule an alarm for.
     */
    fun scheduleMedicationAlarm(medication: Medication) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // The intent will wake up MedicationReminderReceiver
        val intent = Intent(context, MedicationReminderReceiver::class.java).apply {
            action = "android.template.ui.ACTION_REMINDER"
            putExtra("MED_NAME", medication.name)
            putExtra("MED_ID", medication.id)
        }

        // FLAG_UPDATE_CURRENT ensures we don't create duplicate pending intents for the same ID
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medication.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Parse the "HH:mm" time from the Medication entity
        val timeParts = medication.scheduledTime.split(":")
        if (timeParts.size != 2) return

        val hour = timeParts[0].toIntOrNull() ?: return
        val minute = timeParts[1].toIntOrNull() ?: return

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // If the scheduled time has already passed today, set it for tomorrow instead
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                // Fallback to non-exact alarm if permission is missing
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                // Set an exact alarm that triggers even if the device is in Doze mode
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // Last resort fallback
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    /**
     * Cancels the scheduled alarm for the given medication.
     *
     * @param medication The medication to cancel the alarm for.
     */
    fun cancelMedicationAlarm(medication: Medication) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, MedicationReminderReceiver::class.java).apply {
            action = "android.template.ui.ACTION_REMINDER"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            medication.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }
}