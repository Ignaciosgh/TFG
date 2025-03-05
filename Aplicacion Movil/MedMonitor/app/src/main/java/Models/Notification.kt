package Models

import Enums.NotificationTipe
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Notification(
    @SerializedName("id") val id: Long,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("tipo") val tipo: NotificationTipe,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("fecha") val fecha: LocalDate
)
