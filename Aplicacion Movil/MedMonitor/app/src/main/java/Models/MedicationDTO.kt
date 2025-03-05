package Models

import Enums.Dia
import Enums.Estado
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MedicationDTO(
    @SerializedName("usuario_id") val usuario_id: Long,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("dias") val dias: Dia,
    @SerializedName("status") val status: Estado,
    @SerializedName("comprimidos") val comprimidos: Int,
    @SerializedName("veces_al_dia") val veces_al_dia: Int,
    @SerializedName("horas") val horas: String,
) : Serializable
