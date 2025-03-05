package com.example.demo.service;

import com.example.demo.model.MedicationHistory;
import com.example.demo.repository.MedicationHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationHistoryService {
    private final MedicationHistoryRepository medicationHistoryRepository;

    public MedicationHistoryService(MedicationHistoryRepository medicationHistoryRepository) {
        this.medicationHistoryRepository = medicationHistoryRepository;
    }

    public List<MedicationHistory> findAll() {
        return medicationHistoryRepository.findAll();
    }

    public MedicationHistory findById(Long id) {
        return medicationHistoryRepository.findById(id).orElse(null);
    }

    public MedicationHistory save(MedicationHistory medicationHistory) {
        return medicationHistoryRepository.save(medicationHistory);
    }

    public MedicationHistory update(Long id, MedicationHistory medicationHistoryDetails) {
        return medicationHistoryRepository.findById(id).map(medicationHistory -> {
            medicationHistory.setFecha(medicationHistoryDetails.getFecha());
            medicationHistory.setHora(medicationHistoryDetails.getHora());
            medicationHistory.setEstado(medicationHistoryDetails.getEstado());
            return medicationHistoryRepository.save(medicationHistory);
        }).orElse(null);
    }

    public boolean delete(Long id) {
        return medicationHistoryRepository.findById(id).map(medicationHistory -> {
            medicationHistoryRepository.delete(medicationHistory);
            return true;
        }).orElse(false);
    }
}
