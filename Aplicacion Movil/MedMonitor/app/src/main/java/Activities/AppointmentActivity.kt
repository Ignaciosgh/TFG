package Activities

import Adapter.ConsultationsAdapter
import Models.Appointment
import Network.RetrofitClient
import Repositories.AppintmentRepository
import ViewModel.ConsultationViewModel
import ViewModel.ConsultationViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.ua.eps.medmonitor.R
import es.ua.eps.medmonitor.databinding.ActivityAppointmentBinding

class AppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppointmentBinding
    private lateinit var consultationViewModel: ConsultationViewModel
    private lateinit var consultationAdapter: ConsultationsAdapter
    private var userId: Long = -1L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("userId", -1L)
        if (userId == -1L) {
            Toast.makeText(this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        consultationAdapter = ConsultationsAdapter(
            consultations = emptyList(),
            onEditClick = { appointment ->
                val intent = Intent(this, CreateEditAppointmentActivity::class.java).apply {
                    putExtra("appointment", appointment) // Implementar Serializable
                    putExtra("userId", userId)
                }
                startActivity(intent)
            },
            onDeleteClick = { appointment ->
                showDeleteConfirmationDialog(appointment)
            }
        )

        // Configurar RecyclerView
        binding.view.apply {
            layoutManager = LinearLayoutManager(this@AppointmentActivity)
            adapter = consultationAdapter
        }

        // Inicializar ViewModel
        val repository = AppintmentRepository(RetrofitClient.apiService)
        consultationViewModel = ViewModelProvider(this, ConsultationViewModelFactory(repository))
            .get(ConsultationViewModel::class.java)

        loadAppointments();

        val addConsultaMedica: FloatingActionButton = findViewById(R.id.addConsultaButton)
        addConsultaMedica.setOnClickListener {
            val intent = Intent(this, CreateEditAppointmentActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
    }

    private fun loadAppointments() {
        consultationViewModel.getConsultationsByUser(userId)
        consultationViewModel.consultations.observe(this) { appointments ->
            if (appointments.isNotEmpty()) {
                consultationAdapter.updateList(appointments)
            } else {
                Toast.makeText(this, "No se encontraron consultas médicas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(appointment: Appointment) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Consulta Médica")
            .setMessage("¿Estás seguro de que deseas eliminar esta consulta médica?")
            .setPositiveButton("Sí") { _, _ ->
                consultationViewModel.deleteAppointment(appointment.id!!) { success ->
                    if (success) {
                        Toast.makeText(this, "Consulta eliminada con éxito", Toast.LENGTH_SHORT).show()
                        loadAppointments()
                    } else {
                        Toast.makeText(this, "Error al eliminar la consulta", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No", null)
            .show()
    }


    override fun onResume() {
        super.onResume()
        loadAppointments() // Recargar la lista al volver de la actividad de edición
    }
}