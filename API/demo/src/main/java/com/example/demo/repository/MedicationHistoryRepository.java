package com.example.demo.repository;

import com.example.demo.model.MedicationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationHistoryRepository extends JpaRepository<MedicationHistory, Long> {

}
