package Models

import Enums.Estado
import Enums.EstadoAnimo
import java.io.Serializable
import java.time.LocalDate

data class MoodDTO(
    val id: Long? = null,
    val usuario_id: Long,
    val fecha: String,
    val estado: EstadoAnimo,
    val nota: String
): Serializable
