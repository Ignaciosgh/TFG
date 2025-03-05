package Models

import com.google.gson.annotations.SerializedName

data class UserSetting(
    @SerializedName("id") val id: Long,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("notificaciones") val notificaciones: Boolean,
    @SerializedName("sonido") val sonido: String
)
