package com.example.demo.service;

import com.example.demo.model.Medication;
import com.example.demo.model.Reminder;
import com.example.demo.repository.ReminderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;


    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public List<Reminder> findAll() {
        return reminderRepository.findAll();
    }

    public Reminder findById(Long id) {
        return reminderRepository.findById(id).orElse(null);
    }

    public Reminder save(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    public Reminder update(Long id, Reminder reminderDetails) {
        return reminderRepository.findById(id).map(reminder -> {
            reminder.setFrecuencia(reminderDetails.getFrecuencia());
            reminder.setHora(reminderDetails.getHora());
            reminder.setMedicamento_id(reminder.getMedicamento_id());
            return reminderRepository.save(reminder);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        return reminderRepository.findById(id).map(medication -> {
            reminderRepository.delete(medication);
            return true;
        }).orElse(false);
    }

    public List<Reminder> getRemindersByUserId(Long userId) {
        return reminderRepository.findByUserId(userId);
    }
}
