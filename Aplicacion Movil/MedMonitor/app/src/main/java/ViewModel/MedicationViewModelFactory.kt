package ViewModel

import Repositories.MedicationRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MedicationViewModelFactory(private val medicationRepository: MedicationRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicationViewModel(medicationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
