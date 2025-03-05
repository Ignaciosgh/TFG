package Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

data class Appointment(
    @SerializedName("id") val id: Long,
    @SerializedName("usuario_id") val usuario_id: Long,
    @SerializedName("fecha") val fecha : LocalDate,
    @SerializedName("hora") val hora: LocalTime,
    @SerializedName("descripcion") val descripcion: String
): Serializable
