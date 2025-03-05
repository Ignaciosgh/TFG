package Models

import Enums.Dia
import Enums.Estado
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Medication(
    @SerializedName("id") val id: Long,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("usuario_id") val usuario_id: Users,
    @SerializedName("dias") val dias: Dia,
    @SerializedName("comprimidos") val comprimidos: Int,
    @SerializedName("veces_al_dia") val vecesAlDia: Int,
    @SerializedName("horas") val horas: String, // Almacena las horas en formato JSON o CSV
    @SerializedName("status") val status: Estado // Enum para el estado
): Serializable
