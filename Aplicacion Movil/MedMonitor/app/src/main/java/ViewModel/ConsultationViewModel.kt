package ViewModel

import Models.Appointment
import Models.AppointmentDTO
import Network.ApiServices
import Repositories.AppintmentRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConsultationViewModel(private val consultationRepository: AppintmentRepository) : ViewModel() {

    private val _consultations = MutableLiveData<List<Appointment>>() // Variable privada
    val consultations: LiveData<List<Appointment>> get() = _consultations

    // Lógica para obtener consultas médicas usando coroutines o callbacks
    fun getConsultationsByUser(userId: Long) {
        consultationRepository.getConsultationsByUser(userId) { consultations ->
            _consultations.postValue(consultations ?: emptyList())
        }
    }

    fun createAppointment(appointmentDTO: AppointmentDTO, callback: (Boolean) -> Unit) {
        consultationRepository.createAppointment(appointmentDTO) {
            callback(it != null)
        }
    }

    fun deleteAppointment(id: Long, callback: (Boolean) -> Unit) {
        consultationRepository.deleteAppointment(id) { success ->
            callback(success)
        }
    }

    fun updateAppointment(id: Long, appointment: Appointment, callback: (Boolean) -> Unit) {
        consultationRepository.updateAppointment(id, appointment) {
            callback(it != null)
        }
    }

}
