package ViewModel

import Models.Reminder
import Repositories.ReminderRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReminderViewModel(private val reminderRepository: ReminderRepository) : ViewModel() {

    private val _reminders = MutableLiveData<List<Reminder>>()
    val reminders: LiveData<List<Reminder>> get() = _reminders

    fun loadReminders(userId: Long) {
        reminderRepository.getRemindersByUser(userId) { reminders ->
            _reminders.postValue(reminders ?: emptyList())
        }
    }
}
