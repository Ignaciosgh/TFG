package Models

import Enums.Frecuencia
import com.google.gson.annotations.SerializedName

data class Reminder(
    @SerializedName("id") val id: Long,
    @SerializedName("medicamento_id") val medicamentoId: Medication,
    @SerializedName("hora") val hora: String,
    @SerializedName("frecuencia") val frecuencia: Frecuencia
)
