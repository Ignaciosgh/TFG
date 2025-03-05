package Activities

import Enums.PhysicalActivityTipe
import Models.PhysicalActivity
import Models.PhysicalActivityDTO
import Models.Users
import Network.RetrofitClient
import Repositories.PhysicalActivityRepository
import ViewModel.PhysicalActivityViewModel
import ViewModel.PhysicalActivityViewModelFactory
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import es.ua.eps.medmonitor.R
import es.ua.eps.medmonitor.databinding.ActivityCreateEditPhysicalActivityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class CreateEditPhysicalActivityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEditPhysicalActivityBinding
    private lateinit var viewModel: PhysicalActivityViewModel
    private lateinit var usuario: Users

    private var isEditMode = false
    private var activityId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditPhysicalActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getLongExtra("userId", -1L)
        if (userId == -1L) {
            Toast.makeText(this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchUserDetails(userId)

        // Inicializar ViewModel
        val repository = PhysicalActivityRepository(RetrofitClient.apiService)
        val factory = PhysicalActivityViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PhysicalActivityViewModel::class.java)

        // Detectar si es edición o creación
        val physicalActivityDTO = intent.getSerializableExtra("physicalActivityDTO") as? PhysicalActivityDTO
        val physicalActivityEntity = intent.getSerializableExtra("physicalActivity") as? PhysicalActivity

        isEditMode = physicalActivityEntity != null

        if (isEditMode && physicalActivityEntity != null) {
            activityId = physicalActivityEntity.id
            populateFieldsFromPhysicalActivity(physicalActivityEntity)
        } else if (physicalActivityDTO != null) {
            populateFields(physicalActivityDTO)
        } else {
            activityId = null // Caso de creación con formulario vacío
        }

        // Botón Guardar
        binding.btnGuardar.setOnClickListener {
            if (!validateFields()) return@setOnClickListener
            if (isEditMode) {
                updateActivity()
            } else {
                createActivity()
            }
        }

        // Botón Cancelar
        binding.btnCancelar.setOnClickListener {
            finish()
        }

    }

    private fun fetchUserDetails(userId: Long) {
        RetrofitClient.apiService.getUserById(userId).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    usuario = response.body() ?: run {
                        showErrorAndFinish("Usuario no encontrado")
                        return
                    }
                } else {
                    showErrorAndFinish("Error al obtener usuario: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                showErrorAndFinish("Error de conexión: ${t.message}")
            }
        })
    }

    private fun showErrorAndFinish(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun populateFields(activity: PhysicalActivityDTO) {
        activityId = activity.id
        binding.etFecha.setText(activity.fecha.toString())
        binding.etDuracion.setText(activity.duracion.toString())
        setSpinnerSelection(activity.tipo.name)
    }

    private fun getActivityFromFields(): PhysicalActivityDTO {
        val fecha = binding.etFecha.text.toString()
        val duracion = binding.etDuracion.text.toString().toIntOrNull() ?: 0
        val tipo = binding.spTipoActividad.selectedItem.toString()

        val tipoEnum = try {
            PhysicalActivityTipe.valueOf(tipo)
        } catch (e: IllegalArgumentException) {
            null
        }

        if (tipoEnum == null) {
            throw IllegalArgumentException("Tipo de actividad no válido seleccionado: $tipo")
        }

        return PhysicalActivityDTO(
            id = activityId,
            usuario_id = usuario.id!!,
            fecha = LocalDate.parse(fecha).toString(),
            tipo = tipoEnum,
            duracion = duracion
        )
    }

    private fun validateFields(): Boolean {
        if (binding.etFecha.text.isNullOrBlank()) {
            binding.etFecha.error = "La fecha es obligatoria"
            return false
        }
        if (binding.etDuracion.text.isNullOrBlank()) {
            binding.etDuracion.error = "La duración es obligatoria"
            return false
        }
        return true
    }

    private fun createActivity() {
        val activityDTO = getActivityFromFields()
        RetrofitClient.apiService.createPhysicalActivity(activityDTO).enqueue(object : Callback<PhysicalActivityDTO> {
            override fun onResponse(call: Call<PhysicalActivityDTO>, response: Response<PhysicalActivityDTO>) {
                if (response.isSuccessful) {
                    showMessage(true, "Actividad creada con éxito", "Error al crear la actividad")
                } else {
                    showMessage(false, "Actividad creada con éxito", "Error al crear la actividad")
                }
            }

            override fun onFailure(call: Call<PhysicalActivityDTO>, t: Throwable) {
                showMessage(false, "Actividad creada con éxito", "Error al crear la actividad")
            }
        })
    }

    private fun updateActivity() {
        val activityDTO = getActivityFromFields()
        val id = activityId ?: return
        RetrofitClient.apiService.updatePhysicalActivity(id, activityDTO).enqueue(object : Callback<PhysicalActivity> {
            override fun onResponse(call: Call<PhysicalActivity>, response: Response<PhysicalActivity>) {
                if (response.isSuccessful) {
                    showMessage(true, "Actividad actualizada con éxito", "Error al actualizar la actividad")
                } else {
                    showMessage(false, "Actividad actualizada con éxito", "Error al actualizar la actividad")
                }
            }

            override fun onFailure(call: Call<PhysicalActivity>, t: Throwable) {
                showMessage(false, "Actividad actualizada con éxito", "Error al actualizar la actividad")
            }
        })
    }

    private fun showMessage(success: Boolean, successMsg: String, errorMsg: String) {
        Toast.makeText(this, if (success) successMsg else errorMsg, Toast.LENGTH_SHORT).show()
        if (success) finish()
    }

    private fun populateFieldsFromPhysicalActivity(activity: PhysicalActivity) {
        activityId = activity.id
        binding.etFecha.setText(activity.fecha.toString())
        binding.etDuracion.setText(activity.duracion.toString())
        setSpinnerSelection(activity.tipo.name)
    }

    private fun setSpinnerSelection(tipo: String) {
        val adapter = binding.spTipoActividad.adapter as ArrayAdapter<String>
        val position = adapter.getPosition(tipo)
        if (position != -1) {
            binding.spTipoActividad.setSelection(position)
        }
    }

}