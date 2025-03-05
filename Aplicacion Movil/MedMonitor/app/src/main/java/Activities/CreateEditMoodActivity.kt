package Activities

import Enums.Estado
import Enums.EstadoAnimo
import Models.Mood
import Models.MoodDTO
import Models.Users
import Network.RetrofitClient
import Repositories.MoodRepository
import ViewModel.MoodViewModel
import ViewModel.MoodViewModelFactory
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import es.ua.eps.medmonitor.R
import es.ua.eps.medmonitor.databinding.ActivityCreateEditMoodBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateEditMoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEditMoodBinding
    private lateinit var viewModel: MoodViewModel
    private lateinit var usuario: Users

    private var isEditMode = false
    private var moodId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditMoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getLongExtra("userId", -1L)
        if (userId == -1L) {
            Toast.makeText(this, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        fetchUserDetails(userId)

        // Inicializar ViewModel
        val repository = MoodRepository(RetrofitClient.apiService)
        val factory = MoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(MoodViewModel::class.java)

        val mood = intent.getSerializableExtra("mood") as? Mood
        val moodDTO = intent.getSerializableExtra("moodDTO") as? MoodDTO

        isEditMode = mood != null

        fetchUserDetails(userId)

        if (isEditMode && mood != null) {
            moodId = mood.id
            populateFieldsFromMood(mood)
        } else if (moodDTO != null) {
            populateFields(moodDTO)
        } else {
            moodId = null // Create mode
        }

        // Botón Guardar
        binding.btnGuardar.setOnClickListener {
            if (!validateFields()) return@setOnClickListener
            if (isEditMode) {
                updateMood()
            } else {
                createMood()
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

    private fun populateFields(mood: MoodDTO) {
        moodId = mood.id
        binding.etFecha.setText(mood.fecha.toString())
        binding.etNota.setText(mood.nota)
        setSpinnerSelection(mood.estado.name)
    }

    private fun getMoodFromFields(): MoodDTO {
        val fecha = binding.etFecha.text.toString()
        val nota = binding.etNota.text.toString()
        val estado = binding.spEstado.selectedItem.toString()

        val estadoEnum = try {
            EstadoAnimo.valueOf(estado)
        } catch (e: IllegalArgumentException) {
            null
        }

        if (estadoEnum == null) {
            throw IllegalArgumentException("Estado no válido seleccionado: $estado")
        }

        return MoodDTO(
            id = moodId,
            usuario_id = usuario.id!!,
            fecha = fecha,
            estado = estadoEnum,
            nota = nota
        )
    }

    private fun validateFields(): Boolean {
        if (binding.etFecha.text.isNullOrBlank()) {
            binding.etFecha.error = "La fecha es obligatoria"
            return false
        }
        if (binding.etNota.text.isNullOrBlank()) {
            binding.etNota.error = "La nata es obligatoria"
            return false
        }
        if (binding.spEstado.selectedItem == null) {
            Toast.makeText(this, "Debes seleccionar un estado de ánimo", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showMessage(success: Boolean, successMsg: String, errorMsg: String) {
        Toast.makeText(this, if (success) successMsg else errorMsg, Toast.LENGTH_SHORT).show()
        if (success) finish()
    }


    private fun createMood() {
        val moodDTO = getMoodFromFields()
        RetrofitClient.apiService.createMood(moodDTO).enqueue(object : Callback<MoodDTO> {
            override fun onResponse(call: Call<MoodDTO>, response: Response<MoodDTO>) {
                if (response.isSuccessful) {
                    showMessage(true, "Estado de ánimo creado con éxito", "Error al crear el estado de ánimo")
                } else {
                    showMessage(false, "Estado de ánimo creado con éxito", "Error al crear el estado de ánimo")
                }
            }

            override fun onFailure(call: Call<MoodDTO>, t: Throwable) {
                showMessage(false, "Estado de ánimo creado con éxito", "Error al crear el estado de ánimo")
            }
        })
    }


    private fun updateMood() {
        val moodDTO = getMoodFromFields()
        val id = moodId ?: return
        RetrofitClient.apiService.updateMood(id, moodDTO).enqueue(object : Callback<MoodDTO> {
            override fun onResponse(call: Call<MoodDTO>, response: Response<MoodDTO>) {
                if (response.isSuccessful) {
                    showMessage(true, "Estado de ánimo actualizado con éxito", "Error al actualizar el estado de ánimo")
                } else {
                    showMessage(false, "Estado de ánimo actualizado con éxito", "Error al actualizar el estado de ánimo")
                }
            }

            override fun onFailure(call: Call<MoodDTO>, t: Throwable) {
                showMessage(false, "Estado de ánimo actualizado con éxito", "Error al actualizar el estado de ánimo")
            }
        })
    }

    private fun populateFieldsFromMood(mood: Mood) {
        moodId = mood.id
        binding.etFecha.setText(mood.fecha.toString())
        binding.etNota.setText(mood.nota)
        setSpinnerSelection(mood.estado.name)
    }

    private fun setSpinnerSelection(estado: String) {
        val adapter = binding.spEstado.adapter as ArrayAdapter<String>
        val position = adapter.getPosition(estado)
        if (position != -1) {
            binding.spEstado.setSelection(position)
        }
    }
}