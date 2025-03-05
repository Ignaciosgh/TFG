package ViewModel

import Models.PhysicalActivity
import Models.PhysicalActivityDTO
import Repositories.PhysicalActivityRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhysicalActivityViewModel(private val physicalActivityRepository: PhysicalActivityRepository) : ViewModel() {

    private val _activities = MutableLiveData<List<PhysicalActivity>>() // Variable privada
    val activities: LiveData<List<PhysicalActivity>> get() = _activities

    // Lógica para obtener actividades físicas usando coroutines o callbacks
    fun getActivitiesByUser(userId: Long) {
        physicalActivityRepository.getActivitiesByUser(userId) { activities ->
            _activities.postValue(activities ?: emptyList())
        }
    }

    fun createPhysicalActivity(dto: PhysicalActivityDTO, callback: (Boolean) -> Unit) {
        physicalActivityRepository.createPhysicalActivity(dto, callback)
    }

    fun updatePhysicalActivity(dto: PhysicalActivityDTO, callback: (Boolean) -> Unit) {
        physicalActivityRepository.updatePhysicalActivity(dto, callback)
    }

    fun deleteActivity(id: Long, callback: (Boolean) -> Unit) {
        physicalActivityRepository.deleteActivity(id, callback)
    }
}
