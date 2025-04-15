package Activities

import Models.Reminder
import Network.RetrofitClient
import Notificacion.ReminderWorker
import Repositories.ReminderRepository
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.gson.Gson
import es.ua.eps.medmonitor.R
import es.ua.eps.medmonitor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getLongExtra("userId", -1L)
        val saludoTextView = findViewById<TextView>(R.id.textView)
        val nombreUsuario = intent.getStringExtra("userName") ?: "Usuario"
        saludoTextView.text = "Hola, $nombreUsuario"

        createNotificationChannel(this)

        if (userId != -1L) {
            Toast.makeText(this, "Usuario ID: $userId", Toast.LENGTH_SHORT).show()
            val saludoTextView = findViewById<TextView>(R.id.textView)
            val nombreUsuario = intent.getStringExtra("userName") ?: "Usuario"
            saludoTextView.text = "Hola, $nombreUsuario"
        } else {
            Toast.makeText(this, "ID no disponible", Toast.LENGTH_SHORT).show()
        }

        scheduleReminderWorker(this)

        val myTextView: TextView = findViewById(R.id.verMas)
        myTextView.setOnClickListener {
            val intent = Intent(this, CoursesListActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        val myImageView: ImageView = findViewById(R.id.imageViewHouse)
        myImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        val myImageViewAjustes: ImageView = findViewById(R.id.imageViewAjustes)
        myImageViewAjustes.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        val myImageViewPerfil: ImageView = findViewById(R.id.imageViewPerfil)
        myImageViewPerfil.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        val myImageViewPerfil2: ImageView = findViewById(R.id.imageView2)
        myImageViewPerfil2.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        val myConstraintsMedicamentos: ConstraintLayout = findViewById(R.id.cajaCrearMedicamentos)
        myConstraintsMedicamentos.setOnClickListener {
            val intent = Intent(this, CreateEditMedicationActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        val myImageViewMedic: ImageView = findViewById(R.id.imageViewMedicamentosLight)
        myImageViewMedic.setOnClickListener {
            val intent = Intent(this, MedicationActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        val myImageViewConst: ImageView = findViewById(R.id.ConsultasMedicasLight)
        myImageViewConst.setOnClickListener {
            val intent = Intent(this, AppointmentActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        val myImageViewFisic: ImageView = findViewById(R.id.imageViewActividadFisicaLight)
        myImageViewFisic.setOnClickListener {
            val intent = Intent(this, PhysicalActivityActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        val myImageViewAnimo: ImageView = findViewById(R.id.imageViewEstadoAnimoLight)
        myImageViewAnimo.setOnClickListener {
            val intent = Intent(this, MoodActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
    }

    private fun downloadReminders(userId: Long) {
        val apiService = RetrofitClient.apiService
        val repository = ReminderRepository(apiService)

        repository.getRemindersByUser(userId) { reminders ->
            if (reminders != null && reminders.isNotEmpty()) {
                saveRemindersToLocal(reminders)
                Toast.makeText(this, "Recordatorios actualizados", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay recordatorios disponibles", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveRemindersToLocal(reminders: List<Reminder>) {
        val sharedPreferences = getSharedPreferences("RemindersPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val remindersJson = gson.toJson(reminders)
        editor.putString("reminders", remindersJson)
        editor.apply()
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "reminder_notifications"
            val channelName = "Recordatorios"
            val channelDescription = "Notificaciones para recordatorios"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleReminderWorker(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(15, java.util.concurrent.TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "ReminderWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}