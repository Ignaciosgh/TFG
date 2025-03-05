package Models

import java.io.Serializable

data class AppointmentDTO(
    val id: Long? = null,
    val usuario_id: Long,
    val fecha: String, // En formato "YYYY-MM-DD"
    val hora: String, // En formato "HH:MM"
    val descripcion: String
) : Serializable