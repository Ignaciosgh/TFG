package Activities

import Models.Appointment
import Models.AppointmentDTO
import Models.Users
import Network.RetrofitClient
import Repositories.AppintmentRepository
import ViewModel.ConsultationViewModel
import ViewModel.ConsultationViewModelFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import es.ua.eps.medmonitor.R
import es.ua.eps.medmonitor.databinding.ActivityCreateEditAppointmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime

class CreateEditAppointmentActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCreateEditAppointmentBinding
    private lateinit var viewModel: ConsultationViewModel
    private lateinit var usuario: Users

    private var isEditMode = false
    private var appointmentId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getLongExtra("userId", -1L)
        if (userId == -1L) {
            Toast.makeText(this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchUserDetails(userId)

        // Inicializar ViewModel
        val repository = AppintmentRepository(RetrofitClient.apiService)
        val factory = ConsultationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ConsultationViewModel::class.java)

        // Detectar si es edición o creación
        val appointmentDTO = intent.getSerializableExtra("appointmentDTO") as? AppointmentDTO
        val appointmentEntity = intent.getSerializableExtra("appointment") as? Appointment

        isEditMode = appointmentEntity != null

        if (isEditMode && appointmentEntity != null) {
            appointmentId = appointmentEntity.id
            populateFieldsFromAppointment(appointmentEntity)
        } else if (appointmentDTO != null) {
            populateFields(appointmentDTO)
        } else {
            appointmentId = null // Caso de creación con formulario vacío
        }

        // Botón Guardar
        binding.btnGuardar.setOnClickListener {
            if (!validateFields()) return@setOnClickListener
            if (isEditMode) {
                updateAppointment()
            } else {
                createAppointment()
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

    private fun populateFields(appointment: AppointmentDTO) {
        binding.etFecha.setText(appointment.fecha)
        binding.etHora.setText(appointment.hora)
        binding.etDescripcion.setText(appointment.descripcion)
    }

    private fun populateFieldsFromAppointment(appointment: Appointment) {
        binding.etFecha.setText(appointment.fecha.toString())
        binding.etHora.setText(appointment.hora.toString())
        binding.etDescripcion.setText(appointment.descripcion)
    }

    private fun validateFields(): Boolean {
        if (binding.etFecha.text.isNullOrBlank()) {
            binding.etFecha.error = "La fecha es obligatoria"
            return false
        }
        if (binding.etHora.text.isNullOrBlank()) {
            binding.etHora.error = "La hora es obligatoria"
            return false
        }
        if (binding.etDescripcion.text.isNullOrBlank()) {
            binding.etDescripcion.error = "La descripción es obligatoria"
            return false
        }
        return true
    }

    private fun createAppointment() {
        val appointment = getAppointmentFromFields()
        viewModel.createAppointment(appointment) { success ->
            showMessage(success, "Consulta médica creada con éxito", "Error al crear la consulta médica")
        }
    }

    private fun updateAppointment() {
        val id = appointmentId ?: return showErrorAndFinish("ID de consulta no encontrado")
        val updatedFields = getAppointmentFromFields()

        // Recuperar el usuario completo
        RetrofitClient.apiService.getUserById(updatedFields.usuario_id).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val usuarioCompleto = response.body() ?: return showErrorAndFinish("Usuario no encontrado")

                    // Convertir cadenas a LocalDate y LocalTime
                    val fecha = try {
                        LocalDate.parse(updatedFields.fecha)
                    } catch (e: Exception) {
                        return showErrorAndFinish("Fecha inválida: ${updatedFields.fecha}")
                    }

                    val hora = try {
                        LocalTime.parse(updatedFields.hora)
                    } catch (e: Exception) {
                        return showErrorAndFinish("Hora inválida: ${updatedFields.hora}")
                    }

                    // Crear objeto completo
                    val updatedAppointment = Appointment(
                        id = id,
                        usuario_id = usuarioCompleto.id!!,
                        fecha = fecha,
                        hora = hora,
                        descripcion = updatedFields.descripcion
                    )

                    // Actualizar en la base de datos
                    viewModel.updateAppointment(id, updatedAppointment) { success ->
                        showMessage(success, "Consulta actualizada con éxito", "Error al actualizar la consulta")
                    }
                } else {
                    showErrorAndFinish("Error al obtener usuario")
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                showErrorAndFinish("Error de conexión: ${t.message}")
            }
        })
    }

    private fun getAppointmentFromFields(): AppointmentDTO {
        val fecha = binding.etFecha.text.toString()
        val hora = binding.etHora.text.toString()
        val descripcion = binding.etDescripcion.text.toString()

        return AppointmentDTO(
            id = appointmentId,
            usuario_id = usuario.id!!,
            fecha = fecha,
            hora = hora,
            descripcion = descripcion
        )
    }


    private fun showMessage(success: Boolean, successMsg: String, errorMsg: String) {
        Toast.makeText(this, if (success) successMsg else errorMsg, Toast.LENGTH_SHORT).show()
        if (success) finish()
    }
}