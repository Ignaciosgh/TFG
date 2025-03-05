package ViewModel

import Repositories.PhysicalActivityRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PhysicalActivityViewModelFactory(private val physicalActivityRepository: PhysicalActivityRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhysicalActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhysicalActivityViewModel(physicalActivityRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
