package Models

import Enums.EstadoAnimo
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate

data class Mood(
    @SerializedName("id") val id: Long,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("fecha") val fecha: LocalDate,
    @SerializedName("estado") val estado: EstadoAnimo,
    @SerializedName("nota") val nota: String
):Serializable
