package Models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val correo: String,
    val contraseña: String
)
