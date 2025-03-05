package Enums

import com.google.gson.annotations.SerializedName

enum class Dia {
    @SerializedName("Lunes") Lunes,
    @SerializedName("Martes") Martes,
    @SerializedName("Miercoles") Miercoles,
    @SerializedName("Jueves") Jueves,
    @SerializedName("Viernes") Viernes,
    @SerializedName("Sabado") Sabado,
    @SerializedName("Domingo") Domingo
}
