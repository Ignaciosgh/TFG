package ViewModel

import Models.Mood
import Models.MoodDTO
import Repositories.MoodRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MoodViewModel(private val moodRepository: MoodRepository) : ViewModel() {

    private val _moods = MutableLiveData<List<Mood>>() // Variable privada
    val moods: LiveData<List<Mood>> get() = _moods

    // Lógica para obtener estados de ánimo usando coroutines o callbacks
    fun getMoodsByUser(userId: Long) {
        moodRepository.getMoodsByUser(userId) { moods ->
            _moods.postValue(moods ?: emptyList())
        }
    }

    fun createMood(moodDTO: MoodDTO, callback: (Boolean) -> Unit) {
        moodRepository.createMood(moodDTO) {
            callback(it != null)
        }
    }

    fun updateMood(id: Long, moodDTO: MoodDTO, callback: (Boolean) -> Unit) {
        moodRepository.updateMood(id, moodDTO) {
            callback(it != null)
        }
    }

    fun deleteMood(moodId: Long, callback: (Boolean) -> Unit) {
        moodRepository.deleteMood(moodId) { success ->
            callback(success)
        }
    }





}
