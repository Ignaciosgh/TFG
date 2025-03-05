package ViewModel

import Repositories.AppintmentRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConsultationViewModelFactory(private val consultationRepository: AppintmentRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConsultationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConsultationViewModel(consultationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
