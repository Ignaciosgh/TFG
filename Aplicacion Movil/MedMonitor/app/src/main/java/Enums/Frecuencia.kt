package Enums

import com.google.gson.annotations.SerializedName

enum class Frecuencia {
    @SerializedName("Diario") DIARIO,
    @SerializedName("Semanal") SEMANAL,
    @SerializedName("Mensual") MENSUAL
}