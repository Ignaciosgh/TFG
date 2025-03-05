package Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Users(
    @SerializedName("id") val id: Long?,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("contraseña") val contraseña: String
):Serializable
