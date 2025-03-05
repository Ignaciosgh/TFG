package Activities

import Models.Users
import Network.ApiServices
import Network.RetrofitClient
import Repositories.UserRepository
import ViewModel.UserViewModel
import ViewModel.UserViewModelFactory
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import es.ua.eps.medmonitor.R
import es.ua.eps.medmonitor.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:8089") // Reemplaza con la URL base de tu API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiServices::class.java)
        val userRepository = UserRepository(apiService)


        // Inicializamos el ViewModel pasando el application
        val factory = UserViewModelFactory(userRepository, application)  // Pasamos el application aquí
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        binding.registerButton.setOnClickListener {
            if (validateFields()) {
                registerUser()
            }
        }

        userViewModel.registerResult.observe(this, Observer { result ->
            if (result.isSuccess) {
                // Navegar al login
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                // Mostrar error
                Toast.makeText(this, "Error de registro", Toast.LENGTH_SHORT).show()
            }
        })

        binding.redirectToLogin.setOnClickListener {
            // Navegar al login
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateFields(): Boolean {
        if (binding.nameRegister.text.isNullOrBlank()) {
            binding.nameRegister.error = "El nombre es obligatorio"
            return false
        }
        if (binding.emailRegister.text.isNullOrBlank()) {
            binding.emailRegister.error = "El correo es obligatorio"
            return false
        }
        if (binding.passwordRegister.text.isNullOrBlank()) {
            binding.passwordRegister.error = "La contraseña es obligatoria"
            return false
        }
        return true
    }

    private fun registerUser() {
        val user = Users(
            id = null, // Backend will auto-generate ID
            nombre = binding.nameRegister.text.toString(),
            correo = binding.emailRegister.text.toString(),
            contraseña = binding.passwordRegister.text.toString()
        )

        // Make API call
        RetrofitClient.apiService.registerUser(user).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, response.body(), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Close the activity after successful registration
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        response.errorBody()?.string() ?: "Error desconocido",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(
                    this@RegisterActivity,
                    "Usuario Creado con Exito",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}