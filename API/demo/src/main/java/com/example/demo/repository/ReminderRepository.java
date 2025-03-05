package com.example.demo.repository;

import com.example.demo.model.ActivityLogs;
import com.example.demo.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository  extends JpaRepository<Reminder, Long> {
    @Query("SELECT r FROM Reminder r WHERE r.medicamento_id.usuario.id = :userId")
    List<Reminder> findByUserId(@Param("userId") Long userId);
}
