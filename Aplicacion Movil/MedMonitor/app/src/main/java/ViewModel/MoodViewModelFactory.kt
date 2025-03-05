package ViewModel

import Repositories.MoodRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MoodViewModelFactory(private val moodRepository: MoodRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoodViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoodViewModel(moodRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
