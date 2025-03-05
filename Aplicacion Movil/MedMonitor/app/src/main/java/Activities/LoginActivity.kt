package Activities

import Network.ApiServices
import Repositories.UserRepository
import ViewModel.UserViewModel
import ViewModel.UserViewModelFactory
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import es.ua.eps.medmonitor.databinding.ActivityLoginBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        //sharedPreferences.edit().clear().apply()
        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
        val userId = sharedPreferences.getLong("userId", -1L)

        Log.d("LoginActivity", "Estado de sesion $isLoggedIn")

        if (isLoggedIn && userId != -1L) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://192.168.0.23:8089")
            .addConverterFactory(GsonConverterFactory.create())
            .callbackExecutor(java.util.concurrent.Executors.newSingleThreadExecutor())
            .build()

        val apiService = retrofit.create(ApiServices::class.java)
        val userRepository = UserRepository(apiService)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = UserViewModelFactory(userRepository, application)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            val correo = binding.etEmail.text.toString();
            val contraseña = binding.etPassword.text.toString()

            if (correo.isNotBlank() && contraseña.isNotBlank()) {
                userViewModel.login(correo, contraseña)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        userViewModel.loginResult.observe(this, Observer { result ->
            if (result.isSuccess) {
                val userId = userViewModel.getUserId() ?: -1L
                saveUserSession(userId)

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Error: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
            }
        })

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun saveUserSession(userId: Long) {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putBoolean("is_logged_in", true)
            .putLong("userId", userId)
            .apply()
    }

    private fun clearUserSession() {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear() // Elimina todos los datos
            apply()
        }
        // Redirige al inicio de sesión
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun authenticateWithBiometrics() {
        val executor = ContextCompat.getMainExecutor(this) // `this` es la instancia de la actividad
        val biometricPrompt = androidx.biometric.BiometricPrompt(
            this, // LoginActivity extiende de FragmentActivity
            executor,
            object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Acciones tras autenticación exitosa
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Autenticación fallida", Toast.LENGTH_SHORT).show()
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Inicio de Sesión con Huella")
            .setSubtitle("Use su huella para autenticar")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

}

