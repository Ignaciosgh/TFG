package Notificacion

import Models.Reminder
import Network.RetrofitClient
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import es.ua.eps.medmonitor.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val reminders = getRemindersFromLocal()

        val now = LocalTime.now()
        val today = LocalDate.now().dayOfWeek.toString()

        reminders.forEach { reminder ->
            val dias = reminder.medicamentoId.dias.toString()
            if (dias.contains(today, ignoreCase = true)) {
                val reminderTime = LocalTime.parse(reminder.hora, DateTimeFormatter.ofPattern("HH:mm:ss"))
                if (now.isAfter(reminderTime.minusMinutes(1)) && now.isBefore(reminderTime.plusMinutes(1))) {
                    showNotification(
                        "Medicamento: ${reminder.medicamentoId.nombre}",
                        "Hora: ${reminder.hora}, Frecuencia: ${reminder.frecuencia}"
                    )
                }
            }
        }

        return Result.success()
    }

    private fun getRemindersFromLocal(): List<Reminder> {
        val sharedPreferences = context.getSharedPreferences("RemindersPrefs", Context.MODE_PRIVATE)
        val remindersJson = sharedPreferences.getString("reminders", null) ?: return emptyList()
        val gson = Gson()
        val type = object : TypeToken<List<Reminder>>() {}.type
        return gson.fromJson(remindersJson, type)
    }

    private fun showNotification(title: String, details: String) {
        val channelId = "reminder_notifications"
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification) // Cambia por tu ícono
            .setContentTitle(title)
            .setContentText(details)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }
}
