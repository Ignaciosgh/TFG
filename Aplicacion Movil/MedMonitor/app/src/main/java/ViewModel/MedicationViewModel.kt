package ViewModel

import Models.Medication
import Models.MedicationDTO
import Repositories.MedicationRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MedicationViewModel(private val medicationRepository: MedicationRepository) : ViewModel() {

    private val _medications = MutableLiveData<List<Medication>>() // Variable privada
    val medications: LiveData<List<Medication>> get() = _medications

    // Lógica para obtener medicamentos usando coroutines
    fun getMedicationsByUser(userId: Long) {
        medicationRepository.getMedicationsByUser(userId) { medications ->
            _medications.postValue(medications ?: emptyList())
        }
    }

    fun createMedication(medicationDTO: MedicationDTO, callback: (Boolean) -> Unit) {
        medicationRepository.createMedication(medicationDTO) {
            callback(it != null)
        }
    }

    fun updateMedication(id: Long, medication: Medication, callback: (Boolean) -> Unit) {
        medicationRepository.updateMedication(id, medication) {
            callback(it != null)
        }
    }

    fun deleteMedication(id: Long, callback: (Boolean) -> Unit) {
        medicationRepository.deleteMedication(id) { success ->
            callback(success)
        }
    }

    fun loadMedications(userId: Long) {
        medicationRepository.getMedicationsByUser(userId) { medications ->
            _medications.postValue(medications ?: emptyList())
        }
    }

    fun updateMedicationStatus(medicationId: Long, status: String, callback: (Boolean) -> Unit) {
        medicationRepository.updateMedicationStatus(medicationId, status) { success ->
            callback(success)
        }
    }



}

