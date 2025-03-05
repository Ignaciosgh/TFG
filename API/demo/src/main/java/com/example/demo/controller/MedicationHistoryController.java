package com.example.demo.controller;

import com.example.demo.model.MedicationHistory;
import com.example.demo.service.MedicationHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medication-history")
public class MedicationHistoryController {

    private final MedicationHistoryService medicationHistoryService;

    public MedicationHistoryController(MedicationHistoryService medicationHistoryService) {
        this.medicationHistoryService = medicationHistoryService;
    }

    @GetMapping
    public List<MedicationHistory> getAllMedicationHistories() {
        return medicationHistoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationHistory> getMedicationHistoryById(@PathVariable Long id) {
        MedicationHistory medicationHistory = medicationHistoryService.findById(id);
        return medicationHistory != null ? ResponseEntity.ok(medicationHistory) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public MedicationHistory createMedicationHistory(@RequestBody MedicationHistory medicationHistory) {
        return medicationHistoryService.save(medicationHistory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationHistory> updateMedicationHistory(@PathVariable Long id, @RequestBody MedicationHistory medicationHistoryDetails) {
        MedicationHistory updatedMedicationHistory = medicationHistoryService.update(id, medicationHistoryDetails);
        return updatedMedicationHistory != null ? ResponseEntity.ok(updatedMedicationHistory) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicationHistory(@PathVariable Long id) {
        boolean deleted = medicationHistoryService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
