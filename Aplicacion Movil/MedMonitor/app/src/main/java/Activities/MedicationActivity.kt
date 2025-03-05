package Activities

import Adapter.MedicationsAdapter
import Enums.Estado
import Models.Medication
import Network.RetrofitClient
import Repositories.MedicationRepository
import ViewModel.MedicationViewModel
import ViewModel.MedicationViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.ua.eps.medmonitor.R
import es.ua.eps.medmonitor.databinding.ActivityMedicationBinding

class MedicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicationBinding
    private lateinit var medicationViewModel: MedicationViewModel
    private lateinit var medicationAdapter: MedicationsAdapter
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("userId", -1L)
        if (userId == -1L) {
            Toast.makeText(this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Configurar el adaptador con manejo del botón "Editar"
        medicationAdapter = MedicationsAdapter(
            medications = emptyList(),
            onEditClick = { medication ->
                // Acción al pulsar "Editar"
                val intent = Intent(this, CreateEditMedicationActivity::class.java).apply {
                    putExtra("medication", medication) // Pasar el medicamento completo
                    putExtra("userId", userId)         // Pasar también el userId
                }
                startActivity(intent)
            },
            onDeleteClick = { medication ->
                showDeleteConfirmationDialog(medication)
            },
            onUpdateStatusClick = {medication, newStatus ->
                updateMedicationStatus(medication, newStatus)
            }
        )

        // Configurar RecyclerView
        binding.view.apply {
            layoutManager = LinearLayoutManager(this@MedicationActivity)
            adapter = medicationAdapter
        }

        // Inicializar ViewModel
        val apiServices = RetrofitClient.apiService
        val medicationRepository = MedicationRepository(apiServices)
        medicationViewModel = ViewModelProvider(
            this, MedicationViewModelFactory(medicationRepository)
        ).get(MedicationViewModel::class.java)

        // Cargar medicamentos
        loadMedications()

        // Botón para agregar un nuevo medicamento
        val addMedication: FloatingActionButton = findViewById(R.id.addMoodButton)
        addMedication.setOnClickListener {
            val intent = Intent(this, CreateEditMedicationActivity::class.java).apply {
                putExtra("userId", userId)
            }
            startActivity(intent)
        }
    }

    private fun loadMedications() {
        medicationViewModel.getMedicationsByUser(userId)
        medicationViewModel.medications.observe(this) { medications ->
            if (medications.isNotEmpty()) {
                medicationAdapter.updateList(medications)
            } else {
                Toast.makeText(this, "No se encontraron medicamentos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleMedicationClick(medication: Medication) {
        // Obtén el ID del medicamento seleccionado
        val medicationId = medication.id
        showStatusUpdateDialog(medicationId)
    }

    private fun showStatusUpdateDialog(medicationId: Long) {
        val options = arrayOf("Tomado", "Omitido")

        AlertDialog.Builder(this)
            .setTitle("Actualizar Estado")
            .setItems(options) { _, which ->
                val status = if (which == 0) "TOMADO" else "OMITIDO"
                updateMedicationStatus(medicationId, status)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateMedicationStatus(medicationId: Long, status: String) {
        medicationViewModel.updateMedicationStatus(medicationId, status) { success ->
            if (success) {
                Toast.makeText(this, "Estado actualizado a $status", Toast.LENGTH_SHORT).show()
                // Reload medications and update the adapter

            } else {
                Toast.makeText(this, "Error al actualizar el estado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Recargar la lista al volver de la actividad de edición
        val userId = intent.getLongExtra("userId", -1L)
        if (userId != -1L) {
            medicationViewModel.getMedicationsByUser(userId)
        }
    }

    private fun showDeleteConfirmationDialog(medication: Medication) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Medicamento")
            .setMessage("¿Estás seguro de que deseas eliminar ${medication.nombre}?")
            .setPositiveButton("Sí") { _, _ ->
                deleteMedication(medication)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteMedication(medication: Medication) {
        medicationViewModel.deleteMedication(medication.id!!) { success ->
            if (success) {
                Toast.makeText(this, "${medication.nombre} eliminado con éxito", Toast.LENGTH_SHORT).show()
                medicationViewModel.loadMedications(userId) // Recargar lista de medicamentos
            } else {
                Toast.makeText(this, "Error al eliminar ${medication.nombre}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateMedicationStatus(medication: Medication, newStatus: Estado) {
        medicationViewModel.updateMedicationStatus(medication.id!!, newStatus.name) { success ->
            if (success) {
                Toast.makeText(this, "${medication.nombre} actualizado a ${newStatus.name.lowercase()}", Toast.LENGTH_SHORT).show()
                // Recargar la lista
                val userId = intent.getLongExtra("userId", -1L)
                if (userId != -1L) {
                    medicationViewModel.getMedicationsByUser(userId)
                }
            } else {
                Toast.makeText(this, "Error al actualizar ${medication.nombre}", Toast.LENGTH_SHORT).show()
            }
        }
    }


}