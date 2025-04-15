package Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.ua.eps.medmonitor.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", "Nombre no disponible")
        val userEmail = sharedPreferences.getString("userEmail", "Correo no disponible")

        val nameTextView = findViewById<TextView>(R.id.textName)
        val emailTextView = findViewById<TextView>(R.id.textEmail)

        nameTextView.text = userName
        emailTextView.text = userEmail

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val logoutButton = findViewById<ImageView>(R.id.logout)
        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // Limpiar las preferencias de sesión
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Redirigir al usuario a la pantalla de inicio de sesión
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Finaliza la actividad actual
    }
}