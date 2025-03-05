package Activities

import Adapter.ReminderAdapter
import Network.RetrofitClient
import Repositories.ReminderRepository
import ViewModel.ReminderViewModel
import ViewModel.ReminderViewModelFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import es.ua.eps.medmonitor.R
import es.ua.eps.medmonitor.databinding.ActivityReminderBinding

class ReminderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReminderBinding
    private lateinit var reminderViewModel: ReminderViewModel
    private val reminderAdapter = ReminderAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Inicializar ViewModel
        val apiService = RetrofitClient.apiService
        val reminderRepository = ReminderRepository(apiService)
        val factory = ReminderViewModelFactory(reminderRepository)
        reminderViewModel = ViewModelProvider(this, factory).get(ReminderViewModel::class.java)

        // Cargar recordatorios del usuario
        val userId = intent.getLongExtra("userId", -1L)
        if (userId != -1L) {
            reminderViewModel.loadReminders(userId)
        }

        // Observar los recordatorios y actualizar la UI
        reminderViewModel.reminders.observe(this) { reminders ->
            reminderAdapter.updateList(reminders)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewReminders.apply {
            layoutManager = LinearLayoutManager(this@ReminderActivity)
            adapter = reminderAdapter
        }
    }

    private fun observeReminders() {
        reminderViewModel.reminders.observe(this) { reminders ->
            if (reminders.isEmpty()) {
                Toast.makeText(this, "No se encontraron recordatorios", Toast.LENGTH_SHORT).show()
            } else {
                reminderAdapter.updateList(reminders)
            }
        }
    }
}