package Enums

import com.google.gson.annotations.SerializedName

enum class EstadoAnimo {
    @SerializedName("Feliz") Feliz,
    @SerializedName("Triste") Triste,
    @SerializedName("Ansioso") Ansioso,
    @SerializedName("Neutral") Neutral
}