package Models

import Enums.PhysicalActivityTipe
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate

data class PhysicalActivity(
    @SerializedName("id") val id: Long,
    @SerializedName("usuario_id") val usuarioId: Int,
    @SerializedName("fecha") val fecha: LocalDate,
    @SerializedName("tipo") val tipo: PhysicalActivityTipe,
    @SerializedName("duracion") val duracion: Int
):Serializable
