package Enums

import com.google.gson.annotations.SerializedName

enum class Estado {
    @SerializedName("pendiente") pendiente,
    @SerializedName("tomado") tomado,
    @SerializedName("omitido") omitido
}