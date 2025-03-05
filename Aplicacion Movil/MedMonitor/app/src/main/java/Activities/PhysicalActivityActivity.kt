package Activities

import Adapter.MedicationsAdapter
import Adapter.PhysicalActivityAdapter
import Models.PhysicalActivity
import Network.RetrofitClient
import Repositories.PhysicalActivityRepository
import ViewModel.MedicationViewModel
import ViewModel.PhysicalActivityViewModel
import ViewModel.PhysicalActivityViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import es.ua.eps.medmonitor.R
import es.ua.eps.medmonitor.databinding.ActivityMedicationBinding
import es.ua.eps.medmonitor.databinding.ActivityPhysicalActivityBinding

class PhysicalActivityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhysicalActivityBinding
    private lateinit var physicalActivityViewModel: PhysicalActivityViewModel
    private lateinit var physicalActivityAdapter: PhysicalActivityAdapter
    private var userId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhysicalActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra("userId", -1L)
        if (userId == -1L) {
            Toast.makeText(this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        physicalActivityAdapter = PhysicalActivityAdapter(
            activities = emptyList(),
            onEditClick = { activity ->
                val intent = Intent(this, CreateEditPhysicalActivityActivity::class.java).apply {
                    putExtra("physicalActivity", activity)
                    putExtra("userId", userId)
                }
                startActivity(intent)
            },
            onDeleteClick = { activity ->
                showDeleteConfirmationDialog(activity)
            }
        )

        // Configurar RecyclerView
        binding.recyclerViewActivities.apply {
            layoutManager = LinearLayoutManager(this@PhysicalActivityActivity)
            adapter = physicalActivityAdapter
        }

        val repository = PhysicalActivityRepository(RetrofitClient.apiService)
        val factory = PhysicalActivityViewModelFactory(repository)
        physicalActivityViewModel = ViewModelProvider(this, factory).get(PhysicalActivityViewModel::class.java)

        loadActivities()


        val addActivitie: FloatingActionButton = findViewById(R.id.addActivityButton)
        addActivitie.setOnClickListener {
            val intent = Intent(this, CreateEditPhysicalActivityActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
    }

    private fun loadActivities() {
        physicalActivityViewModel.getActivitiesByUser(userId)
        physicalActivityViewModel.activities.observe(this) { activities ->
            if (activities.isNotEmpty()) {
                physicalActivityAdapter.updateList(activities)
            } else {
                Toast.makeText(this, "No se encontraron actividades físicas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog(activity: PhysicalActivity) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Actividad Física")
            .setMessage("¿Estás seguro de que deseas eliminar la actividad del ${activity.fecha}?")
            .setPositiveButton("Sí") { _, _ -> deleteActivity(activity) }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteActivity(activity: PhysicalActivity) {
        physicalActivityViewModel.deleteActivity(activity.id!!) { success ->
            if (success) {
                Toast.makeText(this, "Actividad eliminada con éxito", Toast.LENGTH_SHORT).show()
                loadActivities()
            } else {
                Toast.makeText(this, "Error al eliminar la actividad", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadActivities()
    }
}