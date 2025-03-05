package Activities

import Enums.Dia
import Enums.Estado
import Models.Medication
import Models.MedicationDTO
import Models.Users
import Network.RetrofitClient
import Repositories.MedicationRepository
import ViewModel.MedicationViewModel
import ViewModel.MedicationViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButtonToggleGroup
import es.ua.eps.medmonitor.R
import es.ua.eps.medmonitor.databinding.ActivityCreateEditMedicationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateEditMedicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEditMedicationBinding
    private lateinit var viewModel: MedicationViewModel
    private lateinit var usuario: Users

    private var isEditMode = false
    private var medicationId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditMedicationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getLongExtra("userId", -1L)
        if (userId == -1L) {
            Toast.makeText(this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchUserDetails(userId)

        // Inicializar ViewModel
        val repository = MedicationRepository(RetrofitClient.apiService)
        val factory = MedicationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(MedicationViewModel::class.java)

        // Detectar si es edición o creación
        val medicationDTO = intent.getSerializableExtra("medicationDTO") as? MedicationDTO
        val medicationEntity = intent.getSerializableExtra("medication") as? Medication

        isEditMode = medicationEntity != null

        if (isEditMode && medicationEntity != null) {
            medicationId = medicationEntity.id
            populateFieldsFromMedication(medicationEntity) // Rellena los campos con `Medication`
        } else if (medicationDTO != null) {
            populateFields(medicationDTO) // Rellena los campos con `MedicationDTO`
        } else {
            medicationId = null // Caso de creación con formulario vacío
        }

        // Botón Guardar
        binding.btnGuardar.setOnClickListener {
            if (!validateFields()) return@setOnClickListener
            if (isEditMode) {
                updateMedication() // Lógica de edición
            } else {
                createMedication() // Lógica de creación
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

    private fun populateFields(medication: MedicationDTO) {
        binding.etNombre.setText(medication.nombre)
        binding.etDescripcion.setText(medication.descripcion)
        setToggleSelection(binding.toggleGroupDias, medication.dias)
        binding.etComprimidos.setText(medication.comprimidos.toString())
        binding.etVecesDia.setText(medication.veces_al_dia.toString())
        binding.etHoras.setText(medication.horas)
    }

    private fun populateFieldsFromMedication(medication: Medication) {
        binding.etNombre.setText(medication.nombre)
        binding.etDescripcion.setText(medication.descripcion)
        setSelectedDay(medication.dias) // Selecciona el día
        binding.etComprimidos.setText(medication.comprimidos.toString())
        binding.etVecesDia.setText(medication.vecesAlDia.toString())
        binding.etHoras.setText(medication.horas)
    }

    private fun setToggleSelection(toggleGroup: MaterialButtonToggleGroup, dia: Dia) {
        when (dia) {
            Dia.Lunes -> toggleGroup.check(R.id.btnLunes)
            Dia.Martes -> toggleGroup.check(R.id.btnMartes)
            Dia.Miercoles -> toggleGroup.check(R.id.btnMiercoles)
            Dia.Jueves -> toggleGroup.check(R.id.btnJueves)
            Dia.Viernes -> toggleGroup.check(R.id.btnViernes)
            Dia.Sabado -> toggleGroup.check(R.id.btnSabado)
            Dia.Domingo -> toggleGroup.check(R.id.btnDomingo)
        }
    }

    private fun setSelectedDay(dia: Dia) {
        when (dia) {
            Dia.Lunes -> binding.toggleGroupDias.check(R.id.btnLunes)
            Dia.Martes -> binding.toggleGroupDias.check(R.id.btnMartes)
            Dia.Miercoles -> binding.toggleGroupDias.check(R.id.btnMiercoles)
            Dia.Jueves -> binding.toggleGroupDias.check(R.id.btnJueves)
            Dia.Viernes -> binding.toggleGroupDias.check(R.id.btnViernes)
            Dia.Sabado -> binding.toggleGroupDias.check(R.id.btnSabado)
            Dia.Domingo -> binding.toggleGroupDias.check(R.id.btnDomingo)
        }
    }

    private fun validateFields(): Boolean {
        if (binding.etNombre.text.isNullOrBlank()) {
            binding.etNombre.error = "El nombre es obligatorio"
            return false
        }
        if (binding.etDescripcion.text.isNullOrBlank()) {
            binding.etDescripcion.error = "La descripción es obligatoria"
            return false
        }
        return true
    }

    private fun createMedication() {
        val medication = getMedicationFromFields()
        viewModel.createMedication(medication) { success ->
            showMessage(success, "Medicamento creado con éxito", "Error al crear el medicamento")
        }
    }

    private fun updateMedication() {
        val id = medicationId // Copiamos el valor a una variable local inmutable
        if (id == null) {
            Toast.makeText(this, "Error: ID de medicamento no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtenemos los campos actualizados del DTO
        val updatedFields = getMedicationFromFields()

        // Buscar el usuario completo basado en userId
        val userId = updatedFields.usuario_id
        if (userId == null) {
            Toast.makeText(this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.apiService.getUserById(userId).enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful) {
                    val usuario = response.body()
                    if (usuario == null) {
                        Toast.makeText(this@CreateEditMedicationActivity, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                        return
                    }

                    // Crear el objeto Medication con el usuario completo
                    val updatedMedication = Medication(
                        id = id, // Usamos el ID existente para la edición
                        nombre = updatedFields.nombre,
                        descripcion = updatedFields.descripcion,
                        usuario_id = usuario, // Pasamos el usuario completo aquí
                        dias = updatedFields.dias,
                        status = updatedFields.status,
                        comprimidos = updatedFields.comprimidos,
                        vecesAlDia = updatedFields.veces_al_dia,
                        horas = updatedFields.horas
                    )

                    // Actualizamos el medicamento con el ViewModel
                    viewModel.updateMedication(id, updatedMedication) { success ->
                        showMessage(success, "Medicamento actualizado con éxito", "Error al actualizar el medicamento")
                    }
                } else {
                    Toast.makeText(this@CreateEditMedicationActivity, "Error al obtener usuario: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(this@CreateEditMedicationActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun getMedicationFromFields(): MedicationDTO {
        val nombre = binding.etNombre.text.toString()
        val descripcion = binding.etDescripcion.text.toString()
        val dia = getSelectedDay() ?: throw IllegalStateException("No se seleccionó un día")
        val comprimidos = binding.etComprimidos.text.toString().toIntOrNull() ?: 0
        val vecesDia = binding.etVecesDia.text.toString().toIntOrNull() ?: 0
        val horas = binding.etHoras.text.toString()

        return MedicationDTO(
            nombre = nombre,
            descripcion = descripcion,
            usuario_id = usuario.id!!,
            dias = dia,
            status = Estado.pendiente,
            comprimidos = comprimidos,
            veces_al_dia = vecesDia,
            horas = horas
        )
    }

    private fun getSelectedDay(): Dia? {
        return when (binding.toggleGroupDias.checkedButtonId) {
            R.id.btnLunes -> Dia.Lunes
            R.id.btnMartes -> Dia.Martes
            R.id.btnMiercoles -> Dia.Miercoles
            R.id.btnJueves -> Dia.Jueves
            R.id.btnViernes -> Dia.Viernes
            R.id.btnSabado -> Dia.Sabado
            R.id.btnDomingo -> Dia.Domingo
            else -> null
        }
    }

    private fun showMessage(success: Boolean, successMsg: String, errorMsg: String) {
        Toast.makeText(this, if (success) successMsg else errorMsg, Toast.LENGTH_SHORT).show()
        if (success) finish()
    }
}
