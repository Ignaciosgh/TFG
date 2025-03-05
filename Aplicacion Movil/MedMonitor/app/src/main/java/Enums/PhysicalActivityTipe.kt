package Enums

import com.google.gson.annotations.SerializedName

enum class PhysicalActivityTipe {
    @SerializedName("Caminar") Caminar,
    @SerializedName("Correr") Correr,
    @SerializedName("Nadar") Nadar,
    @SerializedName("Ciclismo") Ciclismo,
    @SerializedName("Otro") Otro

}