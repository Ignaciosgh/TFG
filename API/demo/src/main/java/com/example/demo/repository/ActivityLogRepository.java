package com.example.demo.repository;

import com.example.demo.model.ActivityLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLogs, Long> {

}
