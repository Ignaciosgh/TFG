package Models

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class ActivityLog(
    @SerializedName("id") val id: Long,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("accion") val accion: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fecha") val fecha: LocalDate
)
