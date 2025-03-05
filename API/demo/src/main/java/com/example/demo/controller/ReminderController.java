package com.example.demo.controller;

import com.example.demo.model.Reminder;
import com.example.demo.service.ReminderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reminders")
public class ReminderController {

    private final ReminderService reminderService;


    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping
    public List<Reminder> getAllReminder() {
        return reminderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reminder> getReminderById(@PathVariable Long id) {
        Reminder reminder = reminderService.findById(id);
        return reminder != null ? ResponseEntity.ok(reminder) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Reminder createActivity(@RequestBody Reminder reminder) {
        return reminderService.save(reminder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reminder> updateActivity(@PathVariable Long id, @RequestBody Reminder reminder) {
        Reminder updatedReminder = reminderService.update(id, reminder);
        return reminder != null ? ResponseEntity.ok(updatedReminder) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReminder(@PathVariable Long id) {
        boolean deleted = reminderService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reminder>> getRemindersByUserId(@PathVariable Long userId) {
        List<Reminder> reminders = reminderService.getRemindersByUserId(userId);
        if (reminders.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204 si no hay recordatorios
        }
        return ResponseEntity.ok(reminders); // HTTP 200 con los recordatorios
    }

}
