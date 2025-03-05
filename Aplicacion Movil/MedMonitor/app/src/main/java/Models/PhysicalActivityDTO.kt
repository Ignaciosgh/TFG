package Models

import Enums.PhysicalActivityTipe
import java.io.Serializable

data class PhysicalActivityDTO(
    val id: Long? = null,
    val usuario_id: Long,
    val fecha: String,  // YYYY-MM-DD
    val tipo: PhysicalActivityTipe,
    val duracion: Int   // En minutos
):Serializable
